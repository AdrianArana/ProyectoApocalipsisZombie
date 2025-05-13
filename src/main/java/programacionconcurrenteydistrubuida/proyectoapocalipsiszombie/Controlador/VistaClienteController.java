package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Controlador;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo.RMIInterfaz;
import programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo.Zombie;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class VistaClienteController {
    public TextField humanosTunelesText0;
    public TextField humanosAreasInsegurasText0;
    public TextField zombisAreasInsegurasText0;
    public TextField zombisAreasInsegurasText2;
    public TextField zombisAreasInsegurasText1;
    public TextField zombisAreasInsegurasText3;
    public TextField humanosAreasInsegurasText1;
    public TextField humanosAreasInsegurasText2;
    public TextField humanosAreasInsegurasText3;
    public TextField humanosTunelesText1;
    public TextField humanosTunelesText2;
    public TextField humanosTunelesText3;
    private boolean parado = false;
    public TextField humanosRefugioText;
    public TextArea rankingZombisText;
    public Button ejecucionButton;
    private RMIInterfaz retornaValores;

    public void iniciarActualizacionDeValores() {
        // Hilo que actualiza estadísticas cada segundo
        Thread actualizadorEstadisticas = new Thread(() -> {
            while (true) {
                try {
                    Platform.runLater(() -> {
                        try {
                            humanosTunelesText0.setText(String.valueOf(retornaValores.getNumeroHumanosTunel(0)));
                            humanosTunelesText1.setText(String.valueOf(retornaValores.getNumeroHumanosTunel(1)));
                            humanosTunelesText2.setText(String.valueOf(retornaValores.getNumeroHumanosTunel(2)));
                            humanosTunelesText3.setText(String.valueOf(retornaValores.getNumeroHumanosTunel(3)));

                            zombisAreasInsegurasText0.setText(String.valueOf(retornaValores.getNumeroZombiesZonaRiesgo(0)));
                            zombisAreasInsegurasText1.setText(String.valueOf(retornaValores.getNumeroZombiesZonaRiesgo(1)));
                            zombisAreasInsegurasText2.setText(String.valueOf(retornaValores.getNumeroZombiesZonaRiesgo(2)));
                            zombisAreasInsegurasText3.setText(String.valueOf(retornaValores.getNumeroZombiesZonaRiesgo(3)));

                            humanosAreasInsegurasText0.setText(String.valueOf(retornaValores.getNumeroHumanosZonaRiesgo(0)));
                            humanosAreasInsegurasText1.setText(String.valueOf(retornaValores.getNumeroHumanosZonaRiesgo(1)));
                            humanosAreasInsegurasText2.setText(String.valueOf(retornaValores.getNumeroHumanosZonaRiesgo(2)));
                            humanosAreasInsegurasText3.setText(String.valueOf(retornaValores.getNumeroHumanosZonaRiesgo(3)));

                            humanosRefugioText.setText(String.valueOf(retornaValores.getNumeroHumanosRefugio()));

                            rankingZombisText.setText(retornaValores.getMejoresZombies());

                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    });
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        actualizadorEstadisticas.start();

    }

    public void onEjecucionButtonClick(ActionEvent actionEvent) throws RemoteException {
        parado = !parado; //Alternamos entre valores
        retornaValores.setParado(parado);
    }

    public void setRetornaValores(RMIInterfaz mapa) {
        this.retornaValores = mapa;
    }
}