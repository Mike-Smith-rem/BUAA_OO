import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;

import java.io.IOException;

public class InputThread extends Thread {
    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        Scheduler scheduler = new Scheduler(elevatorInput.getArrivingPattern());
        scheduler.setName("Thread Scheduler");
        scheduler.start();
        while (true) {
            Request request = elevatorInput.nextRequest();
            if (request == null) {
                scheduler.setEnd(true);
                break;
            } else {
                if (request instanceof PersonRequest) {
                    Person p = new Person((PersonRequest) request);
                    scheduler.setRequestQueue(p);
                } else if (request instanceof ElevatorRequest) {
                    Elevator e = new Elevator(Integer.parseInt(((ElevatorRequest) request).
                            getElevatorId()), ((ElevatorRequest) request).
                            getElevatorType(), scheduler);
                    scheduler.setElevatorQueue(e);
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
