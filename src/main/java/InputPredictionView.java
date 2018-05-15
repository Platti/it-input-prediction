import javax.swing.*;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class InputPredictionView {
    private JFrame frame;
    private JTextArea textInput;
    private JPanel buttonPanel;
    private InputPredictionModel model;
    private InputPredictionController controller;

    public InputPredictionView() {
        frame = new JFrame("Input Prediction");
        frame.setSize(500, 300);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textInput = new JTextArea();
        textInput.setLineWrap(true);
        textInput.setWrapStyleWord(true);
        frame.add(textInput, BorderLayout.CENTER);

        buttonPanel = new JPanel();
        frame.add(buttonPanel, BorderLayout.NORTH);
        frame.setVisible(true);
    }

    void update() {
        textInput.setText(model.getText());
        updatePredictions();
    }

    void updatePredictions(){
        buttonPanel.removeAll();
        List<Map.Entry<String, Integer>> predictions = model.getPredictions();
        if (predictions != null && !predictions.isEmpty())
            for (Map.Entry<String, Integer> p : predictions) {
                JButton btn = new JButton(p.getKey());
                btn.addActionListener(controller);
                buttonPanel.add(btn);
            }
        else
            buttonPanel.add(new JLabel("No predictions available..."));
        frame.revalidate();
        frame.repaint();
    }

    public String getText(){
        return textInput.getText();
    }

    public void setModel(InputPredictionModel model) {
        this.model = model;
    }

    public void setController(InputPredictionController controller) {
        this.controller = controller;
        textInput.getDocument().addDocumentListener(controller);
    }
}
