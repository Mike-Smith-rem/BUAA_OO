import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Email {
    private static final Map<String,ArrayList<Emails>> PLACE = new HashMap<>();

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();
        while (!str.equals("END_OF_INFORMATION"))
        {
            str = str.replace(',', ' ');
            str = str.trim();
            String cut = "\\s+";
            String[] strings = str.split(cut);
            for (String item : strings)
            {
                Emails emails = new Emails(item);
                int addTime = 0;
                for (String strs : PLACE.keySet())
                {
                    if (strs.equals(emails.getPlace()))
                    {
                        addTime = 1;
                        PLACE.get(strs).add(emails);
                    }
                }
                if (addTime == 0)
                {
                    ArrayList<Emails> mail = new ArrayList<Emails>();
                    mail.add(emails);
                    PLACE.put(emails.getPlace(),mail);
                }
            }
            str = scanner.nextLine();
        }
        while (scanner.hasNext()) {
            String[] strs = scanner.nextLine().split(" ");
            if (strs.length == 3)
            {
                new Factory(strs[0],strs[1],strs[2]);
            }
            else
            {
                new Factory(strs[0],strs[1],strs[2],strs[3]);
            }
        }
    }

    public static Map<String, ArrayList<Emails>> getPlace() {
        return PLACE;
    }
}
