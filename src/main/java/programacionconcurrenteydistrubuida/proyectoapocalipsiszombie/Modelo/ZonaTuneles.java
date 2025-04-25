package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo;

import java.util.ArrayList;


public class ZonaTuneles {
    ArrayList<Thread>[] tuneles;
    private ArrayList<Thread> tunel1 = new ArrayList<Thread>();
    private ArrayList<Thread> tunel2 = new ArrayList<Thread>();
    private ArrayList<Thread> tunel3 = new ArrayList<Thread>();
    private ArrayList<Thread> tunel4 = new ArrayList<Thread>();
    public ZonaTuneles(){
        tuneles[0] = tunel1;
        tuneles[0] = tunel2;
        tuneles[0] = tunel3;
        tuneles[0] = tunel4;

    }
}


