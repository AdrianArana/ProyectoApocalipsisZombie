package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class ZonaRiesgo {
    public ArrayList<Thread>[] getZonas() {
        return zonas;
    }

    ArrayList<Thread>[] zonas = new ArrayList[4];
    public ZonaRiesgo() {
        zonas[0] = new ArrayList<Thread>();
        zonas[1] = new ArrayList<Thread>();
        zonas[2] = new ArrayList<Thread>();
        zonas[3] = new ArrayList<Thread>();

    }



}
