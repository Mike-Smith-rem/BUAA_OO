import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Judge {
    private static String Atype = "a{2,3}b{2,4}a{2,4}c{2,3}";
    private static String Btype = "a{2,3}(ba){0,100000000}(bc){2,4}";
    private static String Ctype = Btype;
    private static String Dtype1 = "^a{0,3}b{1,1000000}c{2,3}[A-Za-z-]*";
    private static String Dtype2 = "[A-Za-z-]]*b{1,2}a{1,2}c{0,3}$";
    private static String EEtype = "[A-Za-z-]*a+[A-Za-z-]*b+[A-Za-z-]*b+[A-Za-z-]*c+[A-Za-z-]*b+[A-Za-z-]*c+[A-Za-z-]*c+";

    public static String judge(String str)
    {
        int k = 0;
        Pattern[] patterns = new Pattern[10];
        StringBuilder chars = new StringBuilder();
        patterns[0] = Pattern.compile(Atype);
        patterns[1] = Pattern.compile(Btype);
        patterns[2] = Pattern.compile(Ctype);
        patterns[3] = Pattern.compile(Dtype1);
        patterns[4] = Pattern.compile(Dtype2);
        patterns[5] = Pattern.compile(EEtype);
        for (int i = 0;i < 6;i++)
        {
            Matcher m;
            m = patterns[i].matcher(str);
            if (i == 2)
            {
                m = patterns[i].matcher(str.toLowerCase());
            }
            if (i == 3)
            {
                Matcher m1 = patterns[i].matcher(str);
                Matcher m2 = patterns[i + 1].matcher(str.toLowerCase());
                if (m1.find() && m2.find())
                {
                    k++;
                    chars.append(swichNumber(i));
                }
                i++;
                continue;
            }
            if (m.find())
            {
                k++;
                chars.append(swichNumber(i));
            }
        }
        if (k != 0) {
            return Integer.toString(k) + " " + chars.toString();
        }
        else {
            return  Integer.toString(k);
        }
    }

    public static String swichNumber(int number)
    {
        switch (number)
        {
            case 0:
                return "A";
            case 1:
                return "B";
            case 2:
                return "C";
            case 3: case 4:
                return "D";
            case 5:
                return "E";
            default:
                return null;
        }
    }

}
