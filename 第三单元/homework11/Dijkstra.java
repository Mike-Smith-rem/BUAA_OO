import com.oocourse.spec3.main.Person;

import java.util.HashMap;
import java.util.Map;

public class Dijkstra {
    private Map<Integer, Person> personArrayList = new HashMap<>();
    //first is the id, the second is the value
    private Map<Integer, Integer> distance = new HashMap<>();
    private Map<Integer, Integer> bestDistance = new HashMap<>();
    //first is the id, the second is the sign
    private Map<Integer, Integer> flag = new HashMap<>();
    //first is the id, the second is previous
    //private Map<Integer, Integer> pre = new HashMap<>();

    public Dijkstra(Map<Integer, Person> personArrayList) {
        for (Integer i : personArrayList.keySet()) {
            Person p = personArrayList.get(i);
            distance.put(p.getId(), Integer.MAX_VALUE);
            this.personArrayList.put(p.getId(), p);
            this.flag.put(p.getId(), 0);
        }
    }

    public void init(int id1) {
        distance.put(id1, 0);
    }

    public int run(int fromId, int targetId) {
        init(fromId);
        int id = putBest();
        while (!bestDistance.containsKey(targetId)) {
            seek(id);
            id = putBest();
        }
        return bestDistance.get(targetId);
    }

    public int putBest() {
        int min = Integer.MAX_VALUE;
        int id = -1;
        for (Integer p : distance.keySet()) {
            if (distance.get(p) < min && !bestDistance.containsKey(p)) {
                id = p;
                min = distance.get(p);
            }
        }
        //找到最小节点加入并标记
        bestDistance.put(id, min);
        flag.put(id, 1);
        return id;
    }

    //计算刚刚加入节点的邻近节点的距离
    public void seek(int id1) {
        Person p1 = personArrayList.get(id1);
        Map<Integer, Person> ps = ((MyPerson) p1).getAcquaintance();
        for (Integer i : ps.keySet()) {
            Person p = ps.get(i);
            if (flag.get(p.getId()) != 1) {
                getValue(id1, p.getId());
            }
        }
    }

    public void getValue(int id1, int id2) {
        Person p2 = personArrayList.get(id2);
        int value = distance.get(id1) + p2.queryValue(personArrayList.get(id1));
        if (value < distance.get(id2)) {
            distance.put(id2, value);
        }
    }
}
