import com.oocourse.spec3.main.Runner;

public class Main {
    public static void main(String[] arg) throws Exception {
        Runner runner = new Runner(MyPerson.class, MyNetwork.class,
                MyGroup.class, MyMessage.class, MyEmojiMessage.class,
                MyNoticeMessage.class, MyRedEnvelopMessage.class);
        runner.run();
    }
}
