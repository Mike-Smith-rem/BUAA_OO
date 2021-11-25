import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.lang.Character.isDigit;

public class MainClass {
    private static final Map<String,Factor> FACTORS = new HashMap<>();

    public static void main(String[] args) throws Exceptions {
        Scanner in = new Scanner(System.in);
        try {
            String polys = in.nextLine();
            //先分割表达式成为标准的表达式树的形式
            boolean w1 = formatCheck1(polys);
            if (!w1) {
                throw new Exceptions();
            } else {
                polys = polys.replaceAll("[ \\t]", "");
                polys = polys.replaceAll("\\+\\+\\+|--\\+|-\\+-|\\+--", "+");
                polys = polys.replaceAll("---|-\\+\\+|\\+-\\+|\\+\\+-", "-");
                polys = polys.replaceAll("\\+\\+|--", "+");
                polys = polys.replaceAll("\\+-|-\\+", "-");
            }
            polys = segments(polys);
            //System.out.println(polys);
            boolean w2 = formatCheck2(polys);
            if (!w2) {
                throw new Exceptions();
            }
            ArrayList<String> strings = new BuildPostFix(polys).getExp();
            Tree tree = buildTree(strings);
            String ans = tree.setDerivation().toString();
            ans = ans.replaceAll("&","**");
            System.out.println(ans);
        } catch (Exceptions e)
        {
            e.Reason();
        }
    }

    /**
     * 总而言之，将三角函数变成Tn类型，将幂函数变成Pn类型，常数不变？
     * @param poly
     * @return
     */
    public static String segments(String poly) throws Exceptions {
        //优化表达式
        String str = poly;
        if (str.isEmpty())
        {
            throw new Exceptions();
        }
        str = str.replaceAll("\\*\\*","&");
        String power = "x(&[+-]?\\d+)?";
        //先判断三角函数
        StringBuilder strs = new StringBuilder(str);
        Pattern pattern = Pattern.compile("(sin|cos)[(]");
        Matcher matcher = pattern.matcher(strs);
        int indexOfT = 0;
        while (matcher.find())
        {
            indexOfT++;
            //Search
            int bracket = 1;
            int endNumber = strs.length();
            for (int i = matcher.end();i < strs.length();i++)
            {
                char tmp = strs.charAt(i);
                if (tmp == '(')bracket++;
                else if (tmp == ')')bracket--;
                if (bracket == 0)
                {
                    endNumber = i + 1;
                    break;
                }
            }
            boolean findPower = false;
            boolean isNoneSymbol = true;
            boolean isOneSymbol = false;
            boolean findNumber = false;
            int endPower = endNumber;
            for (int i = endNumber; i < strs.length();i++,endPower++)
            {
                boolean b = strs.charAt(i) == '\t' || strs.charAt(i) == ' ';
                if (!findPower)
                {
                    if (b)
                    {
                        continue;
                    }
                    else  if (strs.charAt(i) == '&')
                    {
                        findPower = true;
                        continue;
                    }
                    //already find other chars
                    else break;
                }
                if (findPower && (isNoneSymbol | isOneSymbol) && !findNumber)
                {
                    if (b)
                    {
                        if (isNoneSymbol)continue;
                    }
                    else if (strs.charAt(i) == '+' || strs.charAt(i) == '-')
                    {
                        isNoneSymbol = false;
                        isOneSymbol = !isOneSymbol;
                        continue;
                    }
                    else if (isDigit(strs.charAt(i)))
                    {
                        findNumber = true;
                        continue;
                    }
                    else break;;
                }
                if (findNumber)
                {
                    if (isDigit(strs.charAt(i)))
                    {
                        continue;
                    }
                    else break;
                }
            }
            FACTORS.put("T" + indexOfT, new Triangle(strs.substring(matcher.start(),endPower)));
            strs.delete(endNumber, endPower);
            strs.replace(matcher.start(), matcher.end() - 1, "t");
            pattern = Pattern.compile("(sin|cos)[(]");
            matcher = pattern.matcher(strs);
            //System.out.println(strs);
        }
        str = strs.toString();
        //再判断幂函数
        pattern = Pattern.compile(power);
        matcher = pattern.matcher(str);
        int indexOfP = 0;
        while (matcher.find()) {
            indexOfP++;
            FACTORS.put("P" + indexOfP, new Power(matcher.group()));
        }
        str = str.replaceAll(power,"p");
        //最后是常数
        //str = str.replaceAll("&\\s*","&");
        String normalNumber = "\\d+|(?<=\\*)[+-]\\d+";
        pattern = Pattern.compile(normalNumber);
        matcher = pattern.matcher(str);
        int indexOfN = 0;
        while (matcher.find()) {
            indexOfN++;
            FACTORS.put("N" + indexOfN, new NormalNumber(matcher.group()));
        }
        str = str.replaceAll(normalNumber,"n");

        //二次替换
        int number = 0;
        pattern = Pattern.compile("n");
        matcher = pattern.matcher(str);
        while (matcher.find()) {
            number++;
            str = str.replaceFirst("n","N" + number);
        }
        number = 0;
        pattern = Pattern.compile("p");
        matcher = pattern.matcher(str);
        while (matcher.find()) {
            number++;
            str = str.replaceFirst("p","P" + number);
        }
        number = 0;
        pattern = Pattern.compile("t");
        matcher = pattern.matcher(str);
        while (matcher.find()) {
            number++;
            str = str.replaceFirst("t","T" + number + "/");
        }

        //最后添加一个判断：对于负数变成0-数的形式
        str = str.replaceAll("(?<!(\\d|[)]))-","N0-");
        FACTORS.put("N0",new NormalNumber("0"));
        str = str.replaceAll("(?<!(\\d|[)]))\\+","");
        return str;
    }

