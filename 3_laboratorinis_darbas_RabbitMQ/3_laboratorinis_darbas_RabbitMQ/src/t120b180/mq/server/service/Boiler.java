package t120b180.mq.server.service;

import java.io.Serializable;

public class Boiler implements Serializable {
    /** Generated UID for Serializable. */
    private static final long serialVersionUID = -5010684307474737296L;

    private int requiredTemperature, averageTemperature;
    private double boilerEnergy, energyDeflection;

    /**
     * Boiler constructor
     */
    public Boiler() {
    }

    /**
     * Method to get sauna's average temperature
     *
     * @return returns sauna's average temperature
     */
    public int getAverageTemperature() {
        return averageTemperature;
    }

    /**
     * Method to set average sauna's temperature
     *
     * @param averageTemperature average sauna's temperature
     */
    public void setAverageTemperature(int averageTemperature) {
        this.averageTemperature = averageTemperature;
    }

    /**
     * Method to get required sauna's temperature
     *
     * @return
     */
    public int getRequiredTemperature() {
        return requiredTemperature;
    }

    /**
     * Method to set required sauna's temperature
     *
     * @param requiredTemperature reguired temperature
     */
    public void setRequiredTemperature(int requiredTemperature) {
        this.requiredTemperature = requiredTemperature;
    }

    /**
     * Method to get Boiler energy
     *
     * @return returs Boiler energy
     */
    public double getBoilerEnergy() {
        return boilerEnergy;
    }

    /**
     * Method to set Boiler energy
     *
     * @param BoilerEnergy Boiler energy
     */
    public void setBoilerEnergy(double BoilerEnergy) {
        this.boilerEnergy = BoilerEnergy;
    }

    /**
     * Method to get Boiler energy deflection
     *
     * @return returns energy deflection
     */
    public double getEnergyDeflection() {
        return energyDeflection;
    }

    /**
     * Method to set Boiler energy deflection
     *
     * @param energyDeflection Boiler energy deflection
     */
    public void setEnergyDeflection(double energyDeflection) {
        this.energyDeflection = energyDeflection;
    }
}
