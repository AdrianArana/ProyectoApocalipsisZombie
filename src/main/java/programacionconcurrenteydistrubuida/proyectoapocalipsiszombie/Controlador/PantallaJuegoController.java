package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Controlador;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo.Humano;
import programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo.Mapa;
import programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo.Zombie;
import programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.VistaServidor;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import static java.lang.Thread.sleep;

public class PantallaJuegoController {
    public Button finalizarButton;
    public TextField txtTiempo;
    private Mapa mapa;
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
    public void setData(Mapa mapa) {
        this.mapa = mapa;
        txtTiempo.setText("0");
        mapa.setPausado(false);
    }


    public void onBotonReanudar(ActionEvent actionEvent) throws InterruptedException {
        botonReanudar.setDisable(true);
        botonReanudar.setText("");
        Zombie z = new Zombie("Z0000", mapa, 0);
        z.start();
        new Thread(() -> {
            mapa.setIniciado(true);
            for (int i = 1; i < 10000; i++) {
                mapa.verificarPausa();
                Humano h = new Humano(mapa, String.format("H%04d", i));
                h.start();
                try {
                    sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        Thread actualizarGraficos = new Thread(() -> {
            while (true) {
                try {
                    Platform.runLater(() -> {
                        try {
                            mapa.verificarPausa();

                            // Actualizar el tiempo
                            txtTiempo.setText("" + (Integer.parseInt(txtTiempo.getText()) + 1));

                            // Actualizar las zonas
                            recorrerZonaRefugio();
                            recorrerZonaRiesgo();
                            recorrerZonaTuneles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        actualizarGraficos.start();
            /*
            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

            scheduler.scheduleAtFixedRate(() -> {
                try {
                    mapa.verificarPausa();

                    // Actualizar el tiempo
                    txtTiempo.setText("" + (Integer.parseInt(txtTiempo.getText()) + 1));

                    // Actualizar las zonas
                    recorrerZonaRefugio();
                    recorrerZonaRiesgo();
                    recorrerZonaTuneles();
                } catch (Exception e) {
                    e.printStackTrace();
                    scheduler.shutdown();
                }
            }, 0, 1, TimeUnit.SECONDS);*/

    }


    private synchronized void recorrerZonaRefugio() {
        ArrayList<String> listaIDesDescanso = new ArrayList<>();
        ArrayList<String> listaIDesComedor = new ArrayList<>();
        ArrayList<String> listaIDesZonaComun = new ArrayList<>();

        if (!mapa.getZonaRefugio().getZonaDescanso().isEmpty()) {
            try {
                for (Humano humano : mapa.getZonaRefugio().getZonaDescanso()) {
                    listaIDesDescanso.add(humano.getIde());
                }
            } catch (NullPointerException e) {
            } catch (ConcurrentModificationException ex) {
            }
        }
        if (!mapa.getZonaRefugio().getZonaComedor().isEmpty()) {
            try {
                for (Humano humano : mapa.getZonaRefugio().getZonaComedor()) {
                    listaIDesComedor.add(humano.getIde());
                }
            } catch (NullPointerException e) {
            } catch (ConcurrentModificationException ex) {
            }
        }
        if (!mapa.getZonaRefugio().getZonaComun().isEmpty()) {
            try {
                for (Humano humano : mapa.getZonaRefugio().getZonaComun()) {
                    listaIDesZonaComun.add(humano.getIde());
                }
            } catch (NullPointerException e) {
            } catch (ConcurrentModificationException ex) {
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

        ArrayList<String> listaGruposVuelta0 = new ArrayList<>();
        ArrayList<String> listaGruposVuelta1 = new ArrayList<>();
        ArrayList<String> listaGruposVuelta2 = new ArrayList<>();
        ArrayList<String> listaGruposVuelta3 = new ArrayList<>();

        if (!mapa.getZonaTuneles().getZonasEsperaVuelta()[0].isEmpty()) {
            for (Humano humano : mapa.getZonaTuneles().getZonasEsperaVuelta()[0]) {
                listaGruposVuelta0.add(humano.getIde());
            }
        }
        if (!mapa.getZonaTuneles().getZonasEsperaVuelta()[1].isEmpty()) {
            for (Humano humano : mapa.getZonaTuneles().getZonasEsperaVuelta()[1]) {
                listaGruposVuelta1.add(humano.getIde());
            }
        }
        if (!mapa.getZonaTuneles().getZonasEsperaVuelta()[2].isEmpty()) {
            for (Humano humano : mapa.getZonaTuneles().getZonasEsperaVuelta()[2]) {
                listaGruposVuelta2.add(humano.getIde());
            }
        }
        if (!mapa.getZonaTuneles().getZonasEsperaVuelta()[3].isEmpty()) {
            for (Humano humano : mapa.getZonaTuneles().getZonasEsperaVuelta()[3]) {
                listaGruposVuelta3.add(humano.getIde());
            }
        }


        if (!mapa.getZonaTuneles().getZonasEsperaIda()[0].isEmpty()) {
            for (Humano humano : mapa.getZonaTuneles().getZonasEsperaIda()[0]) {
                listaGrupos0.add(humano.getIde());
            }
        }
        if (!mapa.getZonaTuneles().getZonasEsperaIda()[1].isEmpty()) {
            for (Humano humano : mapa.getZonaTuneles().getZonasEsperaIda()[1]) {
                listaGrupos1.add(humano.getIde());
            }
        }
        if (!mapa.getZonaTuneles().getZonasEsperaIda()[2].isEmpty()) {
            for (Humano humano : mapa.getZonaTuneles().getZonasEsperaIda()[2]) {
                listaGrupos2.add(humano.getIde());
            }
        }
        if (!mapa.getZonaTuneles().getZonasEsperaIda()[3].isEmpty()) {
            for (Humano humano : mapa.getZonaTuneles().getZonasEsperaIda()[3]) {
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

        textTunel21.setText(listaGruposVuelta0.toString());
        textTunel22.setText(listaGruposVuelta1.toString());
        textTunel23.setText(listaGruposVuelta2.toString());
        textTunel24.setText(listaGruposVuelta3.toString());

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
                if (thread instanceof Humano) {
                    listaIDHRiesgo0.add(((Humano) thread).getIde());
                } else {
                    listaIDZRiesgo0.add(((Zombie) thread).getIde());
                }
            }
        }
        if (!mapa.getZonaRiesgo().getZonas()[1].isEmpty()) {
            for (Thread thread : mapa.getZonaRiesgo().getZonas()[1]) {
                if (thread instanceof Humano) {
                    listaIDHRiesgo1.add(((Humano) thread).getIde());
                } else {
                    listaIDZRiesgo1.add(((Zombie) thread).getIde());
                }
            }
        }
        if (!mapa.getZonaRiesgo().getZonas()[2].isEmpty()) {
            for (Thread thread : mapa.getZonaRiesgo().getZonas()[2]) {
                if (thread instanceof Humano) {
                    listaIDHRiesgo2.add(((Humano) thread).getIde());
                } else {
                    listaIDZRiesgo2.add(((Zombie) thread).getIde());
                }
            }
        }
        if (!mapa.getZonaRiesgo().getZonas()[3].isEmpty()) {
            try {
                for (Thread thread : mapa.getZonaRiesgo().getZonas()[3]) {

                    if (thread instanceof Humano) {
                        listaIDHRiesgo3.add(((Humano) thread).getIde());
                    } else {
                        listaIDZRiesgo3.add(((Zombie) thread).getIde());
                    }
                }
            } catch (Exception e) {
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
        System.exit(0);
    }

    public void onFinalizarButton(ActionEvent actionEvent) {

        pararTodo();


        Stage stageAntiguo = (Stage) finalizarButton.getScene().getWindow();
        stageAntiguo.close();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(VistaServidor.class.getResource("finJuego.fxml"));
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
