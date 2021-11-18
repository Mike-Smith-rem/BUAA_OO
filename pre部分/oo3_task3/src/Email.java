import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Email {
    private static Map<String,Emails> information = new HashMap<>();

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();
        while (!str.equals("END_OF_INFORMATION"))
        {
            //将"，"全部修改为" "
            str = str.replace(',', ' ');
            //去除首尾" "
            str = str.trim();
            //得到字符串
            String cut = "\\s+";
            String[] strings = str.split(cut);
            for (String item : strings)
            {
                Emails emails = new Emails(item);
                information.put(emails.getUsername(),emails);
            }
            //System.out.println(str);
            str = scanner.nextLine();
        }
        while (scanner.hasNext()) {
            String type = scanner.next();
            String username = scanner.next();
            Search(type, username);
        }
    }

    public static void Search(String type, String username) {
        Emails s = information.get(username);
        if (s == null) {
            System.out.println("no username exists");
        } else {
            switch (type) {
                case "qdtype":
                    System.out.println(s.getTypeOfDomain());
                    break;
                case "qyear":
                    System.out.println(s.getYear());
                    break;
                case "qmonth":
                    System.out.println(s.getMonth());
                    break;
                case "qday":
                    System.out.println(s.getDay());
                    break;
                case "qhour":
                    System.out.println(s.getHour());
                    break;
                case "qminute":
                    System.out.println(s.getMinute());
                    break;
                case "qsec":
                    System.out.println(s.getSec());
                    break;
                default:
                    break;
            }
        }
    }
}
