package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ZonaRefugio {
    ArrayList<Thread> zonaDescanso;
    ArrayList<Thread> zonaComedor;
    ArrayList<Thread> zonaComun;

    private int amacen_comida;

    public synchronized int getAlmacen_comida() {
        return amacen_comida;
    }

    public void setAmacen_comida(int amacen_comida) {
        this.amacen_comida = amacen_comida;
    }

    public synchronized void addComida(int almacen_comida) {
        this.amacen_comida += almacen_comida;
    }

    public synchronized void takeComida(int almacen_comida) {
        this.amacen_comida -= almacen_comida;
    }
}
