import com.oocourse.spec3.exceptions.*;
import com.oocourse.spec3.main.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;

public class MyNetwork implements Network {
    private Map<Integer, Person> personArrayList = new HashMap<>();
    private int sizeOfPerson = 0;
    private Map<Integer, Group> groups = new HashMap<>();
    private int sizeOfGroup = 0;
    private Map<Integer, ArrayList<Integer>> personEmojiMap = new HashMap<>();
    private Map<Integer, EmojiMessage> emojiFinalMessages = new HashMap<>();
    private Map<Integer, Integer> emojiHeatMessages = new HashMap<>();
    private Map<Integer, RedEnvelopeMessage> envelopeMessages = new HashMap<>();
    private Map<Integer, NoticeMessage> noticeMessages = new HashMap<>();
    private Map<Integer, Message> normalMessages = new HashMap<>();
    private Map<Integer, String> mapMessage = new HashMap<>();
    private PreStatus preStatus = new PreStatus();
    private UnitSet unitSet = new UnitSet();

    enum Ways {
        queryNameRank, queryBlockSum,
    }

    enum Change {
        addPerson, addRelation,
    }

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

    public boolean contains(int id) {
        return personArrayList.containsKey(id);
    }

    public Person getPerson(int id) {
        return personArrayList.getOrDefault(id, null);
    }

