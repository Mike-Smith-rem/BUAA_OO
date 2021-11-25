import com.oocourse.spec1.exceptions.RelationNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class MyRelationNotFoundException extends RelationNotFoundException {
    private static int totalTime = 0;
    private static Map<Integer, Integer> idTime = new HashMap<>();
    private int id1;
    private int id2;

    public MyRelationNotFoundException(int id1, int id2) {
        totalTime++;
        this.id1 = Math.min(id1, id2);
        this.id2 = Math.max(id1, id2);
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
        merge(id1);
        if (id1 != id2) {
            merge(id2);
            System.out.printf("rnf-%d, %d-%d, %d-%d%n",
                    totalTime, id1, idTime.get(id1), id2, idTime.get(id2));
        } else {
            System.out.printf("rnf-%d, %d-%d%n",
                    totalTime, id1, idTime.get(id1));
        }
    }
}
