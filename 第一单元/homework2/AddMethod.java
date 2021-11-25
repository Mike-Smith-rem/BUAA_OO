package src;

public class AddMethod {
    private Node node;

    public AddMethod(Node node) {
        this.node = node;
    }

    public Node setDerivation() {
        Node ret = new Node("+");

        Node left = node.getLeft();
        Node right = node.getRight();
        Object value1 = left.getData();
        Object value2 = right.getData();
        Node leftNode;
        Node rightNode;
        leftNode = getNode(left, value1, null);
        ret.setLeft(leftNode);
//        node.setLeft(leftNode);
        rightNode = getNode(right, value2, null);
//        node.setRight(rightNode);
        ret.setRight(rightNode);
        return ret;
    }

    static Node getNode(Node left, Object value1, Node leftNode) {
        if (value1 instanceof Factor) {
            leftNode = new Node(((Factor) value1).setDerivation().derivation);
        } else if (value1 instanceof String) {
            if (value1.equals("-")) {
                leftNode = new SubtractMethod(left).setDerivation();
            } else if (value1.equals("+")) {
                leftNode = new AddMethod(left).setDerivation();
            } else if (value1.equals("*")) {
                leftNode = new MultipyMethod(left).setDerivation();
            }
        }
        return leftNode;
    }

    public String toString() {
        return "(" +
                (node.getLeft() == null ? "" : node.getLeft().toString()) +
                node.getData() +
                (node.getRight() == null ? "" : node.getRight().toString()) +
                ")";
    }
}
