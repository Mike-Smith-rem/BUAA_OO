import com.oocourse.spec2.main.Runner;

public class Main {
    public static void main(String[] arg) throws Exception {
        Runner runner = new Runner(MyPerson.class, MyNetwork.class,
                MyGroup.class, MyMessage.class);
        runner.run();
    }
}
