import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Person;

public class MyMessage implements Message {
    private int id;
    private int socialValue;
    private int type;
    private Person person1;
    private Person person2;
    private Group group;

    public MyMessage(int messageId, int messageSocialValue,
                     Person messagePerson1, Person messagePerson2) {
        this.type = 0;
        this.group = null;
        this.id = messageId;
        this.socialValue = messageSocialValue;
        this.person1 = messagePerson1;
        this.person2 = messagePerson2;
    }

    public MyMessage(int messageId, int messageSocialValue,
                     Person messagePerson1, Group messageGroup) {
        this.type = 1;
        this.person2 = null;
        this.id = messageId;
        this.socialValue = messageSocialValue;
        this.person1 = messagePerson1;
        this.group = messageGroup;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getSocialValue() {
        return socialValue;
    }

    @Override
    public Person getPerson1() {
        return person1;
    }

    @Override
    public Person getPerson2() {
        try {
            assert person2 != null;
        } catch (AssertionError e) {
            System.out.println("person2 is null !");
        }
        return person2;
    }

    @Override
    public Group getGroup() {
        try {
            assert group != null;
        } catch (AssertionError e) {
            System.out.println("group is null !");
        }
        return group;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Message) {
            MyMessage myMessage = (MyMessage) o;
            return id == myMessage.id;
        }
        return false;
    }
}
