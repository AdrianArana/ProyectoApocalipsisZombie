package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class ZonaRiesgo {
    ArrayList<Thread>[] zonas = new ArrayList[4];

    ArrayList<Thread> zona1;
    ArrayList<Thread> zona2;
    ArrayList<Thread> zona3;
    ArrayList<Thread> zona4;

}
