package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Controlador;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo.Mapa;
import programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.VistaPrincipal;

public class VentanaPrincipalController {
    public Button botonIniciar;
    ////TERCERA VENTANA

    private Stage escenaPrincipal;

    public void setStage(Stage stage) {
        this.escenaPrincipal = stage;
    }

    public void botonIniciarClick() {
        Stage stageAntiguo = (Stage) botonIniciar.getScene().getWindow();
        stageAntiguo.close();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(VistaPrincipal.class.getResource("pantallaJuego.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            PantallaJuegoController pantallaJuegoController = fxmlLoader.getController();
            pantallaJuegoController.setData(new Mapa());//Creamos el mapa

            stage.setTitle("Pantalla Juego de Apocalipsis Zombie");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

       // Main.comenzarApocalipsis();
    }
}
