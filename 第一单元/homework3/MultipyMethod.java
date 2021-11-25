public class MultipyMethod {
    private Node node;

    public MultipyMethod(Node node) {
        this.node = node;
    }

    public Node setDerivation() {

        Node left = node.getLeft();
        Node right = node.getRight();
        Node leftNode = null;
        Node rightNode = null;

        Node newNode1 = new Node("*");
        Object value1 = left.getData();
        if (value1 instanceof Factor) {
            leftNode = new Node(((Factor) value1).setDerivation().derivation);
        } else if (value1 instanceof String) {
            if (value1.equals("-")) {
                leftNode = new SubtractMethod(left).setDerivation();
            } else if (value1.equals("+")) {
                leftNode = new AddMethod(left).setDerivation();
            } else if (value1.equals("*")) {
                leftNode = new MultipyMethod(left).setDerivation();
            } else if (value1.equals("/")) {
                leftNode = new TriangleMethod(left).setDerivation();
            }
        }
        rightNode = right;
        newNode1.setLeft(leftNode);
        newNode1.setRight(rightNode);

        Node newNode2 = new Node("*");
        Object value2 = right.getData();
        if (value2 instanceof Factor) {
            rightNode = new Node(((Factor) value2).setDerivation().derivation);
        } else if (value2 instanceof String) {
            if (value2.equals("-")) {
                rightNode = new SubtractMethod(right).setDerivation();
            } else if (value2.equals("+")) {
                rightNode = new AddMethod(right).setDerivation();
            } else if (value2.equals("*")) {
                rightNode = new MultipyMethod(right).setDerivation();
            } else if (value2.equals("/")) {
                rightNode = new TriangleMethod(right).setDerivation();
            }
        }
        leftNode = left;
        newNode2.setLeft(leftNode);
        newNode2.setRight(rightNode);

        Node newNode = new Node("+");
        newNode.setLeft(newNode1);
        newNode.setRight(newNode2);
        return newNode;
    }

    public String toString() {
        return "(" +
                (node.getLeft() == null ? "" : node.getLeft().toString()) +
                node.getData() +
                (node.getRight() == null ? "" : node.getRight().toString()) +
                ")";
    }
}
