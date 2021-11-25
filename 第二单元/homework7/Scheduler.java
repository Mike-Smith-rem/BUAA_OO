import java.util.ArrayList;
import java.util.Vector;

public class Scheduler extends Thread {
    private final Vector<Person> requestQueue = new Vector<>();
    private final Vector<Person> transitQueue = new Vector<>();
    private final Vector<Person> tmpQueue = new Vector<>();
    private static String type;
    private final Vector<Elevator> elevatorAQueue = new Vector<>();
    private final Vector<Elevator> elevatorBQueue = new Vector<>();
    private final Vector<Elevator> elevatorCQueue = new Vector<>();
    private final ArrayList<int[]> atime = new ArrayList<>();
    private final ArrayList<int[]> btime = new ArrayList<>();
    private final ArrayList<int[]> ctime = new ArrayList<>();
    private boolean isEnd = false;

    private final int[] alist = new int[] {0,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
    private final int[] blist = new int[] {0,
        1, 0, 1, 0, 1, 0, 1, 0, 1, 0,
        1, 0, 1, 0, 1, 0, 1, 0, 1, 0};
    private final int[] clist = new int[] {0,
        1, 1, 1, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 1, 1, 1};

    Scheduler(String type) {
        Scheduler.type = type;
        elevatorAQueue.add(new Elevator(1,"A",this));
        elevatorBQueue.add(new Elevator(2,"B",this));
        elevatorCQueue.add(new Elevator(3,"C",this));
    }

    public synchronized void setEnd(boolean end) {
        isEnd = end;
        //System.out.println("END IS SET TO SCHEDULER");
        this.notifyAll();
    }

    public synchronized void setElevatorQueue(Elevator e) {
        switch (e.getType()) {
            case "A":
                synchronized (elevatorAQueue) {
                    elevatorAQueue.add(e);
                }
                break;
            case "B":
                synchronized (elevatorBQueue) {
                    elevatorBQueue.add(e);
                }
                break;
            case "C":
                synchronized (elevatorCQueue) {
                    elevatorCQueue.add(e);
                }
                break;
            default:
                break;
        }
        //System.out.println("Elevator-" + e.getType() + "-ADD" );
        e.start();
        //this.notifyAll();
    }

    public synchronized void setRequestQueue(Person p) {
        synchronized (requestQueue) {
            requestQueue.add(p);
        }
        this.notifyAll();
    }

    public synchronized void setTmpQueue(Person p) {
        synchronized (tmpQueue) {
            /*System.out.println(p.getId() + "-FROM-" +
                    p.getFromFloor() + "-TO-" +
                    p.getToFloor() + " ENTER AGAIN");*/
            tmpQueue.add(p);
        }
        synchronized (transitQueue) {
            if (!transitQueue.contains(p)) {
                System.out.println(p.getId() + "-FROM-" +
                        p.getFromFloor() + "-TO-" +
                        p.getToFloor() + " NOT EXSIT");

                System.exit(1);
            }
            transitQueue.remove(p);
        }
        this.notifyAll();
    }

    public synchronized String getType() {
        return type;
    }

    @Override
    public void run() {
        synchronized (this) {
            for (Elevator e : elevatorAQueue) {
                e.start();
            }
            for (Elevator e : elevatorBQueue) {
                e.start();
            }
            for (Elevator e : elevatorCQueue) {
                e.start();
            }
        }
        while (true) {
            synchronized (this) {
                if (requestQueue.isEmpty() && tmpQueue.isEmpty()
                        && !isEnd) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            allocElevator();
            synchronized (this) {
                if (isEnd && requestQueue.isEmpty()
                        && !transitQueue.isEmpty() && tmpQueue.isEmpty()) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (isEnd && requestQueue.isEmpty()
                        && transitQueue.isEmpty() && tmpQueue.isEmpty()) {
                    for (Elevator e : elevatorAQueue) {
                        e.setEnd(true);
                    }
                    for (Elevator e : elevatorBQueue) {
                        e.setEnd(true);
                    }
                    for (Elevator e : elevatorCQueue) {
                        e.setEnd(true);
                    }
                    return;
                }
            }
        }
    }

    public synchronized void allocElevator() {
        for (Elevator e : elevatorAQueue) {
            atime.add(e.getFloorTime());
        }
        for (Elevator e : elevatorBQueue) {
            btime.add(e.getFloorTime());
        }
        for (Elevator e : elevatorCQueue) {
            ctime.add(e.getFloorTime());
        }
        String s = evenlyAllocMethod();
        int enum1 = elevatorAQueue.size();
        int enum2 = elevatorBQueue.size();
        int enum3 = elevatorCQueue.size();
        elevatorSet(s, enum1, enum2, requestQueue);
        elevatorSet(s, enum1, enum2, tmpQueue);
        atime.clear();
        btime.clear();
        ctime.clear();
    }

    private void elevatorSet(String s, int enum1, int enum2, Vector<Person> tmpQueue) {
        for (int i = 0; i < tmpQueue.size(); i++) {
            Person p = tmpQueue.get(i);
            int index = getWaitElevator(p);
            if (index != -1
                &&
                ((index < enum2 + enum1 && index >= enum1 && blist[p.getFromFloor()] == 1)
                || (index >= enum1 + enum2 && clist[p.getFromFloor()] == 1)
                || (index < enum1))) {
                if (index < enum1) {
                    Elevator e = elevatorAQueue.get(index);
                    personAnalyse(p, e);
                    e.setRequest(p);
                }
                else if (index < enum1 + enum2) {
                    Elevator e = elevatorBQueue.get(index - enum1);
                    if (checkElevatorValid(p, e)) {
                        personAnalyse(p, e);
                        e.setRequest(p);
                    } else {
                        getNearestElevator(p, s);
                    }
                } else {
                    Elevator e = elevatorCQueue.get(index - enum1 - enum2);
                    if (checkElevatorValid(p, e)) {
                        personAnalyse(p, e);
                        e.setRequest(p);
                    } else {
                        getNearestElevator(p, s);
                    }
                }
            }
            else {
                getNearestElevator(p, s);
            }
            tmpQueue.remove(p);
            i--;
        }
    }

    private String evenlyAllocMethod() {
        int awaitlist = 0;
        int amax = 0;
        int bwaitlist = 0;
        int bmax = 0;
        int cwaitlist = 0;
        int cmax = 0;
        for (Elevator e : elevatorAQueue) {
            awaitlist += e.getOutside().size();
            amax += e.getMaxPeople();
        }
        for (Elevator e : elevatorBQueue) {
            bwaitlist += e.getOutside().size();
            bmax += e.getMaxPeople();
        }
        for (Elevator e : elevatorCQueue) {
            cwaitlist += e.getOutside().size();
            cmax += e.getMaxPeople();
        }
        if (amax <= awaitlist && bmax > bwaitlist) {
            return "B";
        } else if (amax <= awaitlist && cmax > cwaitlist) {
            return "C";
        } else {
            return "A";
        }
    }

    private void getNearestElevator(Person p, String s) {
        int floor = p.getFromFloor();
        int amin;
        int bmin;
        int cmin;
        int indexA;
        int indexB;
        int indexC;
        amin = bmin = cmin = 25000;
        indexA = indexB = indexC = -1;
        for (int[] time : atime) {
            if (time[floor] < amin) {
                amin = time[floor];
                indexA = atime.indexOf(time);
            } else if (time[floor] == amin) {
                Elevator e1 = elevatorAQueue.get(indexA);
                indexA = e1.getOutside().size() == e1.getMaxPeople() ? atime.indexOf(time)
                                                        : indexA;
            }
        }
        for (int[] time : btime) {
            if (time[floor] < bmin) {
                bmin = time[floor];
                indexB = btime.indexOf(time);
            } else if (time[floor] == bmin) {
                Elevator e1 = elevatorBQueue.get(indexB);
                indexB = e1.getOutside().size() == e1.getMaxPeople() ? btime.indexOf(time)
                                                        : indexB;
            }
        }
        for (int[] time : ctime) {
            if (time[floor] < cmin) {
                cmin = time[floor];
                indexC = ctime.indexOf(time);
            } else if (time[floor] == cmin) {
                Elevator e1 = elevatorCQueue.get(indexC);
                indexC = e1.getOutside().size() == e1.getMaxPeople() ? ctime.indexOf(time)
                                                        : indexC;
            }
        }
        Elevator finalElevator;
        if (indexA == -1 && indexB == -1 && indexC == -1) {
            System.out.println("index error");
            System.exit(1);
        }
        if (s.equals("A")) {
            if (amin <= bmin && amin <= cmin) {
                finalElevator = elevatorAQueue.get(indexA);
            } else if (bmin <= cmin && bmin <= amin) {
                finalElevator = elevatorBQueue.get(indexB);
            } else {
                finalElevator = elevatorCQueue.get(indexC);
            }
        }
        else if (s.equals("B")) {
            if (bmin <= amin && bmin <= cmin) {
                finalElevator = elevatorBQueue.get(indexB);
            } else if (cmin <= amin && cmin <= bmin) {
                finalElevator = elevatorCQueue.get(indexC);
            } else {
                finalElevator = elevatorAQueue.get(indexA);
            }
        }
        else {
            if (cmin <= amin && cmin <= bmin) {
                finalElevator = elevatorCQueue.get(indexC);
            } else if (amin <= bmin && amin <= cmin) {
                finalElevator = elevatorAQueue.get(indexA);
            } else {
                finalElevator = elevatorBQueue.get(indexB);
            }
        }
        if (!checkElevatorValid(p, finalElevator)) {
            finalElevator = elevatorAQueue.get(indexA);
        }
        personAnalyse(p, finalElevator);
        finalElevator.setRequest(p);
        //System.out.println(p.getId() + "-INTO-ELEVATOR");
    }

    private boolean checkElevatorValid(Person p, Elevator e) {
        if (e.getType().equals("A")) {
            return true;
        } else if (e.getType().equals("B")) {
            if (blist[p.getFromFloor()] == 1
                    && blist[p.getToFloor()] == 1) {
                return true;
            } else {
                return Math.abs(p.getToFloor() - p.getFromFloor()) > 1;
            }
        } else {
            if (clist[p.getFromFloor()] == 1
                    && clist[p.getToFloor()] == 1) {
                return true;
            } else {
                return (p.getFromFloor() != 3 || p.getToFloor() >= 18)
                        && (p.getFromFloor() != 18 || p.getToFloor() <= 3);
            }
        }
    }

    private void personAnalyse(Person p, Elevator e) {
        if (e.getType().equals("A")) {
            return;
        }
        else if (e.getType().equals("B")) {
            if (blist[p.getFromFloor()] == 1
                    && blist[p.getToFloor()] == 1) {
                return;
            } else {
                p.setStatus("NEED-TRANSIT");
                p.setToFloor(p.getToFloor() - 1);
                transitQueue.add(p);
            }
        }
        else if (e.getType().equals("C")) {
            if (clist[p.getFromFloor()] == 1
                    && clist[p.getToFloor()] == 1) {
                return;
            } else {
                p.setStatus("NEED-TRANSIT");
                if ((p.getToFloor() > 3 && p.getToFloor() <= 10)) {
                    p.setToFloor(3);
                } else if ((p.getToFloor() > 10 && p.getToFloor() < 18)) {
                    p.setToFloor(18);
                }
                transitQueue.add(p);
            }
        }
    }

    private int getWaitElevator(Person p) {
        String state;
        int index = -1;
        int[] time = new int[] {10000, 10000, 10000, 10000, 10000};
        int i = 0;
        for (;i < elevatorAQueue.size(); i++) {
            state = elevatorAQueue.get(i).getTheState();
            if (state.equals("wait")) {
                time[i] = Math.abs(elevatorAQueue.get(i).getCurFloor()
                        - p.getFromFloor()) * 600;
            }
        }
        int j = i;
        for (;i < elevatorBQueue.size() + j; i++) {
            state = elevatorBQueue.get(i - j).getTheState();
            if (state.equals("wait")) {
                time[i] = Math.abs(elevatorBQueue.get(i - j).getCurFloor()
                        - p.getFromFloor()) * 400;
            }
        }
        int k = i;
        for (;i < elevatorCQueue.size() + k; i++) {
            state = elevatorCQueue.get(i - k).getTheState();
            if (state.equals("wait")) {
                time[i] = Math.abs(elevatorCQueue.get(i - k).getCurFloor()
                        - p.getFromFloor()) * 200;
            }
        }
        int fastest = 10000;
        for (i = 0; i < time.length; i++) {
            if (time[i] < fastest) {
                fastest = time[i];
                index = i;
            }
        }
        return index;
    }

}
