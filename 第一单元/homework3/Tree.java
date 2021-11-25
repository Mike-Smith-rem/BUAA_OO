public class Tree {
    private Node root;

    public Tree(Node root) {
        this.root = root;
    }

    public Tree setDerivation() {
        if (root.getData().equals("+")) {
            return new Tree(new AddMethod(root).setDerivation());
        }
        else if (root.getData().equals("-")) {
            return new Tree(new SubtractMethod(root).setDerivation());
        }
        else if (root.getData().equals("*")) {
            return new Tree(new MultipyMethod(root).setDerivation());
        }
        else if (root.getData() instanceof Factor) {
            return new Tree(new Node(((Factor) root.getData()).setDerivation().derivation));
        }
        else if (root.getData().equals("/")) {
            return new Tree(new TriangleMethod(root).setDerivation());
        }
        return null;
    }

    public String toString() {
        return root.toString();
    }
}
