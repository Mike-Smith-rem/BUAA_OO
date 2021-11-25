package src;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Triangle extends Factor implements Derivation {
    //sin 的导数(triangle类型)
    private static final Triangle TRIANGLESINE = new Triangle("-","sin",BigInteger.ONE);
    //cos 的导数(triangle类型)
    private static final Triangle TRIANGLECOSINE = new Triangle("","cos",BigInteger.ONE);
    /**
     * type 用于定义sin 和 cos 类型
     */
    private final String type;

    /**
     * src.Triangle：简单定义类，不同于通过字符串判定类
     * @param symbols 符号
     * @param type 类型
     * @param exp 指数
     */
    public Triangle(String symbols,String type,BigInteger exp)
    {
        this.symbols = symbols;
        this.type = type;
        this.exp = exp;
    }

    /**
     * src.Triangle：通过输入字符串确定一个新的三角函数
     ** @param str 传入字符
     */
    public Triangle(String str)
    {
        //Symbol 表示 因子的所带符号数
        this.symbols = str.charAt(0) == '-' ? "-" : "";
        //Type 表示sin和cos
        if (str.charAt(0) == '+' || str.charAt(0) == '-') {
            this.type = str.startsWith("sin", 1) ? "sin" : "cos";
        }
        else {
            this.type = str.startsWith("sin") ? "sin" : "cos";
        }
        Pattern pattern = Pattern.compile("[+-]?\\d+");
        Matcher matcher = pattern.matcher(str);
        //Exp 得到指数
        this.exp = BigInteger.valueOf(matcher.find() ? Long.parseLong(matcher.group()) : 1);
    }

    /**
     * 用于优化时的判断
     */
    public String getType() {
        return type;
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
        Triangle triangleLeft = new Triangle(symbols,type,exp.subtract(BigInteger.ONE));
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
            Triangle t = type.equals("sin") ? TRIANGLECOSINE : TRIANGLESINE;
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
                         exp.equals(BigInteger.ONE) ? type + "(x)" :
                                                      type + "(x)**" + exp;
        return sign + formats;
    }
}
