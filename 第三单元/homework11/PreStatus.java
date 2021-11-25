import com.oocourse.spec3.main.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PreStatus {
    private Map<Integer, Boolean> dirty = new HashMap<>();
    private Map<Integer, Object> data = new HashMap<>();
    private Map<Integer, Map> buffer = new HashMap<>();
    private Map<Integer, ArrayList<Integer>> map = new HashMap<>();

    public void init(Map<Integer, ArrayList<Integer>> map) {
        this.map = map;
        for (Map.Entry<Integer, ArrayList<Integer>> item : map.entrySet()) {
            ArrayList<Integer> mapTo = item.getValue();
            for (Integer i : mapTo) {
                dirty.put(i, false);
                data.put(i, null);
                buffer.put(i, new HashMap());
            }
        }
    }

    public Object getElement(int wayId) {
        return data.get(wayId);
    }

    public void setElement(int wayId, Object o) {
        if (o instanceof Map) {
            Map<Integer, Integer> item = (Map<Integer, Integer>) o;
            if (data.get(wayId) == null) {
                data.put(wayId, item);
            } else {
                ((Map<Integer, Integer>) data.get(wayId)).putAll(item);
            }
        } else if (o instanceof Integer) {
            Integer item = (Integer) o;
            data.put(wayId, item);
        }
    }

    public boolean getDirty(int wayId) {
        return dirty.get(wayId);
    }

    public void setDirty(int changeId) {
        ArrayList<Integer> mapTo = map.get(changeId);
        for (Integer i : mapTo) {
            dirty.put(i, true);
        }
    }

    public void recoverDirty(int wayId) {
        dirty.put(wayId, false);
    }

    public void setBuffer(int changeId, Map map) {
        ArrayList<Integer> mapTo = this.map.get(changeId);
        for (Integer i : mapTo) {
            Map bufferMap = buffer.get(i);
            for (Object j : map.keySet()) {
                if (map.get(j) instanceof ArrayList) {
                    if (bufferMap.containsKey(j) && bufferMap.get(j) instanceof Person) {
                        bufferMap.put(j, map.get(j));
                    } else if (bufferMap.containsKey(j)
                            && bufferMap.get(j) instanceof ArrayList) {
                        ((ArrayList<Integer>) bufferMap.get(j)).addAll(
                                (ArrayList<Integer>)map.get(j));
                    } else if (!bufferMap.containsKey(j)) {
                        bufferMap.put(j, map.get(j));
                    }
                } else if (map.get(j) instanceof Person) {
                    bufferMap.put(j, map.get(j));
                }
            }
        }
    }

    public void recoverBuffer(int wayId) {
        buffer.get(wayId).clear();
    }

    public Map getBuffer(int wayId) {
        return buffer.get(wayId);
    }
}
