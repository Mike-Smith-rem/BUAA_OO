import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyPerson implements Person {
    private int id;
    private String name;
    private int age;
    private Map<Integer, Person> acquaintance = new HashMap<>();
    private Map<Integer, Integer> value = new HashMap<>();
    private int socialValue;
    private List<Message> messages = new ArrayList<>();
    private int fatherId;

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.socialValue = 0;
        this.fatherId = id;
    }

    public Map<Integer, Person> getAcquaintance() {
        return acquaintance;
    }

    public Map<Integer, Integer> getValue() {
        return value;
    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Person) {
            MyPerson myPerson = (MyPerson) o;
            return id == myPerson.id;
        }
        return false;
    }

    @Override
    public boolean isLinked(Person person) {
        if (id == person.getId()) {
            return true;
        }
        return acquaintance.containsKey(person.getId());
    }

    @Override
    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }

    @Override
    public void addSocialValue(int num) {
        socialValue += num;
    }

    @Override
    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public int getSocialValue() {
        return socialValue;
    }

    public void insertFirst(Message message) {
        ArrayList<Message> messages1 = new ArrayList<>();
        messages1.add(message);
        messages1.addAll(messages);
        messages.clear();
        messages.addAll(messages1);
    }

    public void setFatherId(int id) {
        this.fatherId = id;
    }

    public int getFatherId() {
        return fatherId;
    }

    public boolean isSameUnit(Person p) {
        return fatherId == ((MyPerson) p).getFatherId();
    }

    @Override
    public List<Message> getReceivedMessages() {
        int length = Math.min(messages.size(), 4);
        List<Message> tmp = new ArrayList<Message>();
        for (int i = 0; i < length; i++) {
            tmp.add(messages.get(i));
        }
        return tmp;
    }

    @Override
    public int queryValue(Person person) {
        return value.getOrDefault(person.getId(), 0);
    }
}

