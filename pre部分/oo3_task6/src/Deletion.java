import java.util.ArrayList;
import java.util.Map;

public class Deletion {
    private final Map<String,ArrayList<Emails>> map = Email.getPlace();

    public Deletion(String username,String place)
    {
        try {
            ArrayList<Emails> emailsArrayList = map.get(place);
            Emails mail = null;
            for (Emails emails : emailsArrayList) {
                if (emails.getUsername().equals(username)) {
                    mail = emails;
                    break;
                }
            }
            map.get(place).remove(mail);
        } catch (NullPointerException ignored)
        {
        }
    }

    public Deletion(String all,String time,String place)
    {
        try {
            ArrayList<Emails> emailsArrayList = map.get(place);
            for (int i = 0;i < emailsArrayList.size();i++)
            {
                Emails emails = emailsArrayList.get(i);
                if (emails.getTime().equals(time))
                {
                    emailsArrayList.remove(emails);
                    i--;
                }
            }
            map.remove(place);
            map.put(place,emailsArrayList);
        } catch (NullPointerException ignored) {
        }
    }
}
