package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo;

import java.util.ArrayList;

public class ZonaRiesgo {
    public ArrayList<Thread>[] getZonas() {
        return zonas;
    }

    ArrayList<Thread>[] zonas = new ArrayList[4];
    public ZonaRiesgo() {
        zonas[0] = new ArrayList<>();
        zonas[1] = new ArrayList<>();
        zonas[2] = new ArrayList<>();
        zonas[3] = new ArrayList<>();
    }
}
