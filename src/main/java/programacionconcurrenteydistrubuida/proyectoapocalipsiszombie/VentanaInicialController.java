package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class VentanaInicialController {
    public Button iniciarPartidaButton;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public void iniciarPartidaButtonClick(ActionEvent actionEvent) {
        Stage stageAntiguo = (Stage) iniciarPartidaButton.getScene().getWindow();
        stageAntiguo.close();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(VistaPrincipal.class.getResource("nombre.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 750, 500);
            stage.setTitle("Nombre");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}