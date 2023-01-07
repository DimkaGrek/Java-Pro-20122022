package task2;

public class Main {
    public static void main(String[] args) {

        TextContainer text = new TextContainer("I like Java");
        SaveToFile.saveText(text);
    }
}
