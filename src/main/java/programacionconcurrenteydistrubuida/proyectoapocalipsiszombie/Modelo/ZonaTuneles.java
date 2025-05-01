package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo;

import java.util.ArrayList;


public class ZonaTuneles {
    ArrayList<Humano>[] tuneles;
    ArrayList<Humano>[] zonasEspera;

    public ArrayList<Humano>[] getTuneles() {
        return tuneles;
    }

    public ArrayList<Humano>[] getZonasEspera() {
        return zonasEspera;
    }

    public ZonaTuneles() {
        zonasEspera = new ArrayList[4];
        zonasEspera[0] = new ArrayList<Humano>();
        zonasEspera[1] = new ArrayList<Humano>();
        zonasEspera[2] = new ArrayList<Humano>();
        zonasEspera[3] = new ArrayList<Humano>();
        tuneles = new ArrayList[4];
        tuneles[0] = new ArrayList<Humano>();
        tuneles[1] = new ArrayList<Humano>();
        tuneles[2] = new ArrayList<Humano>();
        tuneles[3] = new ArrayList<Humano>();
    }
}


