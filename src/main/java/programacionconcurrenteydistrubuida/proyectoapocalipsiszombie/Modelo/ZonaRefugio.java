package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo;

import java.util.ArrayList;

public class ZonaRefugio {
    ArrayList<Humano> zonaDescanso;
    ArrayList<Humano> zonaComedor;
    ArrayList<Humano> zonaComun;

    public ZonaRefugio() {
        this.zonaDescanso = new ArrayList<>();
        this.zonaComedor = new ArrayList<>();
        this.zonaComun = new ArrayList<>();
    }

    public ArrayList<Humano> getZonaDescanso() {
        return zonaDescanso;
    }

    public ArrayList<Humano> getZonaComedor() {
        return zonaComedor;
    }

    public ArrayList<Humano> getZonaComun() {
        return zonaComun;
    }

    private int almacen_comida = 0;

    public synchronized int getAlmacen_comida() {
        return almacen_comida;
    }

    public synchronized void addComida(int almacen_comida) {
        this.almacen_comida += almacen_comida;
    }

    public synchronized void takeComida() {
        this.almacen_comida--;
    }

    public synchronized int getNumeroHumanos() {
        int total = 0;
        total += zonaDescanso.size();
        total += zonaComedor.size();
        total += zonaComun.size();
        return total;
    }
}
