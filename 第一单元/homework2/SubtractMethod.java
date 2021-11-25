package src;

public class SubtractMethod {
    private Node node;

    public SubtractMethod(Node node) {
        this.node = node;
    }

    public Node setDerivation() {
        Node ret = new Node("-");

        Node left = node.getLeft();
        Node right = node.getRight();
        Object value1 = left.getData();
        Object value2 = right.getData();
        Node leftNode = null;
        Node rightNode = null;
        if (value1 instanceof Factor) {
            leftNode = new Node(((Factor) value1).setDerivation().derivation);
        } else if (value1 instanceof String) {
            if (value1.equals("-")) {
                leftNode = new SubtractMethod(node.getLeft()).setDerivation();
            } else if (value1.equals("+")) {
                leftNode = new AddMethod(node.getLeft()).setDerivation();
            } else if (value1.equals("*")) {
                leftNode = new MultipyMethod(node.getLeft()).setDerivation();
            }
        }
        ret.setLeft(leftNode);
        //node.setLeft(leftNode);
        if (value2 instanceof Factor) {
            rightNode = new Node(((Factor) value2).setDerivation().derivation);
        } else if (value2 instanceof String) {
            if (value2.equals("-")) {
                rightNode = new SubtractMethod(node.getRight()).setDerivation();
            } else if (value2.equals("+")) {
                rightNode = new AddMethod(node.getRight()).setDerivation();
            } else if (value2.equals("*")) {
                rightNode = new MultipyMethod(node.getRight()).setDerivation();
            }
        }
        ret.setRight(rightNode);
        //node.setRight(rightNode);
        return ret;
    }

    public String toString() {
        return "(" +
                (node.getLeft() == null ? "" : node.getLeft().toString()) +
                node.getData() +
                (node.getRight() == null ? "" : node.getRight().toString()) +
                ")";
    }
}
