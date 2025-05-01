package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Controlador;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class FinJuego {
    public Button salirButton;

    public void onSalirButtonClick(ActionEvent actionEvent) {
        Stage stageAntiguo = (Stage) salirButton.getScene().getWindow();
        stageAntiguo.close();
        Stage stage = new Stage();
    }
}
