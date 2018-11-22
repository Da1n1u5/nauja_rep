package t120b180.mq.client;
import t120b180.mq.server.service.SaunaSensor;

import java.util.UUID;

/**
 * Sevice client.<br/>
 * <br/>
 * Static members are thread safe, instance members are not.
 */
public class SaunaSensorClient {
	private final double minTempValue = 50.00;
	private final double maxTempValue = 100.00;
	/**
	 * Program entry point.
	 * @param args Command line arguments.
	 */
	public static void main(String[] args) {
		SaunaSensorClient self = new SaunaSensorClient();
		self.run();
	}
	
	/**
	 * Program body.
	 */
	private void run() {
		try {
			//get service wrapper
			String randomClientID = UUID.randomUUID().toString();
			ServiceClient service = new ServiceClient(randomClientID);

			while( true ) {
				{

					int sensorTemp = getRandomTemperature(minTempValue, maxTempValue);
					SaunaSensor initialSensor = new SaunaSensor();
					initialSensor.setSensorTemp(sensorTemp);

					System.out.println(String.format("initialSensor temperatura=%d°C,"
									+ " temperaturosVidurkis=%d°C",
							initialSensor.getSensorTemp(),
							initialSensor.getSaunaTemp()));

					SaunaSensor finalSensor = service.updateTemperature(initialSensor);
					System.out.println(String.format("Po iskvietimo initialSensor "
									+ "temperatura=%d°C, temperaturosVidurkis=%d°C",
							initialSensor.getSensorTemp(),
							initialSensor.getSaunaTemp()));

					System.out.println(String.format("Po iskvietimo finalSensor"
									+ " temperatura=%d°C, temperaturosVidurkis=%d°C",
							finalSensor.getSensorTemp(),
							finalSensor.getSaunaTemp()));
					System.out.println("");
				}				

				
				//wait a bit
				Thread.sleep(2000);
			}
		}
		catch( Exception | Error e ) {
			System.out.println("Unhandled exception caught: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * method to get integer result of random generated temperature
	 *
	 * @param min minimum temperature
	 * @param max maxmimum temperature
	 * @return returns random generated temperature
	 */
	static int getRandomTemperature(double min, double max) {
		double val = min + Math.random() * (max - min);
		return (int) Math.round(val);
	}
}
