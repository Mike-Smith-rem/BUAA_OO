
public class Emails {
    private String date = "";
    private String typeOfDomain = "";
    private String year = null;
    private String month = null;
    private String day = null;
    private String hour = null;
    private String minute = null;
    private String sec = null;
    private String domain = "";
    private String username = "";
    private String originUsername = "";

    public Emails(String str)
    {
        String cut = "@";
        String[] strings = str.split(cut);
        originUsername = strings[0];
        username = strings[0].toLowerCase();
        cut = "-";
        String[] strings1 = strings[1].split(cut);
        domain = strings1[0];
        for (String item : strings1)
        {
            if (!item.equals(strings1[0]))
            {
                date = date + "-" + item;
            }
        }
        date = date.substring(1);
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

    public String getOriginUsername()
    {
        return originUsername;
    }

    public String getTypeOfDomain()
    {
        String cut = "\\.";
        typeOfDomain = domain.split(cut)[0];
        if (typeOfDomain.isEmpty())
        {
            typeOfDomain = null;
        }
        return typeOfDomain;
    }

    public String getYear()
    {
        String cut = "-";
        year = date.split(cut)[0];
        return year;
    }

    public String getMonth()
    {
        String cut = "-";
        month = date.split(cut)[1];
        return month;
    }

    public String getDay()
    {
        String cut = "-";
        day = date.split(cut)[2];
        return day;
    }

    public String getHour()
    {
        String cut = ":";
        if (date.split(cut)[0].equals(date))
        {
            if (date.split("-").length == 4)
            {
                hour = date.split("-")[3];
                return hour;
            }
            else {
                return null;
            }
        }
        else
        {
            int length = date.split(cut)[0].length();
            hour = date.substring(length - 2,length);
            return hour;
        }
    }

    public String getMinute()
    {
        String string = getHour();
        if (string != null)
        {
            String cut = ":";
            if (date.split(cut).length > 1) {
                minute = date.split(cut)[1];
                return minute;
            }
            return null;
        }
        return null;
    }

    public String getSec()
    {
        String cut = ":";
        if (date.split(cut).length == 3)
        {
            sec = date.split(cut)[2];
            return date.split(cut)[2];
        }
        return null;
    }
}
