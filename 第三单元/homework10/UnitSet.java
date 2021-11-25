import com.oocourse.spec2.main.Person;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UnitSet {
    private Map<Integer, Map<Integer, Person>> unit = new ConcurrentHashMap<>();
    private Map<Integer, Person> tmpArray = new LinkedHashMap<>();
    private Map<Integer, Person> deleteId = new HashMap<>();
    private int sizeOfUnit = 0;

    public void countUnit(MyPerson d1, MyPerson d2) {
        int id1 = d1.getFatherId();
        int id2 = d2.getFatherId();
        if (unit.containsKey(id1) || unit.containsKey(id2)) {
            if (unit.containsKey(id1) && unit.containsKey(id2)) {
                if (id1 != id2) {
                    merge(id1, id2);
                }
            }
            else if (unit.containsKey(id1)) {
                d2.setFatherId(id1);
                Map<Integer, Person> map = new ConcurrentHashMap<>();
                map.put(id2, d2);
                unit.get(id1).putAll(map);
            }
            else if (unit.containsKey(id2)) {
                d1.setFatherId(id2);
                Map<Integer, Person> map = new ConcurrentHashMap<>();
                map.put(id1, d1);
                unit.get(id2).putAll(map);
            }
        }
        else {
            int idMin = Math.min(id1, id2);
            Map<Integer, Person> map = new ConcurrentHashMap<>();
            map.put(id1, d1);
            map.put(id2, d2);
            d1.setFatherId(idMin);
            d2.setFatherId(idMin);
            unit.put(idMin, map);
            sizeOfUnit++;
        }
        removePerson(d1);
        removePerson(d2);
    }

    public int countUnit() {
        for (Integer id : deleteId.keySet()) {
            if (tmpArray.containsKey(id)) {
                tmpArray.remove(id, tmpArray.get(id));
            }
        }
        deleteId.clear();
        for (Integer id : tmpArray.keySet()) {
            if (!unit.containsKey(id)) {
                Map<Integer, Person> newUnit = new HashMap<>();
                newUnit.put(id, tmpArray.get(id));
                unit.put(id, newUnit);
                sizeOfUnit++;
            }
        }
        tmpArray.clear();
        return sizeOfUnit;
    }

    private void merge(int id1, int id2) {
        int idMin = Math.min(id1, id2);
        int idMax = Math.max(id1, id2);
        Map<Integer, Person> p1 = unit.get(idMin);
        Map<Integer, Person> p2 = unit.get(idMax);
        p1.putAll(p2);
        for (Integer i : p2.keySet()) {
            MyPerson p = (MyPerson) p2.get(i);
            p.setFatherId(idMin);
        }
        unit.remove(idMax, p2);
        sizeOfUnit--;
    }

    public void addPerson(Person p) {
        tmpArray.put(p.getId(), p);
    }

    public void removePerson(Person p) {
        deleteId.put(p.getId(), p);
    }
}

