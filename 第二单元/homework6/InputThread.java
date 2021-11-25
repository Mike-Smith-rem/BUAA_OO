import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.ElevatorRequest;
import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.Request;

import java.io.IOException;

public class InputThread extends Thread {
    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        Scheduler scheduler = new Scheduler(elevatorInput.getArrivingPattern());
        scheduler.setName("Thread scheduler");
        scheduler.start();
        while (true) {
            Request request = elevatorInput.nextRequest();
            if (request == null) {
                scheduler.setEnd(true);
                break;
            } else {
                if (request instanceof PersonRequest) {
                    scheduler.addRequest((PersonRequest) request);
                } else if (request instanceof ElevatorRequest) {
                    scheduler.addElevator(Integer.
                            parseInt(((ElevatorRequest) request).getElevatorId()));
                }
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