    public static Tree buildTree(ArrayList<String> string) {
        Stack<Node> stack = new Stack<>();
        for (String value : string) {
            if (!value.equals("*") && !value.equals("-") && !value.equals("+") && !value.equals("/")) {
                Factor factor = FACTORS.get(value);
                Node node = new Node(factor);
                stack.push(node);
            } else {
                Node node = new Node(value);
                Node right = stack.isEmpty() ? new Node("0") : stack.pop();
                Node left = stack.isEmpty() ? new Node("0") : stack.pop();
                node.setLeft(left);
                node.setRight(right);
                stack.push(node);
            }
        }
        return new Tree(stack.pop());
    }

    public static boolean formatCheck1(String str)
    {
        //stage1
        //space check and * * check and sin/cos check
        String regex = "[\\f\\n\\r\\v]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        if (matcher.find())return false;

        regex = "s\\s+in|si\\s+n|c\\s+os|co\\s+s";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(str);
        if (matcher.find())return false;

        regex = "\\*\\s+\\*";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(str);
        if (matcher.find())return false;

        //---x,---sinx,---cosx
        regex = "[+-]\\s*[+-]\\s*[+-]\\s*[xsc]";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(str);
        if (matcher.find())return false;

        //--- 4
        regex = "[+-]\\s*[+-]\\s*[+-]\\s+\\d";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(str);
        if (matcher.find()) return false;

        //too many symbols
        regex = "[+-]\\s*[+-]\\s*[+-]\\s*[+-]";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(str);
        if (matcher.find()) return false;

        //x*- 2,x**+ 3 sin(  - 3)  cos ( -  3)
        regex = "(\\*|(sin\\s*[(])|(cos\\s*[(]))\\s*[+-]\\s+\\d";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(str);
        if (matcher.find()) return false;

        String s = str;
        s = s.replaceAll("[-)+*(]","");
        s = s.replaceAll("sin|cos|x","");
        s = s.replaceAll("\\d","");
        s = s.replaceAll("[ \t]","");
        if (!s.isEmpty()) return false;

        return true;
    }

    public static boolean formatCheck2(String str)
    {
        //2x,xx,xsin(x),sin(x)sin(x),x(...),2(...),sin(x)(...),(..)(..)
        String regex = "([\\d)][PTN(])";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        if (matcher.find())return false;

        //(x+1)^2  won't be matched
        String s = new String(str);
        regex = "[PNT]\\d+";
        s = s.replaceAll(regex,"");
        regex = "[-+*/)(]";
        s = s.replaceAll(regex,"");
        if (!s.isEmpty()) return false;

        //sin(2*x),sin(3+sin(..)),sin(sin(..)+sin(..)),sin(0-sin())
        return true;
    }
}
