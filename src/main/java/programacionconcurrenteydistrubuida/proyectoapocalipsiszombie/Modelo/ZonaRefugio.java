package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ZonaRefugio {
    ArrayList<Humano> zonaDescanso;
    ArrayList<Humano> zonaComedor;
    ArrayList<Humano> zonaComun;

    public ZonaRefugio() {
        this.zonaDescanso= new ArrayList<Humano>();
        this.zonaComedor=new ArrayList<Humano>();
        this.zonaComun=new ArrayList<Humano>();
    }

    public ArrayList<Humano> getZonaDescanso() {
        return zonaDescanso;
    }

    public void setZonaDescanso(ArrayList<Humano> zonaDescanso) {
        this.zonaDescanso = zonaDescanso;
    }

    public ArrayList<Humano> getZonaComedor() {
        return zonaComedor;
    }

    public void setZonaComedor(ArrayList<Humano> zonaComedor) {
        this.zonaComedor = zonaComedor;
    }

    public ArrayList<Humano> getZonaComun() {
        return zonaComun;
    }

    public void setZonaComun(ArrayList<Humano> zonaComun) {
        this.zonaComun = zonaComun;
    }

    private int almacen_comida=0;

    public synchronized int getAlmacen_comida() {
        return almacen_comida;
    }

    public synchronized void addComida(int almacen_comida) {
        this.almacen_comida += almacen_comida;
    }

    public synchronized void takeComida() {
        this.almacen_comida --;
    }
}
