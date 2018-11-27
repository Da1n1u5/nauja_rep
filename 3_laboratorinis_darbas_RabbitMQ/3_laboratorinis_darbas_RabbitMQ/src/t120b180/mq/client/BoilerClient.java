package t120b180.mq.client;
import t120b180.mq.server.service.Boiler;

import java.util.UUID;

public class BoilerClient {
    /**
     * Program entry point.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        BoilerClient self = new BoilerClient();
        self.run();
    }

    /**
     *
     */
    private void run() {
        try {
            //get service wrapper
            String randomClientID = UUID.randomUUID().toString();
            ServiceClient service = new ServiceClient(randomClientID, "boiler");

            while( true ) { {
                    Boiler initialBoiler = new Boiler();
                    Boiler finalBoiler = service.getRequiredEnergy(initialBoiler);
                    System.out.println(String.format("Reikalinga temperatura: %d°C,"
                                    + "Vidutine temperatura: %d°C,"
                                    + " reikalinga energija: %.2fJ., energijos nuokrypis: %.2fJ.",
                            finalBoiler.getRequiredTemperature(),
                            finalBoiler.getAverageTemperature(),
                            finalBoiler.getBoilerEnergy(),
                            finalBoiler.getEnergyDeflection()
                    ));
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
}
