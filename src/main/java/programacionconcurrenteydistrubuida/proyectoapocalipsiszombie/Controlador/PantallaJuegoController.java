package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Controlador;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
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

    private ArrayList<Humano> lista_humanos=new ArrayList<Humano>();//lista humanos creada para luego hacer el reanudar
    public ArrayList<Zombie> lista_zombis=new ArrayList<Zombie>(); // Lista de hilos de zombis
    private boolean juegoPausado = false; // Bandera para pausar el juego

    public void setData(Mapa mapa){
        this.mapa = mapa;
        lista_humanos = new ArrayList<>();
        lista_zombis = new ArrayList<>();

    }


    public synchronized void pausarJuego() {
        juegoPausado = true;

        // Suspendemos todos los hilos de los humanos
        for (Humano humano : lista_humanos) {
            humano.suspender();
        }

        // Suspendemos todos los hilos de los zombis
        for (Zombie zombie : lista_zombis) {
            zombie.suspender();
        }
    }
    public void onBotonPausar(MouseEvent mouseEvent) {
        pausarJuego();
    }



    public synchronized void reanudarJuego() {
        juegoPausado = false;

        // Reanudamos todos los hilos de los humanos
        for (Humano humano : lista_humanos) {
            humano.reanudar();
        }

        // Reanudamos todos los hilos de los zombis
        for (Zombie zombie : lista_zombis) {
            zombie.reanudar();
        }
    }

    public void onBotonReanudar(ActionEvent actionEvent) {
        reanudarJuego();

    }


    public void onBotonReanudarHumano() {
        // Cuando se reanuda el juego, creas los hilos para los humanos
        for (int i = 0; i < 1; i++) {
            String id = String.format("H%04d", i);
            Humano humano = new Humano(mapa, id);
            lista_humanos.add(humano);
            humano.start(); // Inicia el hilo del humano
        }
    }


    public void onBotonReanudarZombie() {
        Zombie zombie = new Zombie("Z0000", mapa, 0);
        lista_zombis.add(zombie);
        zombie.start(); // Inicia el hilo del zombie
    }
    public void recorrerTodo(){

    }

    private void recorrerZonaRefugio(){

    }
    private void recorrerZonaTuneles() {

    }
    private void recorrerZonaRiesgo() {

    }
    public void onPintarIndividuos(ActionEvent actionEvent) {
    }

    public void onFinalizarButton(ActionEvent actionEvent) {
    }

    public void onBotonGuardar(ActionEvent actionEvent) {
    }
}
