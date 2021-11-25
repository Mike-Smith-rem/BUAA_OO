import java.util.ArrayList;

public class Term extends Factor implements Derivation {
    private ArrayList<Factor> factors = new ArrayList<>();

    /**
     * 以下三个函数同质
     * 用于自底向上
     *
     */
    public Term(ArrayList<Factor> arrayList) {
        factors.addAll(arrayList);
    }

    /**
     * 专门用于传递克隆
     */
    public Term() {
        super();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Factor factor : factors) {
            stringBuilder.append(factor.toString()).append("*");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }
}
