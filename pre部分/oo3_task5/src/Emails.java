
public class Emails {
    private String date = "";
    private String domain = "";
    private String username = "";
    private String time = "";

    public Emails(String str)
    {
        String cut = "@";
        String[] strings = str.split(cut);
        username = strings[0].toLowerCase();
        cut = "-";
        String[] strings1 = strings[1].split(cut);
        domain = strings1[0];
        int addTime = 0;
        for (String item : strings1)
        {
            if (!item.equals(strings1[0]))
            {
                date = date + "-" + item;
                if (addTime < 3)
                {
                    time = date;
                    addTime++;
                }
            }
        }
        time = time.substring(1);
        date = date.substring(1);
    }

    public String getAddress()
    {
        return username + "@" + domain + " " + date;
    }

    public String getDate()
    {
        return date;
    }

    public String getDomain() {
        return domain;
    }

    public String getUsername() {
        return username;
    }

    public String getTime()
    {
        return time;
    }

}
