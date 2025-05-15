package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Controlador.VentanaInicialController;
import programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo.Mapa;
import programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo.RMIInterfaz;
import programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo.RetornaValores;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class VistaServidor extends Application {
    private Mapa mapa;

    @Override
    public void start(Stage stage) throws IOException {
        this.mapa = new Mapa();
        FXMLLoader fxmlLoader = new FXMLLoader(VistaServidor.class.getResource("ventanaInicial.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Apocalipsis Zombie");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
        VentanaInicialController c = fxmlLoader.getController();
        c.setMapa(mapa);
        try {
            RetornaValores mapaSimulacion = new RetornaValores(mapa) {
            };
            Registry registry = LocateRegistry.createRegistry(1099);
            Naming.rebind("//localHost/ObjetoRetornaValores", mapaSimulacion);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}