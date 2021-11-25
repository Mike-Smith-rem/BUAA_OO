import com.oocourse.TimableOutput;
import com.oocourse.elevator2.PersonRequest;

import java.util.Vector;

public class Elevator extends Thread {
    private int curFloor = 1;
    private int targetFloor = 1;
    private int direction = 0;
    private final int id;
    private int people = 0;
    private boolean isEnd = false;
    private String state;
    private final Scheduler scheduler;
    private final Vector<PersonRequest> personVector = new Vector<>();
    private final Vector<PersonRequest> requestVector = new Vector<>();

    Elevator(int id, Scheduler scheduler) {
        this.scheduler = scheduler;
        this.id = id;
        this.setName("Elevator" + id);
        this.state = "wait";
    }

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                while (personVector.isEmpty() && requestVector.isEmpty() && !isEnd) {
                    changeState("wait");
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (isEnd && personVector.isEmpty() && requestVector.isEmpty()) {
                    return;
                }
                changeState("working");
                targetFloor = setTargetFloor();
                //also change the direction
            }
            do {
                if (isOut()) {
                    open();
                    out();
                    close();
                }
                if (isIn()) {
                    if (scheduler.getType().equals("Morning")) {
                        try {
                            sleep(1600);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    open();
                    in();
                    close();
                }
                targetFloor = setTargetFloor();
                direction = Integer.compare(targetFloor, curFloor);
                if (curFloor != targetFloor) {
                    move();
                }
            } while (!(personVector.isEmpty() && requestVector.isEmpty()));
        }
    }

    public void move() {
        try {
            sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        curFloor = curFloor + direction;
        TimableOutput.println("ARRIVE-" + curFloor + "-" + id);
    }

    public void open() {
        TimableOutput.println("OPEN-" + curFloor + "-" + id);
    }

    public void close() {
        TimableOutput.println("CLOSE-" + curFloor + "-" + id);
    }

    public boolean isIn() {
        for (PersonRequest request : requestVector) {
            if (curFloor == request.getFromFloor() && people < 6) {
                if (direction == 0) {
                    direction = Integer.compare(request.getToFloor()
                            , curFloor);
                }
                if (direction != 0 && Integer.compare(request.getToFloor()
                        , curFloor) == direction) {
                    return true;
                }
            }
        }
        return false;
    }

    public void in() {
        try {
            sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < requestVector.size(); i++) {
            PersonRequest p = requestVector.get(i);
            if (curFloor == p.getFromFloor() && people < 6
                && direction == Integer.compare(p.getToFloor(), curFloor)) {
                TimableOutput.println("IN-" + p.getPersonId() +
                        "-" + curFloor + "-" + id);
                people++;
                requestVector.remove(p);
                personVector.add(p);
                i--;
            }
        }
    }

    public boolean isOut() {
        for (PersonRequest request : personVector) {
            if (curFloor == request.getToFloor()) {
                return true;
            }
        }
        return false;
    }

    public void out() {
        try {
            sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < personVector.size(); i++) {
            if (curFloor == personVector.get(i).getToFloor()) {
                PersonRequest p = personVector.get(i);
                TimableOutput.println("OUT-" + p.getPersonId() +
                        "-" + curFloor + "-" + id);
                people--;
                personVector.remove(p);
                i--;
            }
        }
    }

    public synchronized int getDirection() {
        return direction;
    }

    public int setTargetFloor() {
        int visualPeople = people;
        int toFloor = targetFloor;
        for (PersonRequest request : personVector) {
            if (direction == 0) {
                direction = Integer.compare(request.getToFloor(), toFloor);
            }
            if (direction == 1) {
                if (request.getToFloor() > toFloor) {
                    toFloor = request.getToFloor();
                }
            } else if (direction == -1) {
                if (request.getToFloor() < toFloor) {
                    toFloor = request.getToFloor();
                }
            }
        }
        for (PersonRequest request : requestVector) {
            if (visualPeople == 6) {
                break;
            }
            if (direction == 0) {
                direction = Integer.compare(request.getFromFloor(), toFloor);
            }
            if (direction == 1) {
                if (request.getFromFloor() > toFloor) {
                    toFloor = request.getFromFloor();
                    visualPeople++;
                }
            } else if (direction == -1) {
                if (request.getFromFloor() < toFloor) {
                    toFloor = request.getFromFloor();
                    visualPeople++;
                }
            }
        }
        return toFloor;
    }

    public void changeState(String s) {
        this.state = s;
    }

    public synchronized int getCurFloor() {
        return curFloor;
    }

    public synchronized String getTheState() {
        return state;
    }

    public synchronized void setRequest(PersonRequest p) {
        requestVector.add(p);
        this.notifyAll();
        //重新获得锁之后先执行锁里面的内容才能进一步执行之后的内容
    }

    public synchronized int getPeople() {
        return people;
    }

    public synchronized void setEnd(boolean end) {
        this.isEnd = end;
        this.notifyAll();
    }

    public synchronized int getSumTime(PersonRequest p) {
        return Math.abs(targetFloor - p.getFromFloor());
    }

}
