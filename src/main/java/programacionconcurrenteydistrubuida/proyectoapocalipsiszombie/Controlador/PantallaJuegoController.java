package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo.Humano;
import programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo.Mapa;
import programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo.Zombie;
import programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.VistaPrincipal;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class PantallaJuegoController {
    public Button finalizarButton;
    public TextField txtNoHumanos;
    public TextField txtMejorZombie;
    public TextField txtNoZombies;
    public TextField txtMayorCantidadKills;
    public TextField txtTiempo;
    private Mapa mapa;
    public Button botonPausar;
    public Button botonReanudar;
    public TextArea txtDescanso;
    public TextArea txtComedor;
    public TextField txtComida;
    public TextArea txtZonaComun;
    public TextArea textTunel02;
    public TextArea textTunel12;
    public TextArea textTunel22;
    public TextArea textTunel03;
    public TextArea textTunel13;
    public TextArea textTunel23;
    public TextArea textTunel04;
    public TextArea textTunel14;
    public TextArea textTunel24;
    public TextArea textTunel11;
    public TextArea textTunel21;
    public TextArea textTunel01;
    public TextArea textRiesgo03;
    public TextArea textRiesgo13;
    public TextArea textRiesgo04;
    public TextArea textRiesgo14;
    public TextArea textRiesgo05;
    public TextArea textRiesgo15;
    public TextArea textRiesgo01;
    public TextArea textRiesgo11;


    // Bandera para pausar el juego
    private boolean empezado = false;

    public void setData(Mapa mapa) {
        this.mapa = mapa;
        mapa.setPausado(false);
        botonPausar.setDisable(true);
    }

    public void onBotonPausar(ActionEvent mouseEvent) {
        botonReanudar.setDisable(false);
        botonPausar.setDisable(true);
        mapa.pausarHilos();
    }


    public void onBotonReanudar(ActionEvent actionEvent) throws InterruptedException {
        botonReanudar.setDisable(true);
        botonReanudar.setText("Reanudar");
        botonPausar.setDisable(false);

        if (!empezado) {
            empezado = true;
            Zombie z = new Zombie("Z0000", mapa, 0);
            z.start();
            new Thread(() -> {
                for (int i = 1; i < 40; i++) {
                    mapa.sumarNumeroHumanos(1);
                    Humano h = new Humano(mapa, String.format("H%04d", i));
                    h.start();
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
            new Thread(() -> {
                int dost = 0;
                while (true) {
                    try {
                        mapa.verificarPausa();

                        sleep(500); // Intervalo de 0.5 segundos entre ejecuciones
                        //sleep(1000);
                        dost++;
                        txtTiempo.setText(""+((int)(dost/2)));
                        //txtTiempo.setText(""+dost);

                        recorrerZonaRefugio();
                        recorrerZonaRiesgo();
                        recorrerZonaTuneles();
                        txtNoZombies.setText("" + (mapa.getNumeroKills() + 1));
                        txtNoHumanos.setText("" + mapa.getNumeroHumanos());
                        txtMejorZombie.setText(mapa.getMejorZombie());
                        txtMayorCantidadKills.setText("" + mapa.getMayoresKills());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break; // Salir del bucle si el hilo es interrumpido
                    }
                }
            }).start();
        } else {
            mapa.reanudarHilos();
        }
    }


    private synchronized void recorrerZonaRefugio() {
        ArrayList<String> listaIDesDescanso = new ArrayList<>();
        ArrayList<String> listaIDesComedor = new ArrayList<>();
        ArrayList<String> listaIDesZonaComun = new ArrayList<>();

        if (!mapa.getZonaRefugio().getZonaDescanso().isEmpty()) {
            for (Humano humano : mapa.getZonaRefugio().getZonaDescanso()) {
                listaIDesDescanso.add(humano.getIde());
            }
        }
        if (!mapa.getZonaRefugio().getZonaComedor().isEmpty()) {
            for (Humano humano : mapa.getZonaRefugio().getZonaComedor()) {
                listaIDesComedor.add(humano.getIde());
            }
        }
        if (!mapa.getZonaRefugio().getZonaComun().isEmpty()) {
            for (Humano humano : mapa.getZonaRefugio().getZonaComun()) {
                assert humano != null;
                listaIDesZonaComun.add(humano.getIde());
            }
        }
        txtComida.setText("" + mapa.getZonaRefugio().getAlmacen_comida());
        txtDescanso.setText(listaIDesDescanso.toString());
        txtComedor.setText(listaIDesComedor.toString());
        txtZonaComun.setText(listaIDesZonaComun.toString());

    }

    private synchronized void recorrerZonaTuneles() {
        ArrayList<String> listaGrupos0 = new ArrayList<>();
        ArrayList<String> listaGrupos1 = new ArrayList<>();
        ArrayList<String> listaGrupos2 = new ArrayList<>();
        ArrayList<String> listaGrupos3 = new ArrayList<>();

        ArrayList<String> listaIDesTunel0 = new ArrayList<>();
        ArrayList<String> listaIDesTunel1 = new ArrayList<>();
        ArrayList<String> listaIDesTunel2 = new ArrayList<>();
        ArrayList<String> listaIDesTunel3 = new ArrayList<>();

        if (!mapa.getZonaTuneles().getZonasEspera()[0].isEmpty()) {
            for (Humano humano : mapa.getZonaTuneles().getZonasEspera()[0]) {
                listaGrupos0.add(humano.getIde());
            }
        }
        if (!mapa.getZonaTuneles().getZonasEspera()[1].isEmpty()) {
            for (Humano humano : mapa.getZonaTuneles().getZonasEspera()[1]) {
                listaGrupos1.add(humano.getIde());
            }
        }
        if (!mapa.getZonaTuneles().getZonasEspera()[2].isEmpty()) {
            for (Humano humano : mapa.getZonaTuneles().getZonasEspera()[2]) {
                listaGrupos2.add(humano.getIde());
            }
        }
        if (!mapa.getZonaTuneles().getZonasEspera()[3].isEmpty()) {
            for (Humano humano : mapa.getZonaTuneles().getZonasEspera()[3]) {
                listaGrupos3.add(humano.getIde());
            }
        }

        if (!mapa.getZonaTuneles().getTuneles()[0].isEmpty()) {
            for (Humano humano : mapa.getZonaTuneles().getTuneles()[0]) {
                listaIDesTunel0.add(humano.getIde());
            }
        }
        if (!mapa.getZonaTuneles().getTuneles()[1].isEmpty()) {

            for (Humano humano : mapa.getZonaTuneles().getTuneles()[1]) {
                listaIDesTunel1.add(humano.getIde());
            }
        }
        if (!mapa.getZonaTuneles().getTuneles()[2].isEmpty()) {

            for (Humano humano : mapa.getZonaTuneles().getTuneles()[2]) {
                listaIDesTunel2.add(humano.getIde());
            }
        }
        if (!mapa.getZonaTuneles().getTuneles()[3].isEmpty()) {

            for (Humano humano : mapa.getZonaTuneles().getTuneles()[3]) {
                listaIDesTunel3.add(humano.getIde());
            }
        }
        textTunel11.setText(listaIDesTunel0.toString());
        textTunel12.setText(listaIDesTunel1.toString());
        textTunel13.setText(listaIDesTunel2.toString());
        textTunel14.setText(listaIDesTunel3.toString());

        textTunel01.setText(listaGrupos0.toString());
        textTunel02.setText(listaGrupos1.toString());
        textTunel03.setText(listaGrupos2.toString());
        textTunel04.setText(listaGrupos3.toString());
    }

    private synchronized void recorrerZonaRiesgo() {
        ArrayList<String> listaIDHRiesgo0 = new ArrayList<>();
        ArrayList<String> listaIDHRiesgo1 = new ArrayList<>();
        ArrayList<String> listaIDHRiesgo2 = new ArrayList<>();
        ArrayList<String> listaIDHRiesgo3 = new ArrayList<>();
        ArrayList<String> listaIDZRiesgo0 = new ArrayList<>();
        ArrayList<String> listaIDZRiesgo1 = new ArrayList<>();
        ArrayList<String> listaIDZRiesgo2 = new ArrayList<>();
        ArrayList<String> listaIDZRiesgo3 = new ArrayList<>();
        if (!mapa.getZonaRiesgo().getZonas()[0].isEmpty()) {
            for (Thread thread : mapa.getZonaRiesgo().getZonas()[0]) {
                if (thread.getClass() == Humano.class) {
                    listaIDHRiesgo0.add(((Humano) thread).getIde());
                } else {
                    listaIDZRiesgo0.add(((Zombie) thread).getIde());
                }
            }
        }
        if (!mapa.getZonaRiesgo().getZonas()[1].isEmpty()) {
            for (Thread thread : mapa.getZonaRiesgo().getZonas()[1]) {
                if (thread.getClass() == Humano.class) {
                    listaIDHRiesgo1.add(((Humano) thread).getIde());
                } else {
                    listaIDZRiesgo1.add(((Zombie) thread).getIde());
                }
            }
        }
        if (!mapa.getZonaRiesgo().getZonas()[2].isEmpty()) {
            for (Thread thread : mapa.getZonaRiesgo().getZonas()[2]) {
                if (thread.getClass() == Humano.class) {
                    listaIDHRiesgo2.add(((Humano) thread).getIde());
                } else {
                    listaIDZRiesgo2.add(((Zombie) thread).getIde());
                }
            }
        }
        if (!mapa.getZonaRiesgo().getZonas()[3].isEmpty()) {
            for (Thread thread : mapa.getZonaRiesgo().getZonas()[3]) {

                if (thread.getClass() == Humano.class) {
                    listaIDHRiesgo3.add(((Humano) thread).getIde());
                } else {
                    listaIDZRiesgo3.add(((Zombie) thread).getIde());
                }
            }
        }
        textRiesgo01.setText(listaIDHRiesgo0.toString());
        textRiesgo03.setText(listaIDHRiesgo1.toString());
        textRiesgo04.setText(listaIDHRiesgo2.toString());
        textRiesgo05.setText(listaIDHRiesgo3.toString());
        textRiesgo11.setText(listaIDZRiesgo0.toString());
        textRiesgo13.setText(listaIDZRiesgo1.toString());
        textRiesgo14.setText(listaIDZRiesgo2.toString());
        textRiesgo15.setText(listaIDZRiesgo3.toString());
    }


    public void pararTodo() {
        System.exit(0);        //todo
    }

    public void onFinalizarButton(ActionEvent actionEvent) {

        pararTodo();


        Stage stageAntiguo = (Stage) finalizarButton.getScene().getWindow();
        stageAntiguo.close();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(VistaPrincipal.class.getResource("finJuego.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            stage.setTitle("Apocalipsis Zombie ACABADO");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
