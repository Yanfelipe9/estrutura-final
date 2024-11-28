public class Compressor {
    public String compress(String input, CodeMap codeMap) {
        StringBuilder compressed = new StringBuilder();
        for (char c : input.toCharArray()) {
            String code = codeMap.get(c);
            if (code != null) {
                compressed.append(code);
            } else {
                throw new IllegalArgumentException("Character not found: " + c);
            }
        }
        return compressed.toString();
    }
}
