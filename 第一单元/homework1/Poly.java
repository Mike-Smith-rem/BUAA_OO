import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Poly {
    private static Map<BigInteger, Term> termArrayList = new HashMap<>();

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        String poly = scanner.nextLine();
        poly = poly.replace(" ","");
        poly = poly.replace("\t","");
        poly = poly.replaceAll("x","+1*x**+1");
        poly = poly.replaceAll("x\\*\\*\\+1\\*\\*","x**");
        poly = poly.replaceAll("(\\+-\\+)|(-\\+\\+)|(\\+\\+-)","-");
        poly = poly.replaceAll("(--\\+)|(-\\+-)|(\\+--)","+");
        poly = poly.replaceAll("(--)|(\\+\\+)","+");
        poly = poly.replaceAll("(\\+-)|(-\\+)","-");
        String regexs = "(\\+|-)?\\d+(\\*((\\+|-)?\\d+\\*)*x\\*\\*(\\+|-)?\\d+)*(\\*(\\+|-)?\\d+)*";
        Pattern pattern = Pattern.compile(regexs);
        Matcher matcher = pattern.matcher(poly);
        while (matcher.find())
        {
            Term term = new Term(new String(poly.substring(matcher.start(),matcher.end())));
            int flag = 0;
            for (BigInteger key : termArrayList.keySet())
            {
                if (termArrayList.get(key).getExp().equals(term.getExp()))
                {
                    BigInteger b1 = termArrayList.get(key).getCoe();
                    BigInteger b2 = term.getCoe();
                    termArrayList.get(key).setCoe(b1.add(b2));
                    flag = 1;
                }
            }
            if (flag != 1)
            {
                termArrayList.put(term.getExp(),term);
            }
        }
        new Differential(termArrayList);
    }
}
