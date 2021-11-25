import com.oocourse.TimableOutput;

import java.util.Vector;

public class Elevator extends Thread {
    private int id;
    private String type;
    private int maxPeople;
    private long speed;
    private int[] list;
    private final Vector<Person> outside = new Vector<>();
    private final Vector<Person> inside = new Vector<>();
    private int curFloor = 1;
    private int targetFloor = 1;
    private int direction = 0;
    private int people = 0;
    private boolean isEnd = false;
    private Scheduler scheduler;
    private String state;

    Elevator(int id, String type, Scheduler scheduler) {
        this.id = id;
        this.type = type;
        switch (type) {
            case "A":
                maxPeople = 8;
                speed = 600;
                list = new int[] {0,
                    1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                    1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
                break;
            case "B":
                maxPeople = 6;
                speed = 400;
                list = new int[] {0,
                    1, 0, 1, 0, 1, 0, 1, 0, 1, 0,
                    1, 0, 1, 0, 1, 0, 1, 0, 1, 0};
                break;
            case "C":
                maxPeople = 4;
                speed = 200;
                list = new int[] {0,
                    1, 1, 1, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 1, 1, 1};
                break;
            default:
                break;
        }
        this.scheduler = scheduler;
        this.state = "wait";
    }

    public synchronized void setRequest(Person p) {
        synchronized (outside) {
            //System.out.println(p.getId() + "-ADD");
            outside.add(p);
        }
        this.state = "work";
        this.notifyAll();
    }

    public synchronized void setEnd(boolean end) {
        this.isEnd = end;
        this.notifyAll();
    }

    public int setTargetFloor() {
        int visualPeople = people;
        int toFloor = targetFloor;
        synchronized (inside) {
            for (Person request : inside) {
                if (direction == 0) {
                    direction = Integer.compare(request.getToFloor(),
                            toFloor);
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
        }
        synchronized (outside) {
            for (Person request : outside) {
                if (visualPeople == maxPeople) {
                    break;
                }
                if (direction == 0) {
                    direction = Integer.compare(request.getFromFloor(),
                            toFloor);
                }
                if (direction == 1) {
                    if (request.getFromFloor() > toFloor) {
                        toFloor = request.getFromFloor();
                        visualPeople++;
                    } else {
                        visualPeople = request.getFromFloor() > curFloor ?
                                visualPeople + 1 : visualPeople;
                    }
                } else if (direction == -1) {
                    if (request.getFromFloor() < toFloor) {
                        toFloor = request.getFromFloor();
                        visualPeople++;
                    } else {
                        visualPeople = request.getFromFloor() < curFloor ?
                                visualPeople + 1 : visualPeople;
                    }
                }
            }
        }
        return toFloor;
    }

    public void move() {
        try {
            sleep(speed);
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
        synchronized (outside) {
            for (Person request : outside) {
                if (curFloor == request.getFromFloor() && people < maxPeople) {
                    if (direction == 0) {
                        direction = Integer.compare(request.getToFloor(),
                                curFloor);
                    }
                    if (direction != 0 && Integer.compare(request.getToFloor(),
                            curFloor) == direction) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public boolean isOut() {
        synchronized (inside) {
            for (Person request : inside) {
                if (curFloor == request.getToFloor()) {
                    return true;
                }
            }
            return false;
        }
    }

    public void in() {
        try {
            sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (outside) {
            for (int i = 0; i < outside.size(); i++) {
                Person p = outside.get(i);
                if (curFloor == p.getFromFloor() && people < maxPeople
                        && direction == Integer.compare(p.getToFloor(), curFloor)) {
                    TimableOutput.println("IN-" + p.getId() + "-" + curFloor
                            + "-" + id);
                    people++;
                    outside.remove(p);
                    inside.add(p);
                    i--;
                }
            }
        }
    }

    public void out() {
        try {
            sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (inside) {
            for (int i = 0; i < inside.size(); i++) {
                Person p = inside.get(i);
                if (curFloor == p.getToFloor()) {
                    TimableOutput.println("OUT-" + p.getId() + "-"
                            + curFloor + "-" + id);
                    people--;
                    inside.remove(p);
                    i--;
                    if (p.getStatus().equals("NEED-TRANSIT")) {
                        p.setStatus("NO-NEED-TRANSIT");
                        p.setFromFloor(p.getToFloor());
                        p.setToFloor(p.getOriginToFloor());
                        scheduler.setTmpQueue(p);
                    }
                }
            }
        }
    }

    public synchronized int getMaxPeople() {
        return maxPeople;
    }

    public synchronized int getCurFloor() {
        return curFloor;
    }

    public synchronized int getPeople() {
        return people;
    }

    public synchronized String getType() {
        return type;
    }

    public synchronized String getTheState() {
        return state;
    }

    public synchronized Vector<Person> getOutside() {
        return outside;
    }

    public synchronized int[] getFloorTime() {
        int[] time = new int[]{30000,
            30000, 30000, 30000, 30000, 30000,
            30000, 30000, 30000, 30000, 30000,
            30000, 30000, 30000, 30000, 30000,
            30000, 30000, 30000, 30000, 30000};

        if (direction == 0) {
            for (int i = 1; i < time.length; i++) {
                time[i] = (list[i] == 1) ? Math.abs((i - curFloor)) * (int) speed
                                            : time[i];
            }
        }
        else if (direction == 1) {
            for (int i = 1; i < time.length; i++) {
                switch (Integer.compare(i, curFloor)) {
                    case 0:
                        time[i] = (list[i] == 1) ? 0
                                            : time[i];
                        break;
                    case 1:
                        time[i] = (list[i] == 1) ? Math.abs(i - curFloor) * (int) speed
                                            : time[i];
                        break;
                    case -1:
                        time[i] = (list[i] == 1) ? (20 + Math.abs(i - curFloor)) *
                                            (int) speed
                                            : time[i];
                        break;
                    default:
                        break;
                }
            }
        }
        else if (direction == -1) {
            for (int i = 1; i < time.length; i++) {
                switch (Integer.compare(i, curFloor)) {
                    case 0:
                        time[i] = (list[i] == 1) ? 0
                                            : time[i];
                        break;
                    case 1:
                        time[i] = (list[i] == 1) ? (20 + Math.abs(i - curFloor)) *
                                            (int) speed
                                            : time[i];
                        break;
                    case -1:
                        time[i] = (list[i] == 1) ? Math.abs(i - curFloor) * (int) speed
                                            : time[i];
                        break;
                    default:
                        break;
                }
            }
        }
        return time;
    }

    public synchronized boolean isEmpty() {
        return (outside.isEmpty() && inside.isEmpty());
    }

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                while (outside.isEmpty() && inside.isEmpty() && !isEnd) {
                    this.state = "wait";
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (isEnd && outside.isEmpty() && inside.isEmpty()) {
                    return;
                }
                this.state = "work";
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
            } while (!isEmpty());
        }
    }

}
