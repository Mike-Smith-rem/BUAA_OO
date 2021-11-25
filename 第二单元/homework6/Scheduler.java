import com.oocourse.elevator2.PersonRequest;

import java.util.Vector;

public class Scheduler extends Thread {
    private static String type;
    private final Vector<Elevator> elevators = new Vector<>();
    private final Vector<PersonRequest> per = new Vector<>();
    private boolean isEnd = false;

    Scheduler(String type) {
        Scheduler.type = type;
        for (int i = 1; i <= 3; i++) {
            Elevator e = new Elevator(i,this);
            elevators.add(e);
        }
    }

    public synchronized void setEnd(boolean end) {
        isEnd = end;
        this.notifyAll();
    }

    public synchronized void addRequest(PersonRequest p) {
        per.add(p);
        this.notifyAll();
    }

    public synchronized void addElevator(int p) {
        Elevator elevator = new Elevator(p, this);
        elevators.add(elevator);
        elevator.start();
        //this.notifyAll();
    }

    @Override
    public void run() {
        synchronized (this) {
            for (Elevator elevator : elevators) {
                elevator.start();
            }
        }
        while (true) {
            synchronized (this) {
                if (per.isEmpty() && !isEnd) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            note();
            if (isEnd && per.isEmpty()) {
                for (Elevator elevator : elevators) {
                    elevator.setEnd(true);
                }
                return;
            }
        }
    }

    public void setElevator(Vector<PersonRequest> waitQueue) {
        for (int k = 0; k < waitQueue.size(); k++) {
            PersonRequest p = waitQueue.get(k);
            int i = availableElevatorIndex(p);
            if (i != -1) {
                elevators.get(i).setRequest(p);
                per.remove(p);
                k--;
                continue;
            }
            int j = takenableInFive(p);
            if (j != -1) {
                elevators.get(j).setRequest(p);
                per.remove(p);
                k--;
                continue;
            }
            int l = latestInFive(p);
            elevators.get(l).setRequest(p);
            per.remove(p);
            k--;
        }
    }

    public int availableElevatorIndex(PersonRequest p) {
        int i = -1;
        if ((i = availableInFive(p)) != -1)
        {
            return i;
        }
        return i;
    }

    public int availableInFive(PersonRequest p) {
        String state;
        int index = -1;
        int[] time = new int[]{21,21,21,21,21};
        for (int i = 0; i < elevators.size(); i++) {
            state = elevators.get(i).getTheState();
            if (state.equals("wait")) {
                time[i] = Math.abs(elevators.get(i).getCurFloor() -
                        p.getFromFloor());
            }
        }
        int fastest = 21;
        for (int i = 0; i < time.length; i++) {
            if (time[i] < fastest) {
                fastest = time[i];
                index = i;
            }
        }
        return index;
    }

    public int takenableInFive(PersonRequest p) {
        int[] time = new int[]{100,100,100,100,100};
        int requestDirection = Integer.compare(p.getToFloor(), p.getFromFloor());
        Elevator ele;
        int eleDirection;
        for (int i = 0; i < elevators.size(); i++) {
            ele = elevators.get(i);
            eleDirection = ele.getDirection();
            if (eleDirection != 0 && requestDirection != eleDirection
                    || (ele.getCurFloor() - p.getFromFloor()) * requestDirection > 0
                    || ele.getPeople() == 6) {
                continue;
            }
            else {
                time[i] = (p.getFromFloor() - ele.getCurFloor()) * requestDirection;
            }
        }
        int min = 40;
        int index = -1;
        for (int i = 0; i <= 4; i++) {
            index = min > time[i] ? i : index;
            min = Math.min(min, time[i]);
        }
        return index;
    }

    public int latestInFive(PersonRequest p) {
        int index = -1;
        int min = 2000;
        Elevator ele;
        for (int i = 0; i < elevators.size(); i++) {
            ele = elevators.get(i);
            if (ele.getSumTime(p) < min) {
                min = ele.getSumTime(p);
                index = i;
            }
        }
        return index;
    }

    public String getType() {
        return type;
    }

    public synchronized void note() {
        setElevator(per);
    }

}
