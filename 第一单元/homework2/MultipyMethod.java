package src;

public class MultipyMethod {
    private Node node;

    public MultipyMethod(Node node) {
        this.node = node;
    }

    public Node setDerivation() {

        Node left = node.getLeft();
        Node right = node.getRight();
        Node leftNode;
        Node rightNode;

        Node newNode1 = new Node("*");
        Object value1 = left.getData();
        leftNode = AddMethod.getNode(left, value1, null);
        rightNode = right;
        newNode1.setLeft(leftNode);
        newNode1.setRight(rightNode);

        Node newNode2 = new Node("*");
        Object value2 = right.getData();
        rightNode = AddMethod.getNode(right, value2, rightNode);
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
