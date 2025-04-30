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

    private ArrayList<Humano> lista_humanos;//lista humanos creada para luego hacer el reanudar

    public void setData(Mapa mapa){
        this.mapa = mapa;

    }
    public void onBotonPausar(MouseEvent mouseEvent) {
    }

    public void onBotonReanudar(ActionEvent actionEvent) {
        for (int i = 0; i < 1; i++) {
            String id = String.format("H%04d", i);
            System.out.println(id);
            Humano humano = new Humano(mapa,id);
            lista_humanos.add(humano);
            humano.start();
        }
        Zombie zombie = new Zombie(("Z0000"),mapa,0);
        zombie.start();    }

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
