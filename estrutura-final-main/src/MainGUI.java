import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MainGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Compressor de Arquivos - Huffman");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 700);
            frame.setLayout(new BorderLayout());

            // Define o statusLabel como uma variável global acessível
            final JLabel statusLabel = new JLabel("Pronto");
            statusLabel.setHorizontalAlignment(SwingConstants.LEFT);

            // Painel de entrada
            JPanel inputPanel = new JPanel(new BorderLayout());
            inputPanel.setBorder(new TitledBorder("Entrada (Texto ou Arquivo)"));

            JTextArea inputArea = new JTextArea();
            inputArea.setLineWrap(true);
            inputArea.setWrapStyleWord(true);
            JScrollPane inputScrollPane = new JScrollPane(inputArea);

            JButton selectFileButton = new JButton("Selecionar Arquivo");
            selectFileButton.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos TXT", "txt"));
                int returnValue = fileChooser.showOpenDialog(frame);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    try {
                        String content = readFile(fileChooser.getSelectedFile().getAbsolutePath());
                        inputArea.setText(content);
                        setStatus(statusLabel, "Arquivo carregado com sucesso!", true);
                    } catch (IOException ex) {
                        setStatus(statusLabel, "Erro ao carregar o arquivo: " + ex.getMessage(), false);
                    }
                }
            });

            inputPanel.add(inputScrollPane, BorderLayout.CENTER);
            inputPanel.add(selectFileButton, BorderLayout.SOUTH);

            // Painel de saída
            JPanel outputPanel = new JPanel(new BorderLayout());
            outputPanel.setBorder(new TitledBorder("Saída (Resultados)"));

            JTextArea outputArea = new JTextArea();
            outputArea.setEditable(false);
            outputArea.setLineWrap(true);
            outputArea.setWrapStyleWord(true);
            JScrollPane outputScrollPane = new JScrollPane(outputArea);

            JButton compressButton = new JButton("Comprimir");
            compressButton.addActionListener(e -> {
                String input = inputArea.getText().trim();
                if (input.isEmpty()) {
                    setStatus(statusLabel, "Digite ou selecione o conteúdo para compressão!", false);
                } else {
                    try {
                        // Processa a string e constrói a árvore de Huffman
                        HuffmanTree huffmanTree = new HuffmanTree();
                        Node root = processHuffman(input, huffmanTree, outputArea);
                        setStatus(statusLabel, "Compressão concluída com sucesso!", true);
                    } catch (Exception ex) {
                        setStatus(statusLabel, "Erro ao processar o conteúdo: " + ex.getMessage(), false);
                    }
                }
            });

            outputPanel.add(outputScrollPane, BorderLayout.CENTER);

            // Adiciona o botão "Comprimir" no painel de saída
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.add(compressButton);
            outputPanel.add(buttonPanel, BorderLayout.SOUTH);

            // Painel da árvore
            JPanel treePanel = new JPanel(new BorderLayout());
            treePanel.setBorder(new TitledBorder("Árvore de Huffman"));

            HuffmanTreeVisualizer treeVisualizer = new HuffmanTreeVisualizer();
            treePanel.add(treeVisualizer, BorderLayout.CENTER);

            // Barra de status
            JPanel statusPanel = new JPanel(new BorderLayout());
            statusPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            statusPanel.add(statusLabel, BorderLayout.WEST);

            // Adiciona os painéis ao frame principal
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inputPanel, treePanel);
            splitPane.setDividerLocation(400);

            frame.add(splitPane, BorderLayout.CENTER);
            frame.add(outputPanel, BorderLayout.SOUTH);
            frame.add(statusPanel, BorderLayout.NORTH);

            frame.setVisible(true);
        });
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

    // Processa o texto e constrói a árvore de Huffman
    public static Node processHuffman(String input, HuffmanTree huffmanTree, JTextArea outputArea) {
        FrequencyCounter frequencyCounter = new FrequencyCounter(input.length());
        for (char c : input.toCharArray()) {
            frequencyCounter.add(c);
        }

        Node root = huffmanTree.buildTree(frequencyCounter.getCharacters(), frequencyCounter.getFrequencies());

        HuffmanCoding huffmanCoding = new HuffmanCoding();
        CodeMap codeMap = huffmanCoding.getCodes(root, frequencyCounter.getSize());

        Compressor compressor = new Compressor();
        String compressed = compressor.compress(input, codeMap);

        Decompressor decompressor = new Decompressor();
        String decompressed = decompressor.decompress(compressed, root);

        outputArea.setText("String Original:\n" + input + "\n\n" +
                "String Comprimida:\n" + compressed + "\n\n" +
                "String Descomprimida:\n" + decompressed);

        return root; // Retorna a raiz da árvore
    }

    // Define mensagens na barra de status
    private static void setStatus(JLabel statusLabel, String message, boolean success) {
        statusLabel.setText(message);
        statusLabel.setForeground(success ? Color.GREEN : Color.RED);
    }
}

// Classe para desenhar a árvore de Huffman
class HuffmanTreeVisualizer extends JPanel {
    private Node root;

    public void setTreeRoot(Node root) {
        this.root = root;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (root != null) {
            drawTree(g, root, getWidth() / 2, 30, getWidth() / 4);
        }
    }

    private void drawTree(Graphics g, Node node, int x, int y, int horizontalGap) {
        if (node == null) return;

        g.drawString(node.character + " (" + node.frequency + ")", x - 10, y);
        if (node.left != null) {
            g.drawLine(x, y, x - horizontalGap, y + 50);
            drawTree(g, node.left, x - horizontalGap, y + 50, horizontalGap / 2);
        }
        if (node.right != null) {
            g.drawLine(x, y, x + horizontalGap, y + 50);
            drawTree(g, node.right, x + horizontalGap, y + 50, horizontalGap / 2);
        }
    }
}
