package t120b180.mq;

/**
 * Configuration settings.<br/>
 * <br/>
 * Note that in RabbitMQ a discrete point of message exchange is called "exchange". Messages are
 * placed into exchanges and then distributed to queues connected to those exchanges. Distribution is
 * done based on message routing keys and echange-queue configuration (except in fanout exchanges that
 * simply broadcast to all connected queues).<br/>
 * <br/>
 * In this example, we imitate ordinary broadcast queues by using fanout exchanges connected to
 * personal queues of service participants.<br/> 
 * <br/>
 * Static members are thread safe instance members are not.
 */
public class Config {
	/** Name for exchange in one shared two way queue model. */
	//public static String oneSharedExchangeName = "sharedOneQueue";

	/** Name for request exchange in two shared one way queues model. */
	//public static String twoQueuesSharedRequestExchangeName = "twoQueuesSharedRequests";

	/** Name for response exchange in two shared one way queues model. */
	//public static String twoQueuesSharedResponseExchangeName = "twoQueuesSharedResponses";

	/** Name for request exchange in shared request queue only model. */
	public static String saunaSensorExchangeName = "saunaSenorExchange";

	public static String boilerExchangeName = "boilerExchange";
}
