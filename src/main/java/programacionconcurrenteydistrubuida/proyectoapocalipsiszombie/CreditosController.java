package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class CreditosController {
    public Button VolverButton;

    public void onVolverButtonClick(ActionEvent actionEvent) {
        Stage stage = (Stage) VolverButton.getScene().getWindow();
        stage.close();
    }
}
