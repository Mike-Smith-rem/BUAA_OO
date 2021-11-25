import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.main.Network;
import com.oocourse.spec1.main.Person;

import java.util.ArrayList;

public class MyNetwork implements Network {
    private ArrayList<Person> personArrayList = new ArrayList<>();
    private boolean ans = false;
    private ArrayList<Integer> flags = new ArrayList<>();

    //@ ensures \result == (\exists int i; 0 <= i && i < people.length; people[i].getId() == id);
    @Override
    public boolean contains(int id) {
        for (Person p : personArrayList) {
            if (p.getId() == id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Person getPerson(int id) {
        for (Person p : personArrayList) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        for (Person p : personArrayList) {
            if (p.equals(person)) {
                throw new MyEqualPersonIdException(p.getId());
            }
        }
        personArrayList.add(person);
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
            ((MyPerson) p1).getAcquaintance().add(p2);
            ((MyPerson) p1).getValue().add(value);
            ((MyPerson) p2).getAcquaintance().add(p1);
            ((MyPerson) p2).getValue().add(value);
        }
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
        return personArrayList.size();
    }

    @Override
    public int queryNameRank(int id) throws PersonIdNotFoundException {
        Person p = getPerson(id);
        if (p == null) {
            throw new MyPersonIdNotFoundException(id);
        }
        int answer = 1;
        for (Person ps : personArrayList) {
            answer += p.compareTo(ps) > 0 ? 1 : 0;
        }
        return answer;
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        Person p1 = getPerson(id1);
        Person p2 = getPerson(id2);
        if (p1 == null) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (p2 == null) {
            throw new MyPersonIdNotFoundException(id2);
        }
        if (p1 instanceof MyPerson && p2 instanceof MyPerson) {
            circuit((MyPerson) p1, (MyPerson)p2);
            flags.clear();
        }
        if (ans) {
            ans = false;
            return true;
        } else {
            return false;
        }
    }

    private void circuit(MyPerson p1, MyPerson p2) {
        if (!flags.contains(p1.getId())) {
            flags.add(p1.getId());
        } else {
            return;
        }
        if (p1.isLinked(p2) || ans) {
            ans = true;
            return;
        }
        ArrayList<Person> people = p1.getAcquaintance();
        for (Person p : people) {
            if (p instanceof MyPerson) {
                circuit((MyPerson) p, p2);
            }
        }
    }

    @Override
    public int queryBlockSum() {
        int answer = 0;
        int flag;
        for (int i = 0; i < personArrayList.size(); i++) {
            flag = 0;
            for (int j = 0; j < i; j++) {
                try {
                    if (isCircle(personArrayList.get(i).getId(),
                            personArrayList.get(j).getId())) {
                        flag = 1;
                    }
                } catch (PersonIdNotFoundException e) {
                    e.print();
                }
            }
            if (flag == 0) {
                answer++;
            }
        }
        return answer;
    }
}
