import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Solicita o nome do arquivo para leitura
        System.out.print("Digite o caminho completo do arquivo para leitura (ou pressione Enter para inserir manualmente): ");
        String filename = scanner.nextLine();

        // Variável para armazenar o conteúdo do arquivo ou a entrada manual
        String input = "";

        // Se o caminho do arquivo foi informado
        if (!filename.trim().isEmpty()) {
            try {
                input = readFile(filename); // Função para ler o arquivo
                System.out.println("Conteúdo do arquivo: \n" + input);
            } catch (IOException e) {
                System.out.println("Erro ao ler o arquivo: " + e.getMessage());
                System.out.println("Você pode inserir a string manualmente.");
            }
        }

        // Caso o arquivo não tenha sido lido ou o caminho não tenha sido informado, pede a entrada manual
        if (input.isEmpty()) {
            System.out.print("Digite a string a ser comprimida: ");
            input = scanner.nextLine();
        }

        // Conta as frequências dos caracteres
        FrequencyCounter frequencyCounter = new FrequencyCounter(input.length());
        for (char c : input.toCharArray()) {
            frequencyCounter.add(c);
        }

        // Gera a árvore de Huffman
        HuffmanTree huffmanTree = new HuffmanTree();
        Node root = huffmanTree.buildTree(frequencyCounter.getCharacters(), frequencyCounter.getFrequencies());

        // Gera os códigos binários de Huffman
        HuffmanCoding huffmanCoding = new HuffmanCoding();
        CodeMap codeMap = huffmanCoding.getCodes(root, frequencyCounter.getSize());

        // Comprime a entrada
        Compressor compressor = new Compressor();
        String compressed = compressor.compress(input, codeMap);
        System.out.println("String comprimida: " + compressed);

        // Descomprime a string
        Decompressor decompressor = new Decompressor();
        String decompressed = decompressor.decompress(compressed, root);
        System.out.println("String descomprimida: " + decompressed);

        scanner.close();
    }

    // Função para ler o arquivo
    public static String readFile(String filename) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }
}
//C:\\Users\\Alunod26\\Downloads\\estrutura-final-main\\estrutura-final-main\\src\\txt.txt