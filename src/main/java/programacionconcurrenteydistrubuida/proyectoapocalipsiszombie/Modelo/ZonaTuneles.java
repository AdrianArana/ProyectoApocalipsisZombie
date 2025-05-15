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
        zonasEsperaIda[0] = new ArrayList<>();
        zonasEsperaIda[1] = new ArrayList<>();
        zonasEsperaIda[2] = new ArrayList<>();
        zonasEsperaIda[3] = new ArrayList<>();
        zonasEsperaVuelta = new ArrayList[4];
        zonasEsperaVuelta[0] = new ArrayList<>();
        zonasEsperaVuelta[1] = new ArrayList<>();
        zonasEsperaVuelta[2] = new ArrayList<>();
        zonasEsperaVuelta[3] = new ArrayList<>();
        tuneles = new ArrayList[4];
        tuneles[0] = new ArrayList<>();
        tuneles[1] = new ArrayList<>();
        tuneles[2] = new ArrayList<>();
        tuneles[3] = new ArrayList<>();
    }
}


