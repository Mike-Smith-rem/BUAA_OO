import com.oocourse.uml3.interact.AppRunner;

public class Main {
    public static void main(String[] args) throws NoSuchMethodException {
        AppRunner appRunner = AppRunner.newInstance(MyUmlGeneralInteraction.class);
        appRunner.run(args);
    }
}
