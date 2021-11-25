import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Term
{
    private BigInteger coe = BigInteger.valueOf(1);
    private BigInteger exp = BigInteger.valueOf(0);

    public Term(String str)
    {
        String regex = "x\\*\\*(\\+|-)?\\d+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find())
        {
            String exps = str.substring(matcher.start() + 3, matcher.end());
            BigInteger exp1 = new BigInteger(exps);
            exp = exp.add(exp1);
        }
        String coe1 = str.replaceAll(regex,"");
        Pattern patterns = Pattern.compile("(\\+|-)?\\d+");
        Matcher matchers = patterns.matcher(coe1);
        while (matchers.find()) {
            String coe2 = coe1.substring(matchers.start(), matchers.end());
            BigInteger coes = new BigInteger(coe2);
            coe = coe.multiply(coes);
        }

    }

    public BigInteger getCoe()
    {
        return coe;
    }

    public BigInteger getExp()
    {
        return exp;
    }

    public void setCoe(BigInteger bigInteger)
    {
        this.coe = bigInteger;
    }

    public void setExp(BigInteger bigInteger)
    {
        this.exp = bigInteger;
    }
}
