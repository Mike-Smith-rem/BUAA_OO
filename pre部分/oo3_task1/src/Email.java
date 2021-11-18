import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Email {
    public static void main(String[] args)
    {
        ArrayList<String> information = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String str = scanner.nextLine();
            //将"，"全部修改为" "
            str = str.replace(',', ' ');
            //去除首尾" "
            str = str.trim();
            //得到字符串
            String cut = "\\s+";
            String[] strings = str.split(cut);
            for (String item : strings)
            {
                Emails emails = new Emails();
                information.add(emails.getAddress(item));
            }
        }
        information.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                String cut = "@";
                String compare1 = o1.split(cut)[0];
                String compare2 = o2.split(cut)[0];
                return compare1.compareTo(compare2);
            }
        });
        for (String item : information)
        {
            System.out.println(item);
        }
    }
}
