import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Person;

import java.util.HashMap;
import java.util.Map;

public class MyGroup implements Group {
    private int id;
    private int sumOfAge = 0;
    private int sumOfAge2 = 0;
    private int sizeOfPerson = 0;
    private int valueSum = 0;
    private int valueSumOfPeople = 0;
    private int flag = 0;
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
        persons.put(person.getId(), person);
        sizeOfPerson++;
        sumOfAge += person.getAge();
        sumOfAge2 += person.getAge() * person.getAge();
        flag = 1;
    }

    @Override
    public boolean hasPerson(Person person) {
        return persons.containsKey(person.getId());
    }

    @Override
    public int getValueSum() {
        int ans = 0;
        //if (valueSumOfPeople != sizeOfPerson || flag == 1) {
        for (Integer k : persons.keySet()) {
            Person p1 = persons.get(k);
            for (Integer j : persons.keySet()) {
                Person p2 = persons.get(j);
                if (p1.isLinked(p2)) {
                    ans += p1.queryValue(p2);
                }
            }
        }
        //valueSum = ans;
        //valueSumOfPeople = sizeOfPerson;
        //flag = 0;
        return ans;
        //} else {
        //    return valueSum;
        //}
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

    @Override
    public void delPerson(Person person) {
        persons.remove(person.getId(), person);
        sizeOfPerson--;
        sumOfAge -= person.getAge();
        sumOfAge2 -= person.getAge() * person.getAge();
        flag = 1;
    }

    @Override
    public int getSize() {
        return sizeOfPerson;
    }

    public void addSocialValue(int value) {
        for (Integer i : persons.keySet()) {
            Person p = persons.get(i);
            p.addSocialValue(value);
        }
    }
}
