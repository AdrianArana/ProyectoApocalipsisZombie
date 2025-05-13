package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Controlador.VistaClienteController;
import programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo.RMIInterfaz;
import programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo.RetornaValores;

import java.rmi.Naming;

public class VistaCliente extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(VistaCliente.class.getResource("vistaCliente.fxml"));
        stage.setTitle("Cliente Apocalipsis Zombie");
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
        try {
            // Localiza el objeto distribuido
            RMIInterfaz mapa = (RMIInterfaz) Naming.lookup("//127.0.0.1/ObjetoRetornaValores");
            VistaClienteController c = fxmlLoader.getController();
            c.setRetornaValores(mapa);
            c.iniciarActualizacionDeValores();
            if (!mapa.getIniciado()){
                System.out.println("Inicie la simulación para mostrar las estadísticas");
            }
        } catch (Exception e) {
            System.out.println("No se ha podido conectar al host, inicie primero el servidor.");
            System.exit(0);
        }
    }
}

