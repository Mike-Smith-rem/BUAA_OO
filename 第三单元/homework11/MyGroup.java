import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;

import java.util.HashMap;
import java.util.Map;

public class MyGroup implements Group {
    private int id;
    private int sumOfAge = 0;
    private int sumOfAge2 = 0;
    private int sizeOfPerson = 0;
    private int valueSum = 0;
    private Map<Integer, Person> addPersons = new HashMap<>();
    private Map<Integer, Person> delPersons = new HashMap<>();
    private Map<Integer, Person> persons = new HashMap<>();

    public MyGroup(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MyGroup myGroup = (MyGroup) o;
        return id == myGroup.id;
    }

    @Override
    public void addPerson(Person person) {
        addValue(person);
        persons.put(person.getId(), person);
        sizeOfPerson++;
        sumOfAge += person.getAge();
        sumOfAge2 += person.getAge() * person.getAge();
    }

    private void addValue(Person p) {
        for (Integer i : persons.keySet()) {
            valueSum += 2 * p.queryValue(persons.get(i));
        }
    }

    @Override
    public boolean hasPerson(Person person) {
        return persons.containsKey(person.getId());
    }

    @Override
    public int getValueSum() {
        return valueSum;
    }

    @Override
    public int getAgeMean() {
        if (sizeOfPerson == 0) {
            return 0;
        }
        return sumOfAge / sizeOfPerson;
    }

    @Override
    public int getAgeVar() {
        if (sizeOfPerson == 0) {
            return 0;
        }
        return (sumOfAge2 + getAgeMean() * getAgeMean() * sizeOfPerson
                - 2 * sumOfAge * getAgeMean()) / sizeOfPerson;
    }

    public void addMoney(int money, int id) {
        for (Integer i : persons.keySet()) {
            Person p = persons.get(i);
            if (id != p.getId()) {
                p.addMoney(money);
            }
        }
    }

    @Override
    public void delPerson(Person person) {
        delValue(person);
        persons.remove(person.getId(), person);
        sizeOfPerson--;
        sumOfAge -= person.getAge();
        sumOfAge2 -= person.getAge() * person.getAge();
    }

    private void delValue(Person p) {
        for (Integer i : persons.keySet()) {
            valueSum -= 2 * p.queryValue(persons.get(i));
        }
    }

    public void addRelationNotify(Person p1, Person p2) {
        if (persons.containsKey(p1.getId())
                && persons.containsKey(p2.getId())) {
            valueSum += 2 * p1.queryValue(p2);
        }
    }

    @Override
    public int getSize() {
        return persons.size();
    }

    public void addSocialValue(int value) {
        for (Integer i : persons.keySet()) {
            Person p = persons.get(i);
            p.addSocialValue(value);
        }
    }
}
