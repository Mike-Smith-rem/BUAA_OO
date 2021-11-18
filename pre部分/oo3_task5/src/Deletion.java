import java.util.ArrayList;
import java.util.Map;

public class Deletion {
    private final Map<String,Emails> map = Email.getInformation();

    public Deletion(String username)
    {
        try {
            Emails emails = map.get(username);
        } catch (NullPointerException e)
        {
            System.out.println("no");
        }
        map.remove(username);
    }

    public Deletion(String all,String time)
    {
        ArrayList<String> strings = new ArrayList<>();
        for (String str : map.keySet())
        {
            if (map.get(str).getTime().equals(time))
            {
                strings.add(str);
            }
        }
        for (String str : strings)
        {
            map.remove(str);
        }
    }
}
