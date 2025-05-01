package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Controlador;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo.Humano;
import programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo.Mapa;
import programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo.Zombie;

import java.util.ArrayList;

public class PantallaJuegoController {
    private Mapa mapa;
    public Button botonPausar;
    public Label labelTurnoActual;
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

    private ArrayList<Humano> lista_humanos = new ArrayList<Humano>();//lista humanos creada para luego hacer el reanudar
    public ArrayList<Zombie> lista_zombis = new ArrayList<Zombie>(); // Lista de hilos de zombis
    // Bandera para pausar el juego
    private boolean empezado = false;

    public void setData(Mapa mapa) {
        this.mapa = mapa;
    }

    public synchronized void pausarJuego(){}
    /*public synchronized void pausarJuego() {
        // Suspendemos todos los hilos de los humanos
        for (Humano humano : lista_humanos) {
            humano.suspender();
        }
        // Suspendemos todos los hilos de los zombis
        for (Zombie zombie : lista_zombis) {
            zombie.suspender();
        }
    }
*/
    public void onBotonPausar(ActionEvent mouseEvent) {
        botonReanudar.setDisable(false);
        botonPausar.setDisable(true);
        pausarJuego();
    }


    public synchronized void reanudarJuego() {
        // Reanudamos todos los hilos de los humanos
        for (Humano humano : lista_humanos) {
            //humano.reanudar();
        }
        // Reanudamos todos los hilos de los zombis
        for (Zombie zombie : lista_zombis) {
            zombie.reanudar();
        }
    }

    public void onBotonReanudar(ActionEvent actionEvent) throws InterruptedException {
        botonReanudar.setDisable(true);
        botonPausar.setDisable(false);
        if (!empezado) {
            empezado = true;
            recorrerTodo();

            Zombie z = new Zombie("Z0000", mapa, 0);
            z.start();
            for (int i = 1; i < 4; i++) {
                Humano h = new Humano(mapa, String.format("H%04d", i));
                h.start();
                Thread.sleep(500);
            }
        } else {
            reanudarJuego();
        }
    }


    public void onBotonReanudarHumano() {
        // Cuando se reanuda el juego, creas los hilos para los humanos
        for (int i = 0; i < 1; i++) {
            String id = String.format("H%04d", i);
            Humano humano = new Humano(mapa, id);
            lista_humanos.add(humano);
            humano.start(); // Inicia el hilo del humano
        }

        Zombie zombie = new Zombie(("Z0000"), mapa, 0);
        zombie.start();
    }


    public void onBotonReanudarZombie() {
        Zombie zombie = new Zombie("Z0000", mapa, 0);
        lista_zombis.add(zombie);
        zombie.start(); // Inicia el hilo del zombie
    }

    private void recorrerTodo() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000); // Intervalo de 1 segundo entre ejecuciones
                    recorrerZonaRefugio();
                    recorrerZonaRiesgo();
                    recorrerZonaTuneles();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break; // Salir del bucle si el hilo es interrumpido
                }
            }
        }).start();
    }

    private void recorrerZonaRefugio() {
        ArrayList<String> listaIDesDescanso = new ArrayList<>();
        ArrayList<String> listaIDesComedor = new ArrayList<>();
        ArrayList<String> listaIDesZonaComun = new ArrayList<>();
        for (Humano humano : mapa.getZonaRefugio().getZonaDescanso()) {

            listaIDesDescanso.add(humano.getIde());
        }
        for (Humano humano : mapa.getZonaRefugio().getZonaComedor()) {
            listaIDesComedor.add(humano.getIde());
        }
        for (Humano humano : mapa.getZonaRefugio().getZonaComun()) {
            listaIDesZonaComun.add(humano.getIde());
        }
        txtComida.setText("" + mapa.getZonaRefugio().getAlmacen_comida());
        txtDescanso.setText(listaIDesDescanso.toString());
        txtComedor.setText(listaIDesComedor.toString());
        txtZonaComun.setText(listaIDesZonaComun.toString());

    }

    private void recorrerZonaTuneles() {
        ArrayList<String> listaGrupos0 = new ArrayList<>();
        ArrayList<String> listaGrupos1 = new ArrayList<>();
        ArrayList<String> listaGrupos2 = new ArrayList<>();
        ArrayList<String> listaGrupos3 = new ArrayList<>();

        ArrayList<String> listaIDesTunel0 = new ArrayList<>();
        ArrayList<String> listaIDesTunel1 = new ArrayList<>();
        ArrayList<String> listaIDesTunel2 = new ArrayList<>();
        ArrayList<String> listaIDesTunel3 = new ArrayList<>();
        for (Humano humano : mapa.getZonaTuneles().getTuneles()[0]) {
            listaIDesTunel0.add(humano.getIde());
        }
        for (Humano humano : mapa.getZonaTuneles().getTuneles()[1]) {
            listaIDesTunel1.add(humano.getIde());
        }
        for (Humano humano : mapa.getZonaTuneles().getTuneles()[2]) {
            listaIDesTunel2.add(humano.getIde());
        }
        for (Humano humano : mapa.getZonaTuneles().getTuneles()[3]) {
            listaIDesTunel3.add(humano.getIde());
        }
        textTunel11.setText(listaIDesTunel0.toString());
        textTunel12.setText(listaIDesTunel1.toString());
        textTunel13.setText(listaIDesTunel2.toString());
        textTunel14.setText(listaIDesTunel3.toString());

    }

    private void recorrerZonaRiesgo() {
        ArrayList<String> listaIDHRiesgo0 = new ArrayList<>();
        ArrayList<String> listaIDHRiesgo1 = new ArrayList<>();
        ArrayList<String> listaIDHRiesgo2 = new ArrayList<>();
        ArrayList<String> listaIDHRiesgo3 = new ArrayList<>();
        ArrayList<String> listaIDZRiesgo0 = new ArrayList<>();
        ArrayList<String> listaIDZRiesgo1 = new ArrayList<>();
        ArrayList<String> listaIDZRiesgo2 = new ArrayList<>();
        ArrayList<String> listaIDZRiesgo3 = new ArrayList<>();

        for (Thread thread : mapa.getZonaRiesgo().getZonas()[0]) {
            if (thread.getClass() == Humano.class) {
                listaIDHRiesgo0.add(((Humano) thread).getIde());
            } else {
                listaIDZRiesgo0.add(((Zombie) thread).getIde());
            }
        }
        for (Thread thread : mapa.getZonaRiesgo().getZonas()[1]) {
            if (thread.getClass() == Humano.class) {
                listaIDHRiesgo1.add(((Humano) thread).getIde());
            } else {
                listaIDZRiesgo1.add(((Zombie) thread).getIde());
            }
        }
        for (Thread thread : mapa.getZonaRiesgo().getZonas()[2]) {
            if (thread.getClass() == Humano.class) {
                listaIDHRiesgo2.add(((Humano) thread).getIde());
            } else {
                listaIDZRiesgo2.add(((Zombie) thread).getIde());
            }
        }
        for (Thread thread : mapa.getZonaRiesgo().getZonas()[3]) {
            if (thread.getClass() == Humano.class) {
                listaIDHRiesgo3.add(((Humano) thread).getIde());
            } else {
                listaIDZRiesgo3.add(((Zombie) thread).getIde());
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

    public void onPintarIndividuos(ActionEvent actionEvent) {
    }

    public void onFinalizarButton(ActionEvent actionEvent) {
    }

    public void onBotonGuardar(ActionEvent actionEvent) {
    }
}
