package t120b180.mq.server;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import t120b180.mq.Config;
import t120b180.mq.server.service.Boiler;
import t120b180.mq.server.service.SaunaSensor;
import t120b180.mq.util.SerializationUtil;

/**
 * Service wrapper.<br/>
 * <br/>
 * Static members are thread safe, instance members are not.
 */
public class MQService {
    //queue names

    private final String saunaSensorQueueName = "saunaSensor#server";
    private final String boilerQueueName = "boiler#server";

    /**
     * Lock for accessing service logic.
     */
    private final Object serviceLogicLock = new Object();

    /**
     * Service logic implementation.
     */
    private Service serviceLogic = new Service(80);

    /**
     * Channel to request queue in shared request queue only model.
     */
    private Channel saunaSensorChan;
    private Channel boilerChan;

    /**
     * Constructor.
     */
    public MQService() throws Exception {
        //connect to message broker
        ConnectionFactory connFact = new ConnectionFactory();
        connFact.setHost("localhost");

        Connection conn = connFact.newConnection();

        //connect-to/create service queues (using one fanout exchange per queue)
        saunaSensorChan = connectToQueue(conn, Config.saunaSensorExchangeName, saunaSensorQueueName);

        boilerChan = connectToQueue(conn, Config.boilerExchangeName, boilerQueueName);


        saunaSensorChan.basicConsume(saunaSensorQueueName, true,
                new DefaultConsumer(saunaSensorChan) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope,
                                               AMQP.BasicProperties props, byte[] msg) throws IOException {
                        onTwoQueuesSharedRequestUpdateTemperature(consumerTag, envelope, props, msg);
                    }
                }
        );

        boilerChan.basicConsume(boilerQueueName, true,
                new DefaultConsumer(boilerChan) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope,
                                               AMQP.BasicProperties props, byte[] msg) throws IOException {
                        onTwoQueuesSharedRequestGetRequiredEnergy(consumerTag, envelope, props, msg);
                    }
                }
        );

    }

    /**
     * Connect-to/create a given queue. Uses one exchange per queue.
     *
     * @param conn         Connection to use.
     * @param exchangeName Name of the exchange to connect the queue to.
     * @param queueName    Name of the queue to connect to.
     * @return Channel to the queue.
     */
    private Channel connectToQueue(Connection conn, String exchangeName, String queueName) throws Exception {
        Channel chan = conn.createChannel();
        chan.exchangeDeclare(exchangeName, "fanout", true);
        chan.queueDeclare(queueName, true, false, false, null);
        chan.queueBind(queueName, exchangeName, queueName);

        //
        return chan;
    }

    /**
     * Handles messages received to "request queue in shared request queue only queues model".
     *
     * @param consumerTag Consumer tag. Server generated.
     * @param envelope    Message envelope.
     * @param props       Message properties.
     * @param msg         Message body.
     */
    private void onTwoQueuesSharedRequestUpdateTemperature(String consumerTag, Envelope envelope, AMQP.BasicProperties props, byte[] msg) {
        try {
            //try deserializing message body into object
            Object objMsg = SerializationUtil.deserialize(msg);

            //check if operation request has been received
            if (objMsg instanceof SaunaSensor) {
                //get operation parameters
                SaunaSensor sensor = (SaunaSensor) objMsg;

                //execute operation
                SaunaSensor result;
                synchronized (serviceLogicLock) {
                    System.out.println(String.format(
                            "Calling TwoQueuesSharedRequestUpdateTemperature(sensorTemp = %d) for %s",
                            sensor.getSensorTemp(),  props.getCorrelationId()));
                    result = serviceLogic.updateTemperature(sensor);
                }

                //build and send reply message
                AMQP.BasicProperties replyProps =
                        (new AMQP.BasicProperties.Builder())
                                .correlationId(props.getCorrelationId())
                                .build();

                saunaSensorChan.basicPublish(
                        props.getReplyTo(), "", replyProps,
                        SerializationUtil.serialize(result)
                );
            }
        } catch (Exception | Error e) {
            System.out.println("Unhandled exception caught: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     *
     * @param consumerTag
     * @param envelope
     * @param props
     * @param msg
     */
    private void onTwoQueuesSharedRequestGetRequiredEnergy(String consumerTag, Envelope envelope, AMQP.BasicProperties props, byte[] msg) {
        try {
            //try deserializing message body into object
            Object objMsg = SerializationUtil.deserialize(msg);

            //check if operation request has been received
            if (objMsg instanceof Boiler) {
                //get operation parameters
                Boiler boiler = (Boiler) objMsg;

                //execute operation
                Boiler result;
                synchronized (serviceLogicLock) {
                    System.out.println(String.format(
                            "Calling onTwoQueuesSharedRequestGetRequiredEnergy for %s",
                            props.getCorrelationId()));
                    result = serviceLogic.getRequiredEnergy(boiler);
                }

                //build and send reply message
                AMQP.BasicProperties replyProps =
                        (new AMQP.BasicProperties.Builder())
                                .correlationId(props.getCorrelationId())
                                .build();

                boilerChan.basicPublish(
                        props.getReplyTo(), "", replyProps,
                        SerializationUtil.serialize(result)
                );
            }
        } catch (Exception | Error e) {
            System.out.println("Unhandled exception caught: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
