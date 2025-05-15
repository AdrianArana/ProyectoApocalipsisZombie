package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo.Mapa;
import programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.VistaServidor;

public class VentanaInicialController {
    public Button iniciarPartidaButton;
    private Mapa mapa;
    public void iniciarPartidaButtonClick(ActionEvent actionEvent) {
        Stage stageAntiguo = (Stage) iniciarPartidaButton.getScene().getWindow();
        stageAntiguo.close();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(VistaServidor.class.getResource("nombre.fxml"));

        try {
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            stage.setTitle("Apocalipsis Zombie");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();
            NombreController c = fxmlLoader.getController();
            c.setMapa(mapa);

        } catch (Exception e) {
            System.out.println("Error en el boton que inicia la ventana nombre\n" + e.getMessage());
        }

    }

    public void setMapa(Mapa mapa) {
        this.mapa = mapa;
    }
    public void onCreditosButtonClick() {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(VistaServidor.class.getResource("creditos.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            stage.setTitle("Creditos de Apocalipsis Zombie");
            stage.setScene(scene);
            stage.setResizable(false); // Opcional
            stage.centerOnScreen();
            stage.show();

        } catch (Exception e) {
            System.out.println("Error en el boton de creditos en la primera ventana del servidor\n" + e.getMessage());
        }

    }
}