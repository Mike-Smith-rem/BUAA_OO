package src;

public class Node {
    private Object data = null;
    private Node left = null;
    private Node right = null;

    public Node(Factor factor) {
        setFactor(factor);
    }

    public Node(String string) {
        setFactor(string);
    }

    public void setFactor(Object data) {
        this.data = data;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public Object getData() {
        return data;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public String toString() {
        if (data.getClass().equals(String.class)) {
            return "(" + (left == null ? "0" : left.toString())
                       + data
                       + (right == null ? "0" : right.toString())
                       + ")";
        }
        else if (data instanceof Factor) {
            Factor factor;
            String str = "";
            if (data.getClass().equals(Triangle.class)) {
                factor = (Triangle) data;
                str = factor.toString();
            }
            else if (data.getClass().equals(Power.class)) {
                factor = (Power) data;
                str = factor.toString();
            }
            else if (data.getClass().equals(NormalNumber.class)) {
                factor = (NormalNumber) data;
                str = factor.toString();
            }
            else if (data.getClass().equals(Term.class)) {
                factor = (Term) data;
                str = factor.toString();
            }
            return  "(" + (left == null ? "" : left.toString())
                    + str
                    + (right == null ? "" : right.toString())
                    + ")";
        }
        return null;
    }
}
