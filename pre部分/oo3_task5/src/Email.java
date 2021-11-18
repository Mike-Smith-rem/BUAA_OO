import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Email {
    private static final Map<String,Emails> INFORMATION = new HashMap<>();

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
                INFORMATION.put(emails.getUsername(),emails);
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
                new Factory(strs[0],strs[1]);
            }
        }
    }

    public static Map<String,Emails> getInformation()
    {
        return INFORMATION;
    }

}
