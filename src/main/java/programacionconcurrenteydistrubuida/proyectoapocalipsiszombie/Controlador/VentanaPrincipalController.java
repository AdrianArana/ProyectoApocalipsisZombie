package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Controlador;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo.Mapa;
import programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.VistaServidor;

public class VentanaPrincipalController {
    private Mapa mapa;
    public Button botonIniciar;

    private Stage escenaPrincipal;

    public void setStage(Stage stage) {
        this.escenaPrincipal = stage;
    }

    public void setMapa(Mapa mapa) {
        this.mapa = mapa;
    }
    public void botonIniciarClick() {
        Stage stageAntiguo = (Stage) botonIniciar.getScene().getWindow();
        stageAntiguo.close();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(VistaServidor.class.getResource("pantallaJuego.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
            PantallaJuegoController pantallaJuegoController = fxmlLoader.getController();

            stage.setTitle("Pantalla Juego de Apocalipsis Zombie");
            stage.setScene(scene);
            stage.show();
            PantallaJuegoController c = fxmlLoader.getController();
            c.setData(mapa);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
