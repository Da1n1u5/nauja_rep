package t120b180.mq.server.service;

import java.io.Serializable;

public class SaunaSensor  implements Serializable {
    private static final long serialVersionUID = 3558550900506597371L;
    private int sensorTemp,
            saunaAverageTemp;

    /**
     * Method to get sauna temperature
     *
     * @return returs sauna temperature
     */
    public int getSaunaTemp() {
        return saunaAverageTemp;
    }

    /**
     * Method to set sauna temperature
     *
     * @param saunaTemp sauna temperature
     */
    public void setSaunaTemp(int saunaTemp) {
        this.saunaAverageTemp = saunaTemp;
    }

    /**
     * Sauna sensor constructor
     */
    public SaunaSensor() {
    }

    /**
     * Methos to set sensor temperature
     *
     * @param sensorTemp sensor temperature
     */
    public void setSensorTemp(int sensorTemp) {
        this.sensorTemp = sensorTemp;
    }

    /**
     * Method to get sensor temp
     *
     * @return returns sensor temperature
     */
    public int getSensorTemp() {
        return sensorTemp;
    }
}
