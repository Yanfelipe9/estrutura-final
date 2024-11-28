public class Decompressor {
    public String decompress(String compressed, Node root) {
        StringBuilder decompressed = new StringBuilder();
        Node current = root;

        for (char bit : compressed.toCharArray()) {
            current = (bit == '0') ? current.left : current.right;

            if (current.left == null && current.right == null) {
                decompressed.append(current.character);
                current = root;
            }
        }

        return decompressed.toString();
    }
}
