import java.math.BigInteger;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Power extends Factor implements Derivation {
    /**
     * 简单定义类型
     * @param symbols 符号
     * @param exp 指数
     */
    public Power(String symbols,BigInteger exp) {
        this.symbols = symbols;
        this.exp = exp;
    }

    /**
     * Power：通过输入字符串确定一个新的幂函数
     * @param str 传入字符
     */
    public Power(String str) throws Exceptions {
        //Symbol
        this.symbols = str.charAt(0) == '-' ? "-" : "";
        Pattern pattern = Pattern.compile("(\\+|-)?\\d+");
        Matcher matcher = pattern.matcher(str);
        this.exp = new BigInteger(matcher.find() ? matcher.group() : "1");
        if (exp.compareTo(BigInteger.valueOf(50)) > 0)
        {
            throw new Exceptions();
        }
    }

    /**
     * 链表类型（自返）
     * 用于得到幂函数的导数
     * 根据自底向上原则，因子的导数为项
     * @return 自反
     */
    public Factor setDerivation() {
        NormalNumber number = new NormalNumber(exp,"");
        Power power = new Power(this.symbols,exp.subtract(BigInteger.ONE));
        ArrayList<Factor> arrayList = new ArrayList<>();
        if (this.exp.equals(BigInteger.ZERO)) {
            arrayList.add(new NormalNumber(BigInteger.ZERO,""));
        }
        else if (this.exp.equals(BigInteger.ONE)) {
            arrayList.add(number);
        }
        else {
            arrayList.add(number);
            arrayList.add(power);
        }
        this.derivation = new Term(arrayList);
        return this;
    }

    @Override
    public String toString() {
        String sign = symbols.equals("-") ? "-1*" : "";
        String format = exp.equals(BigInteger.ZERO) ? "1" :
                        exp.equals(BigInteger.ONE) ? "x" :
                                                     "x**" + exp;
        return sign + format;
    }
}
