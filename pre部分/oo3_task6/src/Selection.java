import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

public class Selection {
    private final Map<String, ArrayList<Emails>> map = Email.getPlace();

    public Selection(String username, String place) {
        try {
            ArrayList<Emails> emailsArrayList = map.get(place);
            if (emailsArrayList.size() == 0)
            {
                System.out.println("no place exists");
                return;
            }
            int find = 0;
            for (Emails emails : emailsArrayList) {
                if (emails.getUsername().equals(username)) {
                    System.out.println(emails.getTime());
                    find = 1;
                    break;
                }
            }
            if (find == 0) {
                System.out.println("no username exists");
            }
        } catch (NullPointerException e) {
            System.out.println("no place exists");
        }
    }

    public Selection(String all, String time, String place) {
        try {
            ArrayList<Emails> emailsArrayList = map.get(place);
            ArrayList<Emails> result = new ArrayList<>();
            if (emailsArrayList.size() == 0)
            {
                System.out.println("no place exists");
                return;
            }
            for (Emails emails : emailsArrayList) {
                if (emails.getTime().equals(time)) {
                    result.add(emails);
                }
            }
            if (result.size() == 0) {
                System.out.println("no email exists");
            } else {
                result.sort(new Comparator<Emails>() {
                    @Override
                    public int compare(Emails o1, Emails o2) {
                        return o1.getUsername().compareTo(o2.getUsername());
                    }
                });
                for (Emails emails : result) {
                    System.out.println(emails.getAddress());
                }
            }
        } catch (NullPointerException e)
        {
            System.out.println("no place exists");
        }
    }
}
