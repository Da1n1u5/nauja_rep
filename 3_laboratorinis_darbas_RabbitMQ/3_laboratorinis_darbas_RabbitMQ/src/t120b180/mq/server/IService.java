package t120b180.mq.server;

import t120b180.mq.server.service.Boiler;
import t120b180.mq.server.service.SaunaSensor;

/**
 * Service interface. Each methods corresponds to single (add two numbers) operation call in different 
 * service/client interaction model.
 */
public interface IService {

	/**
	 * Method to update average temperature and make energy adjustments
	 *
	 * @param sensor Sensor temperature
	 * @return returns SensorResult object
	 */
	SaunaSensor updateTemperature(SaunaSensor sensor);

	/**
	 * Method to get Boiler object with all parameters(energy and deflection, average temperature)
	 *
	 * @return return Boiler object
	 */
	Boiler getRequiredEnergy(Boiler boiler);
}
