public class HuffmanTree {
    public Node buildTree(char[] characters, int[] frequencies) {
        PriorityQueue queue = new PriorityQueue(characters.length);

        for (int i = 0; i < characters.length; i++) {
            queue.insert(new Node(characters[i], frequencies[i]));
        }

        while (queue.size > 1) {
            Node left = queue.extractMin();
            Node right = queue.extractMin();

            Node parent = new Node('\0', left.frequency + right.frequency);
            parent.left = left;
            parent.right = right;

            queue.insert(parent);
        }

        return queue.extractMin();
    }
}
