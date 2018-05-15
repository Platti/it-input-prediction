import java.util.Arrays;
import java.util.List;

public class InputPredictionModel {
    private String text;
    private InputPredictionView view;

    InputPredictionModel() {
        text = "";
    }

    List<String> getPredictions() {
        return Arrays.asList(new String[]{"Hello", "world", "Interactive", "Technologies"});
    }

    void appendText(String prediction) {
        if (text.isEmpty() || text.endsWith(" ")) {
            text += prediction + " ";
        } else if (text.endsWith(".")) {
            text += " " + prediction + " ";
        } else if (text.contains(" ")) {
            text = text.substring(0, text.lastIndexOf(" ") + 1) + prediction + " ";
        } else {
            text = prediction + " ";
        }
        view.update();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setView(InputPredictionView view) {
        this.view = view;
    }
}
