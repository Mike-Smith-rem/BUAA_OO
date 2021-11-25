import java.util.ArrayList;
import java.util.Stack;

import static java.lang.Character.*;

public class BuildPostFix {
    private static final ArrayList<String> exp = new ArrayList<>();

    public BuildPostFix(String str) throws Exceptions {
        Stack<Character> op = new Stack<>();
        for (int i = 0; i < str.length(); i++) {
            //如果是元素
            if (isAlphabetic(str.charAt(i))) {
                //N11
                int j = i + 1;
                while (j < str.length() && isDigit(str.charAt(j))) {
                    j++;
                }
                //加入N11
                exp.add(str.substring(i, j));
                i = j - 1;
            }
            //如果是括号
            else if (str.charAt(i) == '(') {
                op.push(str.charAt(i));
            } else if (str.charAt(i) == ')') {
                char temp;
                while ((temp = op.pop()) != '(') {
                    exp.add(Character.toString(temp));
                }
            }
            //如果是操作符号
            else if (isSymbol(str.charAt(i))) {
                while (true) {
                    if (op.isEmpty()) {
                        op.push(str.charAt(i));
                        break;
                    } else if (Prior(op.peek()) < Prior(str.charAt(i))) {
                        op.push(str.charAt(i));
                        break;
                    } else {
                        exp.add(Character.toString(op.pop()));
                    }
                }
            }
        }
        while (!op.isEmpty()) {
            if (op.peek() == ')' || op.peek() == '(') throw new Exceptions();
            exp.add(Character.toString(op.pop()));
        }
        //System.out.println(exp);
    }

    private static Integer Prior(Character c) {
        if (c == '+' || c == '-') {
            return 1;
        } else if (c == '*') {
            return 2;
        } else if (c == '/') {
            return 3;
        }
        return 0;
    }

    private static boolean isSymbol(Character c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    public ArrayList<String> getExp() {
        return exp;
    }
}
