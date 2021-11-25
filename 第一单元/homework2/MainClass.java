package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainClass {
    private static final Map<String,Factor> FACTORS = new HashMap<>();

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String polys = in.nextLine();
        //先分割表达式成为标准的表达式树的形式
        polys = segments(polys);
        ArrayList<String> strings = new BuildPostFix(polys).getExp();
        Tree tree = buildTree(strings);
        System.out.println(tree.setDerivation().toString());
    }

    /**
     * 总而言之，将三角函数变成Tn类型，将幂函数变成Pn类型，常数不变？
     * @param poly
     * @return
     */
    public static String segments(String poly) {
        //优化表达式
        String str = poly;
        str = str.replaceAll("\\s+", "");
        str = str.replaceAll("(\\+-\\+)|(-\\+\\+)|(\\+\\+-)","-");
        str = str.replaceAll("(--\\+)|(-\\+-)|(\\+--)","+");
        str = str.replaceAll("(--)|(\\+\\+)","+");
        str = str.replaceAll("(\\+-)|(-\\+)","-");
        String power = "x(\\*\\*[+-]?\\d+)?";
        String triangle = "(sin|cos)[(]x[)](\\*\\*[+-]?\\d+)?";
        //String normalNumber = "\\d+";
        //先判断三角函数
        Pattern pattern = Pattern.compile(triangle);
        Matcher matcher = pattern.matcher(str);
        int indexOfT = 0;
        while (matcher.find()) {
            indexOfT++;
            FACTORS.put("T" + indexOfT, new Triangle(matcher.group()));
        }
        str = str.replaceAll(triangle,"t");
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
        String normalNumber = "(\\d+)|((?<=\\*)-\\d+)";
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
            str = str.replaceFirst("t","T" + number);
        }
        //最后添加一个判断：对于负数变成0-数的形式
        str = str.replaceAll("(?<!((\\d)|[)]))-","N0-");
        FACTORS.put("N0",new NormalNumber("0"));
        str = str.replaceAll("(?<!((\\d)|[)]))\\+","");
        return str;
    }

    public static Tree buildTree(ArrayList<String> string) {
        Stack<Node> stack = new Stack<>();
        for (String value : string) {
            if (!value.equals("*") && !value.equals("-") && !value.equals("+")) {
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
}
