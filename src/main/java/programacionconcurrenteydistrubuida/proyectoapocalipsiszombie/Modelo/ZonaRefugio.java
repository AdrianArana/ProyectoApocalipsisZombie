package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ZonaRefugio {
    ArrayList<Thread> zonaDescanso;
    ArrayList<Thread> zonaComedor;
    ArrayList<Thread> zonaComun;

    public ZonaRefugio() {
        this.zonaDescanso= new ArrayList<Thread>();
        this.zonaComedor=new ArrayList<Thread>();
        this.zonaComun=new ArrayList<Thread>();
    }

    private int almacen_comida=0;

    public synchronized int getAlmacen_comida() {
        return almacen_comida;
    }

    public synchronized void addComida(int almacen_comida) {
        this.almacen_comida += almacen_comida;
    }

    public synchronized void takeComida(int almacen_comida) {
        this.almacen_comida -= almacen_comida;
    }
}
