import javax.swing.*;
import java.awt.*;

public class InputPrediction {
    public static void main(String[] args) {
        InputPredictionModel model = new InputPredictionModel();
        InputPredictionView view = new InputPredictionView();
        InputPredictionController controller = new InputPredictionController();

        model.setView(view);
        view.setModel(model);
        view.setController(controller);
        controller.setModel(model);
        controller.setView(view);

        view.update();
    }
}
