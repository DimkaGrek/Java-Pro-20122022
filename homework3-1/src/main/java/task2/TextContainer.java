package task2;

import java.io.FileWriter;
import java.io.IOException;

@SaveTo(path = "info.txt")
public class TextContainer {
    String text;

    public TextContainer(String text) {
        this.text = text;
    }

    @Saver
    public void save(String path)  {
        try (FileWriter f = new FileWriter(path, true) )
        {
            f.write(text + "\n");
            f.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
