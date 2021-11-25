import java.math.BigInteger;
import java.util.Map;

public class Differential {
    private StringBuilder arrayList = new StringBuilder();

    public Differential(Map<BigInteger,Term> termMap)
    {
        for (BigInteger item : termMap.keySet())
        {
            Term term = termMap.get(item);
            if (!term.getCoe().equals(BigInteger.ZERO))
            {
                if (term.getExp().equals(BigInteger.ONE))
                {
                    arrayList.append(term.getCoe());
                }
                else if (!term.getExp().equals(BigInteger.ZERO))
                {
                    BigInteger bigInteger = term.getCoe().multiply(term.getExp());
                    if (!bigInteger.equals(BigInteger.valueOf(-1))) {
                        arrayList.append(bigInteger)
                                 .append("*");
                    }
                    else
                    {
                        arrayList.append("-");
                    }
                    arrayList.append("x");
                    bigInteger = term.getExp().subtract(BigInteger.valueOf(1));
                    if (!bigInteger.equals(BigInteger.ONE)) {
                        arrayList.append("**")
                                 .append(bigInteger);
                    }
                }
            }
            arrayList.append("+");
        }
        String string = arrayList.toString();
        string = string.replaceAll("\\+\\++","+");
        string = string.substring(0, string.length() - 1);
        if (string.length() == 0)
        {
            string = "0";
        }
        if (string.charAt(0) == '+')
        {
            string = string.substring(1);
        }
        string = string.replaceAll("\\+-","-");
        System.out.println(string);
    }
}
