import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Emails {
    private String date = "";
    private String domain = "";
    private String username = "";
    private String time = "";
    private String place = "";

    public Emails(String str)
    {
        //str = 01:00:03-02-12-2020-abc@buaa.edu.cn-wuhu
        String cut = "@";
        String[] stringsTwo = str.split(cut);
        //strTwo[0] = 01:00:03-02-12-2020-abc
        //strTwo[1] = buaa.edu.cn-wuhu
        domain = stringsTwo[1].split("-")[0];
        place = stringsTwo[1].split("-")[1];
        //domain = buaa.edu.cn       place = wuhu
        date = setNumber(stringsTwo[0]);
        username = setUsername(stringsTwo[0]);
    }

    public String setNumber(String str)
    {
        String cut = "\\d+";
        Pattern pattern = Pattern.compile(cut);
        Matcher matcher = pattern.matcher(str);
        StringBuilder stringTemp = new StringBuilder();
        StringBuilder stringDate = new StringBuilder();
        StringBuilder stringTime = new StringBuilder();
        while (matcher.find())
        {
            stringTemp.append(matcher.group())
                    .append(" ");
        }
        String[] strings = stringTemp.toString().split(" ");
        for (int i = 0;i < 3; i++)
        {
            stringDate.append(strings[strings.length - 1 - i]).append("-");
        }
        stringDate.deleteCharAt(stringDate.length() - 1);
        time = stringDate.toString();
        for (int i = 3;i < strings.length;i++)
        {
            stringTime.append(strings[strings.length - 1 - i]).append(":");
        }
        if (strings.length != 3) {
            stringTime.deleteCharAt(stringTime.length() - 1);
            return  stringDate.toString() + "-" + stringTime.toString();
        }
        return  stringDate.toString();
    }

    public String setUsername(String str)
    {
        String strs = str.replaceAll(":","-");
        strs = strs.replaceAll("\\d+-","");
        return strs.toLowerCase();
    }

    public String getTime()
    {
        return time;
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

    public String getPlace() {
        return place;
    }

}
