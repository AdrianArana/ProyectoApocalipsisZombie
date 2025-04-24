package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Controlador;

import javafx.event.ActionEvent;
import javafx.stage.Stage;
import programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo.Main;
import programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo.Main.*;

public class VentanaPrincipalController {////TERCERA VENTANA

    private Stage escenaPrincipal;

    public void setStage(Stage stage) {
        this.escenaPrincipal = stage;
    }

    public void botonIniciarClick(ActionEvent actionEvent) {
        Main.comenzarApocalipsis();
    }
}
