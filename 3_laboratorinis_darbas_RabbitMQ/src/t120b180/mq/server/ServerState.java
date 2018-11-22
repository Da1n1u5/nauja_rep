package t120b180.mq.server;

import java.util.Queue;
/**
 * Server State class
 */
public class ServerState {
    int saunaRequiredTemperature,
            saunaAverageTemperature;
    double energy;
    Queue<Integer> sensorTemperatures;
}
