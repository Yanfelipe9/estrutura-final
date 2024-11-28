public class CodeMap {
    private char[] keys;
    private String[] values;
    private int size;
    private int capacity;

    public CodeMap(int capacity) {
        this.keys = new char[capacity];
        this.values = new String[capacity];
        this.size = 0;
    }

    public void put(char key, String value) {
        keys[size] = key;
        values[size] = value;
        size++;
    }

    public String get(char key) {
        for (int i = 0; i < size; i++) {
            if (keys[i] == key) {
                return values[i];
            }
        }
        return null;
    }
}
