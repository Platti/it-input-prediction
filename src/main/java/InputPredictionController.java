import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InputPredictionController implements ActionListener, DocumentListener {
    private InputPredictionModel model;
    private InputPredictionView view;

    @Override
    public void actionPerformed(ActionEvent e) {
        model.appendText(((JButton) (e.getSource())).getText());
    }

    @Override
    public void changedUpdate(DocumentEvent e) {

    }

    public void setModel(InputPredictionModel model) {
        this.model = model;
    }

    public void setView(InputPredictionView view) {
        this.view = view;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        model.setText(view.getText());
        view.updatePredictions();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        model.setText(view.getText());
        view.updatePredictions();
    }
}
