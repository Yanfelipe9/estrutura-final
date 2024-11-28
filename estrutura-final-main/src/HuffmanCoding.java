public class HuffmanCoding {

    /**
     * Gera os códigos binários para os caracteres, percorrendo a árvore de Huffman.
     * @param root O nó raiz da árvore de Huffman.
     * @param code O código binário acumulado para o nó atual.
     * @param codeMap A estrutura personalizada para armazenar os pares caractere-código.
     */
    public void generateCodes(Node root, String code, CodeMap codeMap) {
        if (root == null) {
            return;
        }

        // Se o nó é uma folha, adicionamos o caractere e seu código ao CodeMap
        if (root.character != '\0') {
            codeMap.put(root.character, code);
        }

        // Recursivamente gerar os códigos para os filhos esquerdo e direito
        generateCodes(root.left, code + "0", codeMap);
        generateCodes(root.right, code + "1", codeMap);
    }

    /**
     * Método de conveniência para inicializar e preencher o CodeMap.
     * @param root A raiz da árvore de Huffman.
     * @param numCharacters Número de caracteres distintos (tamanho do CodeMap).
     * @return O CodeMap contendo os pares caractere-código.
     */
    public CodeMap getCodes(Node root, int numCharacters) {
        CodeMap codeMap = new CodeMap(numCharacters); // Inicializa o CodeMap
        generateCodes(root, "", codeMap); // Gera os códigos de Huffman
        return codeMap;
    }
}
