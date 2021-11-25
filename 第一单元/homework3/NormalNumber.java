import java.math.BigInteger;
import java.util.ArrayList;

public class NormalNumber extends Factor implements Derivation {
    private BigInteger bigInteger = BigInteger.ZERO;

    /**
     * 简单定义类
     * @param bigInteger 系数
     * @param symbols 符号
     */
    public NormalNumber(BigInteger bigInteger,String symbols) {
        this.symbols = symbols;
        this.bigInteger = bigInteger;
    }

    /**
     * Power：通过输入字符串确定一个新的常数因子
     * @param str 字符串
     */
    public NormalNumber(String strs) {
        String str = strs.replaceAll("\\s+","");
        this.symbols = str.charAt(0) == '-' ? "-" : "";
        if (str.charAt(0) == '+' || str.charAt(0) == '-') {
            bigInteger = BigInteger.valueOf(Long.parseLong(str.substring(1)));
        }
        else {
            bigInteger = new BigInteger(str);
        }
    }

    /**
     * 优化判断
     * @return Biginteger
     */
    public BigInteger getBigInteger() {
        return bigInteger;
    }

    public Factor setDerivation() {
        NormalNumber number = new NormalNumber(BigInteger.ZERO,"");
        ArrayList<Factor> arrayList = new ArrayList<>();
        arrayList.add(number);
        this.derivation = new Term(arrayList);
        return this;
    }

    @Override
    public String toString() {
        return symbols.equals("-") ? "-1*" + bigInteger.toString() : "" + bigInteger.toString();
    }
}