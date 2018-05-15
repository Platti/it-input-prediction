import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class InputPredictionModel {
    private String text;
    private InputPredictionView view;
    private ProbabilityLearner probabilityLearner;

    InputPredictionModel() {
        text = "";
        probabilityLearner = new ProbabilityLearner("data/smsCorpus_en_2015.03.09_all.xml");
    }

    List<Map.Entry<String, Double>> getPredictions() {
        if (text.length() > 1 && text.endsWith(" ")) {
            String temp = text.substring(0, text.length() - 1);
            String lastWord = temp.contains(" ") ? temp.substring(temp.lastIndexOf(" ") + 1) : temp;
            lastWord = lastWord.toLowerCase();
            return probabilityLearner.getDatabase().get(lastWord);
        } else if (text.isEmpty()) {
            return Arrays.asList(new AbstractMap.SimpleEntry<String, Double>("Hello", 0d));
        } else {
            return null;
        }

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
