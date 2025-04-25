package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo;

import java.util.ArrayList;


public class ZonaTuneles {
    ArrayList<Humano>[] tuneles;

    public ZonaTuneles(){
        ArrayList<Humano>[] tuneles = new ArrayList[4];
        tuneles[0]=new ArrayList<Humano>();
        tuneles[1]=new ArrayList<Humano>();
        tuneles[2]=new ArrayList<Humano>();
        tuneles[3]=new ArrayList<Humano>();

    }
}


