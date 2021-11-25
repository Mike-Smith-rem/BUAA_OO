import com.oocourse.TimableOutput;

public class Main {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        Floor floor = new Floor();
        Input input = new Input(floor);
        input.setName("input THread");
        input.start();
    }
}

