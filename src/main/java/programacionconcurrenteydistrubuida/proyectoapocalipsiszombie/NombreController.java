package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NombreController {////SEGUNDA VENTANA

    public TextField textFieldNombre;
    public Button continuarButton;
    public Button VolverButton;
    public Label labelError;

    private String nombreGuardadoString;

    public void onSiguienteButtonClick(ActionEvent actionEvent) {
        nombreGuardadoString = (textFieldNombre.getText());
        if (nombreGuardadoString.equals("")) {
            labelError.setText("El nombre no puede ser vacío");
        } else {
            System.out.println("Nombre Guardado: " + nombreGuardadoString);
            Stage stageAnterior = (Stage) continuarButton.getScene().getWindow();
            stageAnterior.close();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(VistaPrincipal.class.getResource("ventanaPrincipal.fxml"));
            try {
                Scene scene = new Scene(fxmlLoader.load(), 800, 650);
                stage.setTitle("Apocalipsis Zombie de..." + nombreGuardadoString.toUpperCase());
                stage.setScene(scene);
                //Aqui creamos el controlador de la ventana de configuracion y le guardamos la Data
                VentanaPrincipalController ventanaPrincipalController = fxmlLoader.getController();
                //TODO -> Pasar los datos iniciales del programa desde aquí
                ventanaPrincipalController.setStage(stage);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void onVolverButton(ActionEvent actionEvent) {
    }
}
