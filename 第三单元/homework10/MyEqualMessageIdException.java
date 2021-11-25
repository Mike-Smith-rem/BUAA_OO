import com.oocourse.spec2.exceptions.EqualMessageIdException;

import java.util.HashMap;
import java.util.Map;

public class MyEqualMessageIdException extends EqualMessageIdException {
    private static int totalTime = 0;
    private static Map<Integer, Integer> idTime = new HashMap<>();
    private int id;

    public MyEqualMessageIdException(int id) {
        totalTime++;
        this.id = id;
    }

    private void merge(int a) {
        if (idTime.containsKey(a)) {
            int value = idTime.get(a) + 1;
            idTime.remove(a);
            idTime.put(a, value);
        } else {
            idTime.put(a, 1);
        }
    }

    @Override
    public void print() {
        merge(id);
        System.out.printf("emi-%d, %d-%d%n",
                totalTime, id, idTime.get(id));
    }
}
