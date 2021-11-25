import com.oocourse.TimableOutput;
import com.oocourse.elevator1.PersonRequest;

import java.util.Vector;

public class Elevator extends Thread {
    private int curFloor = 1;
    private int direction = 0;
    private int destination = 1;
    private final Vector<PersonRequest> requests = new Vector<>();
    private int people = 0;
    private final Floor floor;
    private static String type = "";

    public Elevator(Floor floor) {
        this.floor = floor;
    }

    public synchronized void setType(String s) {
        Elevator.type = s;
        this.notifyAll();
    }

    public void open() {
        TimableOutput.println("OPEN-" + curFloor);
    }

    public void close() {
        TimableOutput.println("CLOSE-" + curFloor);
    }

    public void in() {
        open();
        try {
            sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Vector<PersonRequest> removable = new Vector<>();
        for (PersonRequest p : floor.getFloors().get(curFloor)) {
            if (direction == 0) {
                direction = Integer.compare(p.getToFloor(), curFloor);
            }
            if (people == 6) {
                break;
            }
            if (Integer.compare(p.getToFloor(), curFloor) != direction
                && !type.equals("Night")) {
                continue;
            }
            removable.add(p);
            requests.add(p);
            people++;
            TimableOutput.println("IN-" + p.getPersonId() + "-" + curFloor);
        }
        for (PersonRequest p : removable) {
            floor.removes(p);
        }
        close();
    }

    public void out() {
        open();
        try {
            sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Vector<PersonRequest> removable = new Vector<>();
        for (PersonRequest p : requests) {
            if (p.getToFloor() == curFloor) {
                removable.add(p);
                people--;
                TimableOutput.println("OUT-" + p.getPersonId() + "-" + curFloor);
            }
        }
        for (PersonRequest p : removable) {
            requests.remove(p);
        }
        close();
    }

    public void move() {
        try {
            sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        direction = Integer.compare(destination - curFloor, 0);
        curFloor = curFloor + direction;
        direction = Integer.compare(destination - curFloor, 0);
        TimableOutput.println("ARRIVE-" + curFloor);
    }

    @Override
    public void run() {
        synchronized (this) {
            if (type.equals("")) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        while (true) {
            synchronized (floor) {
                while (floor.isEmpty()) {
                    if (floor.getIsEnd()) {
                        return;
                    }
                    try {
                        floor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (floor.getIsEnd()) {
                        return;
                    }
                }
            }
            //initial
            for (int i = 1; i <= 20; i++) {
                if (!floor.getFloors().get(i).isEmpty()) {
                    PersonRequest p = floor.getFloors().get(i).get(0);
                    destination = p.getFromFloor();
                    break;
                }
            }
            //
            do {
                if (isOut()) {
                    out();
                }
                else if (isIn()) {
                    in();
                }
                switch (type) {
                    case "Random":
                        destination = setDestinationA();
                        break;
                    case "Night":
                        destination = setDestinationB();
                        break;
                    case "Morning":
                        destination = setDestinationC();
                        break;
                    default:
                        break;
                }
                if (destination != curFloor) {
                    move();
                }
            } while (people > 0);
        }
    }

    public boolean isIn() {
        boolean stop = false;
        for (PersonRequest p : floor.getFloors().get(curFloor)) {
            int direct = Integer.compare(p.getToFloor() - curFloor, 0);
            if (direct == direction || (people == 0 && direction == 0) ||
                   type.equals("Night")) {
                stop = people < 6;
                break;
            }
        }
        return stop;
    }

    public boolean isOut() {
        boolean stop = false;
        for (PersonRequest p : requests) {
            if (p.getToFloor() == curFloor) {
                stop = true;
            }
        }
        return stop;
    }

    public int setDestinationA() {
        int des = destination;
        int max = 0;
        for (PersonRequest p : requests) {
            if ((p.getToFloor() - curFloor) * direction > max) {
                des = p.getToFloor();
                max = Math.abs(p.getToFloor() - curFloor);
            }
        }
        return des;
    }

    public int setDestinationB() {
        int des = destination;
        int max = 0;
        int visualPeople = people;
        if (visualPeople < 6 && !floor.isEmpty()) {
            for (int i = curFloor; i <= 20; i++) {
                for (PersonRequest p : floor.getFloors().get(i)) {
                    if (visualPeople >= 6) {
                        break;
                    }
                    if (Math.abs(p.getFromFloor() - curFloor) >= max) {
                        visualPeople++;
                        des = p.getFromFloor();
                        max = Math.abs(p.getToFloor() - curFloor);
                    }
                }
                if (visualPeople >= 6) {
                    break;
                }
            }
        } else {
            des = 1;
        }
        return des;
    }

    public int setDestinationC() {
        int des = destination;
        int max = 0;
        for (PersonRequest p : requests) {
            if (Math.abs(p.getToFloor() - curFloor) > max) {
                des = p.getToFloor();
                max = Math.abs(p.getToFloor() - curFloor);
            }
        }
        return des;
    }
}
