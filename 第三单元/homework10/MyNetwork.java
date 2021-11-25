import com.oocourse.spec2.exceptions.EqualGroupIdException;
import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import com.oocourse.spec2.exceptions.GroupIdNotFoundException;
import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.exceptions.MessageIdNotFoundException;
import com.oocourse.spec2.exceptions.EqualMessageIdException;
import com.oocourse.spec2.exceptions.EqualRelationException;
import com.oocourse.spec2.exceptions.RelationNotFoundException;
import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Network;
import com.oocourse.spec2.main.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyNetwork implements Network {
    private Map<Integer, Person> personArrayList = new HashMap<>();
    private int sizeOfPeople = 0;
    private Map<Integer, Group> groups = new HashMap<>();
    private int sizeOfGroup = 0;
    private Map<Integer, Message> messages = new HashMap<>();
    private PreStatus preStatus = new PreStatus();
    private UnitSet unitSet = new UnitSet();

    private enum Ways { queryNameRank,  queryBlockSum }

    private enum Change { addPerson, addRelation }

    public MyNetwork() {
        Map<Integer, ArrayList<Integer>> map = new HashMap<>();
        ArrayList<Integer> array1 = new ArrayList<>();
        array1.add(Ways.queryNameRank.ordinal());
        array1.add(Ways.queryBlockSum.ordinal());
        map.put(Change.addPerson.ordinal(), array1);
        ArrayList<Integer> array2 = new ArrayList<>();
        array2.add(Ways.queryBlockSum.ordinal());
        map.put(Change.addRelation.ordinal(), array2);
        preStatus.init(map);
    }

    public void update(int changeId, Map map) {
        preStatus.setDirty(changeId);
        preStatus.setBuffer(changeId, map);
    }

    public void store(int wayId, Object map) {
        preStatus.recoverDirty(wayId);
        preStatus.recoverBuffer(wayId);
        preStatus.setElement(wayId, map);
    }

    @Override
    public boolean contains(int id) {
        return personArrayList.containsKey(id);
    }

    @Override
    public Person getPerson(int id) {
        return personArrayList.getOrDefault(id, null);
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        if (personArrayList.containsKey(person.getId())) {
            throw new MyEqualPersonIdException(person.getId());
        }
        personArrayList.put(person.getId(), person);
        sizeOfPeople++;
        int changeId = Change.addPerson.ordinal();
        Map<Integer, Person> p = new HashMap<>();
        p.put(person.getId(), person);
        update(changeId, p);
    }

    @Override
    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
        Person p1 = getPerson(id1);
        Person p2 = getPerson(id2);
        if (p1 == null) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (p2 == null) {
            throw new MyPersonIdNotFoundException(id2);
        }
        if (p1.isLinked(p2)) {
            throw new MyEqualRelationException(id1, id2);
        }
        if (p1 instanceof MyPerson && p2 instanceof MyPerson) {
            ((MyPerson) p1).getAcquaintance().put(p2.getId(), p2);
            ((MyPerson) p1).getValue().put(p2.getId(), value);
            ((MyPerson) p2).getAcquaintance().put(p1.getId(), p1);
            ((MyPerson) p2).getValue().put(p1.getId(), value);
        }
        ArrayList<Integer> relation = new ArrayList<>();
        relation.add(id1);
        relation.add(id2);
        int changeId = Change.addRelation.ordinal();
        Map<Integer, ArrayList<Integer>> map = new HashMap<>();
        map.put(Math.min(id1, id2), relation);
        update(changeId, map);
    }

    @Override
    public int queryValue(int id1, int id2)
            throws PersonIdNotFoundException, RelationNotFoundException {
        Person p1 = getPerson(id1);
        Person p2 = getPerson(id2);
        if (p1 == null) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (p2 == null) {
            throw new MyPersonIdNotFoundException(id2);
        }
        if (!p1.isLinked(p2)) {
            throw new MyRelationNotFoundException(id1, id2);
        }
        return p1.queryValue(p2);
    }

    @Override
    public int compareName(int id1, int id2) throws PersonIdNotFoundException {
        Person p1 = getPerson(id1);
        Person p2 = getPerson(id2);
        if (p1 == null) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (p2 == null) {
            throw new MyPersonIdNotFoundException(id2);
        }
        return p1.compareTo(p2);
    }

    @Override
    public int queryPeopleSum() {
        return sizeOfPeople;
    }

    @Override
    public int queryNameRank(int id) throws PersonIdNotFoundException {
        Person p = getPerson(id);
        if (p == null) {
            throw new MyPersonIdNotFoundException(id);
        }
        int answer = 1;
        /*int wayId = Ways.queryNameRank.ordinal();
        if (preStatus.getElement(wayId) != null) {
            Map<Integer, Integer> data = (Map<Integer, Integer>)
                    preStatus.getElement(wayId);
            if (!preStatus.getDirty(wayId) && data.containsKey(id)) {
                return data.get(id);
            }
        }*/
        for (Integer i : personArrayList.keySet()) {
            Person ps = personArrayList.get(i);
            answer += p.compareTo(ps) > 0 ? 1 : 0;
        }
        /*Map<Integer, Integer> newData = new HashMap<>();
        newData.put(id, answer);
        store(wayId, newData);*/
        return answer;
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        MyPerson p1 = (MyPerson) getPerson(id1);
        MyPerson p2 = (MyPerson) getPerson(id2);
        if (p1 == null) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (p2 == null) {
            throw new MyPersonIdNotFoundException(id2);
        }
        queryBlockSum();//for update
        return p1.isSameUnit(p2);
    }

    @Override
    public int queryBlockSum() {
        int wayId = Ways.queryBlockSum.ordinal();
        int changeId1 = Change.addPerson.ordinal();
        int changeId2 = Change.addRelation.ordinal();
        if (preStatus.getElement(wayId) != null) {
            Integer data = (Integer) preStatus.getElement(wayId);
            if (!preStatus.getDirty(changeId1) &&
                    !preStatus.getDirty(changeId2)) {
                return data;
            }
        }
        if (preStatus.getBuffer(wayId) == null) {
            return 0;
        }
        Map buffer = preStatus.getBuffer(wayId);
        for (Object i : buffer.keySet()) {
            if (buffer.get(i) instanceof Person) {
                unitSet.addPerson((Person) buffer.get(i));
            } else if (buffer.get(i) instanceof ArrayList) {
                ArrayList<Integer> relation = (ArrayList<Integer>) buffer.get(i);
                for (int s = 1; s < relation.size(); s++) {
                    MyPerson p1 = (MyPerson) getPerson(relation.get(s - 1));
                    MyPerson p2 = (MyPerson) getPerson(relation.get(s));
                    unitSet.countUnit(p1, p2);
                }
            }
        }
        Integer data = unitSet.countUnit();
        store(wayId, data);
        return data;
    }

    @Override
    public void addGroup(Group group) throws EqualGroupIdException {
        if (groups.containsKey(group.getId())) {
            throw new MyEqualGroupIdException(group.getId());
        }
        groups.put(group.getId(), group);
        sizeOfGroup++;
    }

    @Override
    public Group getGroup(int id) {
        return groups.get(id);
    }

    @Override
    public void addToGroup(int id1, int id2) throws GroupIdNotFoundException,
            PersonIdNotFoundException, EqualPersonIdException {
        Group group = getGroup(id2);
        Person person = getPerson(id1);
        if (group == null) {
            throw new MyGroupIdNotFoundException(id2);
        }
        if (person == null) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (group.hasPerson(person)) {
            throw new MyEqualPersonIdException(id1);
        }
        if (group.getSize() < 1111) {
            group.addPerson(person);
        }
    }

    @Override
    public int queryGroupSum() {
        return sizeOfGroup;
    }

    @Override
    public int queryGroupPeopleSum(int id) throws GroupIdNotFoundException {
        Group group = getGroup(id);
        if (group == null) {
            throw new MyGroupIdNotFoundException(id);
        }
        return group.getSize();
    }

    @Override
    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        Group group = getGroup(id);
        if (group == null) {
            throw new MyGroupIdNotFoundException(id);
        }
        return group.getValueSum();
    }

    @Override
    public int queryGroupAgeMean(int id) throws GroupIdNotFoundException {
        Group group = getGroup(id);
        if (group == null) {
            throw new MyGroupIdNotFoundException(id);
        }
        return group.getAgeMean();
    }

    @Override
    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        Group group = getGroup(id);
        if (group == null) {
            throw new MyGroupIdNotFoundException(id);
        }
        return group.getAgeVar();
    }

    @Override
    public void delFromGroup(int id1, int id2) throws GroupIdNotFoundException,
            PersonIdNotFoundException, EqualPersonIdException {
        Group group = getGroup(id2);
        Person person = getPerson(id1);
        if (group == null) {
            throw new MyGroupIdNotFoundException(id2);
        }
        if (person == null) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!group.hasPerson(person)) {
            throw new MyEqualPersonIdException(id1);
        }
        group.delPerson(person);
    }

    @Override
    public boolean containsMessage(int id) {
        return messages.containsKey(id);
    }

    @Override
    public void addMessage(Message message) throws EqualMessageIdException,
            EqualPersonIdException {
        if (messages.containsKey(message.getId())) {
            throw new MyEqualMessageIdException(message.getId());
        }
        if (message.getType() == 0 && message.getPerson1()
                .equals(message.getPerson2())) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        }
        messages.put(message.getId(), message);
    }

    @Override
    public Message getMessage(int id) {
        return messages.get(id);
    }

    @Override
    public void sendMessage(int id) throws RelationNotFoundException,
            MessageIdNotFoundException, PersonIdNotFoundException {
        Message message = getMessage(id);
        if (message == null) {
            throw new MyMessageIdNotFoundException(id);
        }
        if (message.getType() == 0) {
            Person person1 = message.getPerson1();
            Person person2 = message.getPerson2();
            if (!person1.isLinked(person2)) {
                throw new MyRelationNotFoundException(person1.getId(),
                        person2.getId());
            }
            int socialValue = message.getSocialValue();
            person1.addSocialValue(socialValue);
            person2.addSocialValue(socialValue);
            ((MyPerson) person2).insertFirst(message);
            messages.remove(message.getId(), message);
        }
        if (message.getType() == 1) {
            Person person = message.getPerson1();
            Group group = message.getGroup();
            if (!group.hasPerson(person)) {
                throw new MyPersonIdNotFoundException(person.getId());
            }
            int socialValue = message.getSocialValue();
            ((MyGroup) group).addSocialValue(socialValue);
            messages.remove(message.getId(), message);
        }
    }

    @Override
    public int querySocialValue(int id) throws PersonIdNotFoundException {
        Person person = getPerson(id);
        if (person == null) {
            throw new MyPersonIdNotFoundException(id);
        }
        return person.getSocialValue();
    }

    @Override
    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        Person person = getPerson(id);
        if (person == null) {
            throw new MyPersonIdNotFoundException(id);
        }
        return person.getReceivedMessages();
    }
}

