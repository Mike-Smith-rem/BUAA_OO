import com.oocourse.TimableOutput;

public class Main {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        InputThread inputThread = new InputThread();
        inputThread.setName("Thread Input");
        inputThread.start();
    }
}
