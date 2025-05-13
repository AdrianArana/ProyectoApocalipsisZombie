package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo;

import java.util.ArrayList;


public class ZonaTuneles {
    ArrayList<Humano>[] tuneles;
    ArrayList<Humano>[] zonasEsperaIda;
    ArrayList<Humano>[] zonasEsperaVuelta;

    public ArrayList<Humano>[] getTuneles() {
        return tuneles;
    }

    public ArrayList<Humano>[] getZonasEsperaIda() {
        return zonasEsperaIda;
    }

    public ArrayList<Humano>[] getZonasEsperaVuelta() {
        return zonasEsperaVuelta;
    }

    public ZonaTuneles() {
        zonasEsperaIda = new ArrayList[4];
        zonasEsperaIda[0] = new ArrayList<Humano>();
        zonasEsperaIda[1] = new ArrayList<Humano>();
        zonasEsperaIda[2] = new ArrayList<Humano>();
        zonasEsperaIda[3] = new ArrayList<Humano>();
        zonasEsperaVuelta = new ArrayList[4];
        zonasEsperaVuelta[0] = new ArrayList<Humano>();
        zonasEsperaVuelta[1] = new ArrayList<Humano>();
        zonasEsperaVuelta[2] = new ArrayList<Humano>();
        zonasEsperaVuelta[3] = new ArrayList<Humano>();
        tuneles = new ArrayList[4];
        tuneles[0] = new ArrayList<Humano>();
        tuneles[1] = new ArrayList<Humano>();
        tuneles[2] = new ArrayList<Humano>();
        tuneles[3] = new ArrayList<Humano>();
    }
}


