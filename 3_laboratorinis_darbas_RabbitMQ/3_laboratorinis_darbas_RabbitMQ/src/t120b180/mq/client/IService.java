package t120b180.mq.client;

import t120b180.mq.server.service.Boiler;
import t120b180.mq.server.service.SaunaSensor;

import java.io.IOException;

/**
 * Service interface. Each methods corresponds to single (add two numbers) operation call in different 
 * service/client interaction model.<br/>
 * <br/>
 * Interface is redeclared on client side, because client side RPC calls need to throw exceptions.
 */
public interface IService {

	SaunaSensor updateTemperature(SaunaSensor sensor) throws Exception;

	Boiler getRequiredEnergy(Boiler boiler) throws Exception;
}
