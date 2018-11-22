package t120b180.mq.client;

import java.util.UUID;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

import t120b180.mq.Config;

import t120b180.mq.server.service.Boiler;
import t120b180.mq.server.service.SaunaSensor;
import t120b180.mq.util.SerializationUtil;

public class ServiceClient implements IService {
	//client queue names (client ID is added in constructor)
	private String saunaSensorQueueName = "saunaSensor#client#";
	private String boilerQueueName = "boiler#server";
	//exchange and queue names for dedicated responses (client ID is added in constructor)
	private String dedicatedResponseEchangeName = "dedicateResponseExchange#client#";
	private String dedicateResponseQueueName = "dedicateResponseQeue#client#";



	/** Channel to request queue in shared request queue only model. */
	private Channel saunaSensorChan;
	private Channel boilerChan;

	/** Channel to dedicate response queue in shared request queue only model. */
	private Channel dedicatedResponseChan;

	/** Consumer to read messages from dedicated response queue in shared request queue only model. */
	private QueueingConsumer dedicatedResponseConsumer;

	/**
	 * Constructor.
	 * @param clientId ID to use for building unique client queue names.
	 */
	public ServiceClient(String clientId) throws Exception {
		//validate inputs
		assert clientId != null : "Argument 'clientId' is null.";

		//build client-unique queue names
		saunaSensorQueueName += clientId;
		boilerQueueName += clientId;

		//build client-unique names for dedicate response exchange and queue
		dedicatedResponseEchangeName += clientId;
		dedicateResponseQueueName += clientId;

		//connect to message broker
		ConnectionFactory connFact = new ConnectionFactory();
		connFact.setHost("localhost");

		Connection conn = connFact.newConnection();

		//connect-to/create service queues (using one fanout exchange per queue)

		saunaSensorChan = connectToQueue(conn, Config.saunaSensorExchangeName, saunaSensorQueueName);

		boilerChan = connectToQueue(conn, Config.boilerExchangeName, boilerQueueName);

		dedicatedResponseChan = connectToQueue(conn, dedicatedResponseEchangeName, dedicateResponseQueueName);

		//create response queue message consumers, bind to channels (use auto-ack mode)

		dedicatedResponseConsumer = new QueueingConsumer(dedicatedResponseChan);
		dedicatedResponseChan.basicConsume(dedicateResponseQueueName, true, dedicatedResponseConsumer);
	}

	/**
	 * Connect-to/create a given queue. Uses one exchange per queue.
	 * @param conn Connection to use.
	 * @param exchangeName Name of the exchange to connect the queue to.
	 * @param queueName Name of the queue to connect to.
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

	@Override
	public SaunaSensor updateTemperature(SaunaSensor sensor) throws Exception{
		//generate system wide unique correlation ID by using UUID generator
		String corrId = UUID.randomUUID().toString();

		//build request

		AMQP.BasicProperties reqProps =
				(new AMQP.BasicProperties.Builder())
						.correlationId(corrId)
						.replyTo(dedicatedResponseEchangeName)
						.build();

		//send request
		saunaSensorChan.basicPublish(
				Config.saunaSensorExchangeName, "", reqProps,
				SerializationUtil.serialize(sensor)
		);

		//read response
		while( true ) {
			//receive next message from queue
			Delivery delivery = dedicatedResponseConsumer.nextDelivery(1000);

			//ignore delivery timeouts
			if( delivery == null ) {
				continue;
			}

			//ignore outdated messages
			if( delivery.getProperties().getCorrelationId().compareTo(corrId) == 0 ) {
				//get message object
				Object msgObj = SerializationUtil.deserialize(delivery.getBody());

				//check if this is a reply message
				if( msgObj instanceof SaunaSensor ) {
					return (SaunaSensor) msgObj;
				}
			}
		}
	}

	@Override
	public Boiler getRequiredEnergy(Boiler boiler) throws Exception{
		//generate system wide unique correlation ID by using UUID generator
		String corrId = UUID.randomUUID().toString();

		//build request

		AMQP.BasicProperties reqProps =
				(new AMQP.BasicProperties.Builder())
						.correlationId(corrId)
						.replyTo(dedicatedResponseEchangeName)
						.build();

		//send request
		boilerChan.basicPublish(
				Config.boilerExchangeName, "", reqProps,
				SerializationUtil.serialize(boiler)
		);

		//read response
		while( true ) {
			//receive next message from queue
			Delivery delivery = dedicatedResponseConsumer.nextDelivery(1000);

			//ignore delivery timeouts
			if( delivery == null ) {
				continue;
			}

			//ignore outdated messages
			if( delivery.getProperties().getCorrelationId().compareTo(corrId) == 0 ) {
				//get message object
				Object msgObj = SerializationUtil.deserialize(delivery.getBody());

				//check if this is a reply message
				if( msgObj instanceof Boiler ) {
					return (Boiler) msgObj;
				}
			}
		}
	}

}
