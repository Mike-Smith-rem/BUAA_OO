import java.math.BigInteger;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Character.isDigit;

public class Triangle extends Factor implements Derivation {
    /**
     * type 用于定义sin 和 cos 类型
     */
    private String type;
    private String inside;
    /**
     * Triangle：简单定义类，不同于通过字符串判定类
     * @param symbols 符号
     * @param type 类型
     * @param exp 指数
     */
    public Triangle(String symbols,String type,String inside,BigInteger exp)
    {
        this.symbols = symbols;
        this.type = type;
        this.inside = inside;
        this.exp = exp;
    }

    /**
     * Triangle：通过输入字符串确定一个新的三角函数
     ** @param strt 传入字符
     */
    public Triangle(String strt) throws Exceptions {
        String str = strt;
        str = str.replaceAll("[ \\t]","");
        //Symbol 表示 因子的所带符号数
        //实际上从字符串传入的时候并没有带符号
        this.symbols = str.charAt(0) == '-' ? "-" : "";
        //Type 表示sin和cos
        if (str.charAt(0) == '+' || str.charAt(0) == '-') {
            this.type = str.startsWith("sin", 1) ? "sin" : "cos";
        }
        else {
            this.type = str.startsWith("sin") ? "sin" : "cos";
        }
        Pattern pattern = Pattern.compile("[+-]?\\d+$");
        Matcher matcher = pattern.matcher(str);
        //Exp 得到指数
        this.exp = BigInteger.valueOf(matcher.find() ? Long.parseLong(matcher.group()) : 1);
        if (exp.compareTo(BigInteger.valueOf(50)) > 0)
        {
            throw new Exceptions();
        }
        //inside得到里面的表达式,但是并不需要进行特判，只用知道里面是一坨东西
        pattern = Pattern.compile("(sin[(])|(cos[(])");
        matcher = pattern.matcher(str);
        int brackets = 1;
        int start = 0;
        if (matcher.find()) {
            start = matcher.end();
        }
        int end = start;
        for (int i = start; i < str.length();i++,end++)
        {
            if (str.charAt(i) == '(')brackets ++;
            else if (str.charAt(i) == ')')brackets --;
            if (brackets == 0)break;
        }
        str = str.substring(start,end);
        this.inside = str;
        if (inside.isEmpty() || inside.replaceAll("[)(]","").isEmpty())throw new Exceptions();
        //judge inside!
        if (inside.charAt(0) != '(')
        {
            StringBuilder strs = new StringBuilder(inside);
            //cos* sin*
            if (inside.startsWith("sin(") || inside.startsWith("cos("))//0c,1o,2s,3(,4
            {
                int bracket = 1;
                int endNumber = strs.length();
                for (int i = 4; i < strs.length(); i++)
                {
                    char tmp = strs.charAt(i);
                    if (tmp == '(') bracket++;
                    else if (tmp == ')') bracket--;
                    if (bracket == 0) {
                        endNumber = i + 1;
                        break;
                    }
                }
                Pattern pattern1 = Pattern.compile("^((&[+-]?\\d+)?[-+*])");
                Matcher matcher1 = pattern1.matcher(strs.substring(endNumber));
                if (matcher1.find()) throw new Exceptions();
            }
            else
            {
                //+2*
                String regex = "^[+-]?\\d+[-+*]";
                Pattern pattern1 = Pattern.compile(regex);
                Matcher matcher1 = pattern1.matcher(inside);
                if (matcher1.find()) throw new Exceptions();
                //-x,-sin,+cos
                regex = "^[+-](x|sin|cos)";
                pattern1 = Pattern.compile(regex);
                matcher1 = pattern1.matcher(inside);
                if (matcher1.find()) throw new Exceptions();
                //x*,x&2*
                regex = "^x(&[+-]?\\d+)?[-+*]";
                pattern1 = Pattern.compile(regex);
                matcher1 = pattern1.matcher(inside);
                if (matcher1.find()) throw new Exceptions();
            }
        }
        else if (inside.charAt(0) == '(')
        {
            int bracket = 1;
            int i = 1;
            for (; i < inside.length(); i++)
            {
                char tmp = inside.charAt(i);
                if (tmp == '(') bracket++;
                else if (tmp == ')') bracket--;
                if (bracket == 0) break;
            }
            if (i != inside.length() - 1) throw new Exceptions();
        }
    }

    /**
     * 链表类型（自返）
     * 用于得到三角函数的导数
     * 根据自底向上原则，因子的导数为项
     * @return 自反
     */
    public Factor setDerivation()
    {
        ArrayList<Factor> arrayList = new ArrayList<>();
        //指数复合的导数(triangle类型)
        Triangle triangleLeft = new Triangle(symbols,type,inside,exp.subtract(BigInteger.ONE));
        Triangle triangleCosine = new Triangle("","cos",inside,BigInteger.ONE);
        Triangle triangleSine = new Triangle("-","sin",inside,BigInteger.ONE);
        //创建新的项，存储在该类的Derivation中，作为该类的导数
        /*
         * 优化补充：这里判定下exp
         */
        if (exp.equals(BigInteger.ZERO)) {
            //添加数字0
            arrayList.add(new NormalNumber(BigInteger.ZERO,""));
        }
        else {
            //分三步：添加原本因子、导数因子、指数常量因子
            arrayList.add(triangleLeft);
            Triangle t = type.equals("sin") ? triangleCosine : triangleSine;
            arrayList.add(t);
            arrayList.add(new NormalNumber(exp,""));
        }
        //得到最后的导数项
        this.derivation = new Term(arrayList);
        //自返
        return this;
    }

    @Override
    public String toString() {
        String sign = symbols.equals("-") ? "-1*" : "";
        String formats = exp.equals(BigInteger.ZERO) ? "1" :
                         exp.equals(BigInteger.ONE) ? type + "(" + inside + ")" :
                                                      type + "(" + inside + ")**" + exp;
        return sign + formats;
    }
}