    public void addPerson(Person person) throws EqualPersonIdException {
        if (personArrayList.containsKey(person.getId())) {
            throw new MyEqualPersonIdException(person.getId());
        }
        personArrayList.put(person.getId(), person);
        sizeOfPerson++;
        int changeId = Change.addPerson.ordinal();
        Map<Integer, Person> p = new HashMap<>();
        p.put(person.getId(), person);
        update(changeId, p);
    }

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
            ((MyPerson) p1).getAcquaintance().put(id2, p2);
            ((MyPerson) p1).getValue().put(id2, value);
            ((MyPerson) p2).getAcquaintance().put(id1, p1);
            ((MyPerson) p2).getValue().put(id1, value);
            Map<Integer, Integer> groupId1 = ((MyPerson) p1).getGroupId();
            Map<Integer, Integer> groupId2 = ((MyPerson) p2).getGroupId();
            for (Integer i : groupId1.keySet()) {
                if (groupId2.containsKey(i)) {
                    ((MyGroup)getGroup(i)).addRelationNotify(p1, p2);
                }
            }
        }
        ArrayList<Integer> relation = new ArrayList<>();
        relation.add(id1);
        relation.add(id2);
        int changeId = Change.addRelation.ordinal();
        Map<Integer, ArrayList<Integer>> map = new HashMap<>();
        map.put(Math.min(id1, id2), relation);
        update(changeId, map);
    }

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

    public int queryPeopleSum() {
        return sizeOfPerson;
    }

    public int queryNameRank(int id) throws PersonIdNotFoundException {
        Person p = getPerson(id);
        if (p == null) {
            throw new MyPersonIdNotFoundException(id);
        }
        int answer = 1;
        for (Integer i : personArrayList.keySet()) {
            Person ps = personArrayList.get(i);
            answer += p.compareTo(ps) > 0 ? 1 : 0;
        }
        return answer;
    }

    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        MyPerson p1 = (MyPerson) getPerson(id1);
        MyPerson p2 = (MyPerson) getPerson(id2);
        if (p1 == null) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (p2 == null) {
            throw new MyPersonIdNotFoundException(id2);
        }
        queryBlockSum();
        return p1.isSameUnit(p2);
    }

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

    public void addGroup(Group group) throws EqualGroupIdException {
        if (groups.containsKey(group.getId())) {
            throw new MyEqualGroupIdException(group.getId());
        }
        groups.put(group.getId(), group);
        sizeOfGroup++;
    }

    public Group getGroup(int id) {
        return groups.get(id);
    }

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
        ((MyPerson) person).addToGroup(id2);
    }

    public int queryGroupSum() {
        return sizeOfGroup;
    }

    public int queryGroupPeopleSum(int id) throws GroupIdNotFoundException {
        Group group = getGroup(id);
        if (group == null) {
            throw new MyGroupIdNotFoundException(id);
        }
        return group.getSize();
    }

    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        Group group = getGroup(id);
        if (group == null) {
            throw new MyGroupIdNotFoundException(id);
        }
        return group.getValueSum();
    }

    public int queryGroupAgeMean(int id) throws GroupIdNotFoundException {
        Group group = getGroup(id);
        if (group == null) {
            throw new MyGroupIdNotFoundException(id);
        }
        return group.getAgeMean();
    }

    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        Group group = getGroup(id);
        if (group == null) {
            throw new MyGroupIdNotFoundException(id);
        }
        return group.getAgeVar();
    }

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
        ((MyPerson) person).delFromGroup(id2);
    }

    public boolean containsMessage(int id) {
        return mapMessage.containsKey(id);
    }

    public void addMessage(Message message) throws EqualMessageIdException,
            EmojiIdNotFoundException, EqualPersonIdException {
        if (containsMessage(message.getId())) {
            throw new MyEqualMessageIdException(message.getId());
        }
        if (message instanceof EmojiMessage
                && !containsEmojiId(((EmojiMessage) message).getEmojiId())) {
            throw new MyEmojiIdNotFoundException(((EmojiMessage) message).getEmojiId());
        }
        if (message.getType() == 0 && message.getPerson1().
                equals(message.getPerson2())) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        }
        if (message instanceof EmojiMessage) {
            emojiFinalMessages.put(message.getId(), (EmojiMessage) message);
            personEmojiMap.get(((EmojiMessage) message).getEmojiId()).
                    add(message.getId());
            mapMessage.put(message.getId(), "Emoji");
        } else if (message instanceof NoticeMessage) {
            noticeMessages.put(message.getId(), (NoticeMessage) message);
            mapMessage.put(message.getId(), "Notice");
        } else if (message instanceof RedEnvelopeMessage) {
            envelopeMessages.put(message.getId(), (RedEnvelopeMessage) message);
            mapMessage.put(message.getId(), "Envelop");
        } else {
            normalMessages.put(message.getId(), message);
            mapMessage.put(message.getId(), "Normal");
        }
    }

    public Message getMessage(int id) {
        if (mapMessage.containsKey(id)) {
            switch (mapMessage.get(id)) {
                case "Emoji" :
                    return emojiFinalMessages.get(id);
                case "Notice" :
                    return noticeMessages.get(id);
                case "Envelop" :
                    return envelopeMessages.get(id);
                case "Normal" :
                    return normalMessages.get(id);
                default:
                    break;
            }
        }
        return null;
    }

    private void getRemove(int id) {
        String type = mapMessage.get(id);
        mapMessage.remove(id);
        if (type.equals("Emoji")) {
            personEmojiMap.get(emojiFinalMessages.get(id).getEmojiId()).
                    remove((Integer) id);
            emojiFinalMessages.remove(id);
        } else if (type.equals("Notice")) {
            noticeMessages.remove(id);
        } else if (type.equals("Envelop")) {
            envelopeMessages.remove(id);
        } else if (type.equals("Normal")) {
            normalMessages.remove(id);
        }
    }

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
            send(message, person1, person2);
        }
        if (message.getType() == 1) {
            Person person = message.getPerson1();
            Group group = message.getGroup();
            if (!group.hasPerson(person)) {
                throw new MyPersonIdNotFoundException(person.getId());
            }
            int socialValue = message.getSocialValue();
            ((MyGroup) group).addSocialValue(socialValue);
            if (message instanceof RedEnvelopeMessage) {
                int arvMoney = ((RedEnvelopeMessage) message).getMoney()
                        / (group.getSize());
                person.addMoney(-arvMoney * (group.getSize() - 1));
                ((MyGroup) group).addMoney(arvMoney, person.getId());
            } else if (message instanceof EmojiMessage) {
                int index = ((EmojiMessage) message).getEmojiId();
                emojiHeatMessages.put(index, emojiHeatMessages.get(index) + 1);
            }
            getRemove(message.getId());
        }
    }

    private void send(Message message, Person person1, Person person2) {
        int socialValue = message.getSocialValue();
        person1.addSocialValue(socialValue);
        person2.addSocialValue(socialValue);
        ((MyPerson) person2).insertFirst(message);
        if (message instanceof RedEnvelopeMessage) {
            person1.addMoney(-((RedEnvelopeMessage) message).getMoney());
            person2.addMoney(((RedEnvelopeMessage) message).getMoney());
        } else if (message instanceof EmojiMessage) {
            int index = ((EmojiMessage) message).getEmojiId();
            emojiHeatMessages.put(index, emojiHeatMessages.get(index) + 1);
        }
        getRemove(message.getId());
    }

    public int querySocialValue(int id) throws PersonIdNotFoundException {
        Person person = getPerson(id);
        if (person == null) {
            throw new MyPersonIdNotFoundException(id);
        }
        return person.getSocialValue();
    }

    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        Person person = getPerson(id);
        if (person == null) {
            throw new MyPersonIdNotFoundException(id);
        }
        return person.getReceivedMessages();
    }

    public boolean containsEmojiId(int id) {
        return emojiHeatMessages.containsKey(id);
    }

    public void storeEmojiId(int id) throws EqualEmojiIdException {
        if (emojiHeatMessages.containsKey(id)) {
            throw new MyEqualEmojiIdException(id);
        }
        personEmojiMap.put(id, new ArrayList<>());
        emojiHeatMessages.put(id, 0);
    }

    public int queryMoney(int id) throws PersonIdNotFoundException {
        if (getPerson(id) == null) {
            throw new MyPersonIdNotFoundException(id);
        }
        return getPerson(id).getMoney();
    }

    public int queryPopularity(int id) throws EmojiIdNotFoundException {
        if (!emojiHeatMessages.containsKey(id)) {
            throw new MyEmojiIdNotFoundException(id);
        }
        return emojiHeatMessages.get(id);
    }

    public int deleteColdEmoji(int limit) {
        Iterator<Map.Entry<Integer, Integer>> iterator =
                emojiHeatMessages.entrySet().iterator();
        ArrayList<Integer> emojiIds = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Integer> item = iterator.next();
            if (item.getValue() < limit) {
                emojiIds.add(item.getKey());
                iterator.remove();
            }
        }
        for (Integer emoji : emojiIds) {
            ArrayList<Integer> personIds = personEmojiMap.get(emoji);
            for (Integer i : personIds) {
                mapMessage.remove(i);
                emojiFinalMessages.remove(i);
            }
            personEmojiMap.remove(emoji);
        }
        return emojiHeatMessages.size();
    }

    public int sendIndirectMessage(int id) throws MessageIdNotFoundException {
        if (!containsMessage(id) || getMessage(id).getType() == 1) {
            throw new MyMessageIdNotFoundException(id);
        }
        Message message = getMessage(id);
        Person p1 = message.getPerson1();
        Person p2 = message.getPerson2();
        try {
            if (!isCircle(p1.getId(), p2.getId())) {
                return -1;
            }
        } catch (PersonIdNotFoundException e) {
            e.print();
        }
        send(message, p1, p2);
        Map<Integer, Person> set = unitSet.getUnit(((MyPerson)p1).getFatherId());
        Dijkstra dijkstra = new Dijkstra(set);
        return dijkstra.run(p1.getId(), p2.getId());
    }
}

