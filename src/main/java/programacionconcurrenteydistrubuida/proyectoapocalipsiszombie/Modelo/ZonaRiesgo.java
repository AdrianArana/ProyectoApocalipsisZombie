package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class ZonaRiesgo {
    ArrayList<Thread>[] zonas;
    public ZonaRiesgo() {
        this.zonas = new ArrayList[4];
        zonas[0] = new ArrayList<Thread>();
        zonas[1] = new ArrayList<Thread>();
        zonas[2] = new ArrayList<Thread>();
        zonas[3] = new ArrayList<Thread>();

    }

}
