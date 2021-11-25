import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

import java.io.IOException;

public class Input extends Thread {
    private final Floor floor;
    private static Elevator elevator;

    public Input(Floor floor) {
        this.floor = floor;
        elevator = new Elevator(floor);
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        elevator.setType(elevatorInput.getArrivingPattern());
        elevator.setName("Elevator Thread");
        elevator.start();
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            if (request == null) {
                floor.setIsEnd(true);
                break;
            } else {
                floor.adds(request);
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

