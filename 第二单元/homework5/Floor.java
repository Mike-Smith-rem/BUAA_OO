import com.oocourse.elevator1.PersonRequest;

import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

public class Floor {
    private static final Map<Integer, Vector<PersonRequest>> FLOORS = new TreeMap<>();
    private static boolean isEnd = false;

    public Floor() {
        for (int i = 1; i <= 20; i++) {
            Vector<PersonRequest> requests = new Vector<>();
            FLOORS.put(i, requests);
        }
    }

    public synchronized void setIsEnd(boolean isEnd) {
        Floor.isEnd = isEnd;
        this.notifyAll();
    }

    public synchronized boolean getIsEnd() {
        return isEnd;
    }

    public synchronized Map<Integer, Vector<PersonRequest>> getFloors() {
        return FLOORS;
    }

    public synchronized void adds(PersonRequest request) {
        FLOORS.get(request.getFromFloor()).add(request);
        this.notifyAll();
    }

    public synchronized void removes(PersonRequest request) {
        FLOORS.get(request.getFromFloor()).remove(request);
    }

    public synchronized boolean isEmpty() {
        boolean ans = true;
        for (int i = 1; i <= 20; i++) {
            if (!FLOORS.get(i).isEmpty()) {
                ans = false;
                break;
            }
        }
        return ans;
    }

}
