public class Node {
    public char character; // Caractere representado
    public int frequency;  // Frequência do caractere
    public Node left;      // Filho esquerdo
    public Node right;     // Filho direito

    public Node(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
        this.left = null;
        this.right = null;
    }
}
