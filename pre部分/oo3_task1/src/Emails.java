
public class Emails {
    private String address = "";
    private String time = "";
    private String domain = "";
    private String username = "";

    public String getAddress(String str)
    {
        String cut = "@";
        String[] strings = str.split(cut);
        username = strings[0].toLowerCase();
        cut = "-";
        String[] strings1 = strings[1].split(cut);
        domain = strings1[0];
        strings1 = strings1;
        for (String item : strings1)
        {
            if (!item.equals(strings1[0]))
            {
                address = address + "-" + item;
            }
        }
        address = address.substring(1);
        return username + "@" + domain + " " + address;
    }

    public String getAddress()
    {
        return address;
    }

    public String getTime()
    {
        return time;
    }

    public String getDomain() {
        return domain;
    }

    public String getUsername() {
        return username;
    }
}
