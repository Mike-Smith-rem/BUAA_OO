import com.oocourse.spec1.main.Person;

import java.util.ArrayList;
import java.util.Objects;

public class MyPerson implements Person {
    private int id;
    private String name;
    private int age;
    private ArrayList<Person> acquaintance = new ArrayList<>();
    private ArrayList<Integer> value = new ArrayList<>();

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public ArrayList<Person> getAcquaintance() {
        return acquaintance;
    }

    public ArrayList<Integer> getValue() {
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
    public int hashCode() {
        return Objects.hash(id, name, age);
    }

    @Override
    public boolean isLinked(Person person) {
        if (id == person.getId()) {
            return true;
        }
        for (Person p : acquaintance) {
            if (p.getId() == person.getId()) {
                return true;
            }
        }
        return false;
    }

    //ensures \result == name.compareTo(p2.getName());
    @Override
    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }

    @Override
    public int queryValue(Person person) {
        int result = 0;
        for (int i = 0; i < acquaintance.size(); i++) {
            Person p = acquaintance.get(i);
            if (p.getId() == person.getId()) {
                result = value.get(i);
                break;
            }
        }
        return result;
    }

}
