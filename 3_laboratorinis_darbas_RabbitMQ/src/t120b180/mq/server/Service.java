package t120b180.mq.server;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import t120b180.mq.server.service.Boiler;
import t120b180.mq.server.service.SaunaSensor;

public class Service implements IService {
    private ServerState serverState;

    /**
     * Service constructor
     *
     * @param initialTemp
     */
    public Service(int initialTemp) {
        serverState = new ServerState();
        serverState.saunaRequiredTemperature = initialTemp;
        serverState.energy = getEnergyByTemperature(initialTemp);
        serverState.sensorTemperatures = new CircularFifoQueue<>(10);
    }


    /**
     * Method to update average temperature and make energy adjustments
     *
     * @param sensor Sensor temperature
     * @return returns SensorResult object
     */
    @Override
    public SaunaSensor updateTemperature(SaunaSensor sensor) {
        synchronized (this) {
            serverState.sensorTemperatures.add(sensor.getSensorTemp());
            GetAverageTemp();
            makeEnergyAdjustements(serverState.saunaRequiredTemperature,
                    serverState.saunaAverageTemperature);
            sensor.setSaunaTemp(serverState.saunaAverageTemperature);
            return sensor;
        }
    }

    /**
     * Method to get Boiler object with all parameters(energy,average temperature)
     *
     * @return Boiler object
     */
    @Override
    public Boiler getRequiredEnergy(Boiler boiler) {
        synchronized (this) {
            boiler.setEnergyDeflection(getEnergyByTemperature(
                    serverState.saunaRequiredTemperature
                            - serverState.saunaAverageTemperature));
            boiler.setBoilerEnergy(serverState.energy);
            boiler.setAverageTemperature(serverState.saunaAverageTemperature);
            boiler.setRequiredTemperature(serverState.saunaRequiredTemperature);
            return boiler;
        }
    }

    /**
     * Method to get energy by emperature
     *
     * @param temperature temperature
     * @return returns energy
     */
    private static double getEnergyByTemperature(int temperature) {
        return temperature * 1000;
    }

    /**
     * Method to calculate average temperature
     */
    private void GetAverageTemp() {
        int sum = 0;
        for (double item : serverState.sensorTemperatures) {
            sum += item;
        }
        serverState.saunaAverageTemperature = sum
                / serverState.sensorTemperatures.size();
    }

    /**
     * Method to make energy adjustments acording to temperatures
     *
     * @param requiredTemp required temperature
     * @param averageTemp  average temperature
     */
    private void makeEnergyAdjustements(int requiredTemp,
                                        int averageTemp) {
        int tempDeflection = requiredTemp - averageTemp;
        if (tempDeflection == 0) {
        } else {
            serverState.energy += getEnergyByTemperature(tempDeflection);
        }
    }

}
