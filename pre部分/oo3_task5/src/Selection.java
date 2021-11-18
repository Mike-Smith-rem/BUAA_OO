import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

public class Selection {
    private final Map<String, Emails> map = Email.getInformation();
    private ArrayList<Emails> result = new ArrayList<>();

    public Selection(String username)
    {
        try {
            Emails emails = map.get(username);
            System.out.println(emails.getTime());
        } catch (NullPointerException e)
        {
            System.out.println("no username exists");
        }
    }

    public Selection(String all,String time)
    {
        for (String str : map.keySet())
        {
            Emails emails = map.get(str);
            if (emails.getTime().equals(time))
            {
                result.add(emails);
            }
        }
        if (result.size() == 0)
        {
            System.out.println("no email exists");
        }
        else
        {
            result.sort(new Comparator<Emails>() {
                @Override
                public int compare(Emails o1, Emails o2) {
                    return o1.getUsername().compareTo(o2.getUsername());
                }
            });
            for (Emails emails : result)
            {
                System.out.println(emails.getAddress());
            }
        }
    }
}
