import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class InputPredictionModel {
    private static final int MAX_PREFIX_LETTERS = Integer.MAX_VALUE;
    private String text;
    private InputPredictionView view;
    private PredictionLearner predictionLearner;

    InputPredictionModel() {
        text = "";
        predictionLearner = new PredictionLearner("data/smsCorpus_en_2015.03.09_all.xml", MAX_PREFIX_LETTERS);
    }

    List<Map.Entry<String, Integer>> getPredictions() {
        String line = text.contains("\n") ? text.substring(text.lastIndexOf("\n") + 1, text.length()) : text;
        line = line.replaceAll("[^a-zA-ZäöüÄÖÜ ]", "");
        if (line.length() > 1 && line.endsWith(" ")) { // Succeeding word prediction
            String temp = line.substring(0, line.length() - 1);
            String lastWord = temp.contains(" ") ? temp.substring(temp.lastIndexOf(" ") + 1) : temp;
            lastWord = lastWord.toLowerCase();
            return predictionLearner.getDatabaseSucceeding().get(lastWord);
        } else if (line.matches(".*[a-zA-Z]$")) { // word completion prediction
            String lastWord = line.contains(" ") ? line.substring(line.lastIndexOf(" ") + 1) : line;
            String prefix = lastWord.length() > MAX_PREFIX_LETTERS ? lastWord.substring(0, MAX_PREFIX_LETTERS) : lastWord;
            prefix = prefix.toLowerCase();
            return predictionLearner.getDatabaseCompletion().get(prefix);
        } else if (line.isEmpty()) {
            return Arrays.asList(new AbstractMap.SimpleEntry<String, Integer>("Hello", 0));
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
