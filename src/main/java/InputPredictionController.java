import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputPredictionController implements ActionListener, KeyListener {
    private InputPredictionModel model;
    private InputPredictionView view;

    @Override
    public void actionPerformed(ActionEvent e) {
        model.appendText(((JButton) (e.getSource())).getText());
    }

    @Override
    public void keyTyped(KeyEvent e) {
        model.setText(view.getText());
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void setModel(InputPredictionModel model) {
        this.model = model;
    }

    public void setView(InputPredictionView view) {
        this.view = view;
    }
}
