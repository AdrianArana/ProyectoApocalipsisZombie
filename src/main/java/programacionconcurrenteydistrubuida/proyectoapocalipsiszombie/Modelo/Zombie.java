package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo;

import java.util.Random;

public class Zombie extends Thread {
    private final int zonaInicial;
    private Mapa mapa;

    public String getIde() {
        return id;
    }

    private String id; //Z____
    Random random=new Random();
    private int kills=0;
    private boolean enPausa = false;
    private final Object lock = new Object(); // Objeto para sincronización
// Control de pausa



    public void sumarKills(){
        kills++;
    }


    public Zombie(String z0000, Mapa mapa, int zonaInicial) {
        this.id = z0000;
        this.mapa = mapa;
        this.zonaInicial = zonaInicial;
    }

    @Override
    public void run() {
        mapa.zonaRiesgo.zonas[zonaInicial].add(this); ///cuando construimos el zombie le pasamos una zona,
                                                        /// se usa por ejemplo en la funcion de atarcar en Mapa
        int nuevaZona = zonaInicial;
        while ( true) {
            try {
                nuevaZona = mapa.cambiarDeZona(nuevaZona,this);
                mapa.atacar(this,nuevaZona);
                verificarPausa();

                Thread.sleep(random.nextInt(1000) );//le hemos bajao pa que ataque frecuentemente todo

                verificarPausa();
            } catch (InterruptedException e) {
                System.out.println("HAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            }
        }
    }

    public synchronized void suspender() {
        enPausa = true;  // Marca al zombie como pausado
    }

    public synchronized void reanudar() {
        enPausa = false;  // Marca al zombie como no pausado
        synchronized (lock) {
            lock.notify();  // Notifica al zombie para que reanude su ejecución
        }
    }

    private void verificarPausa() throws InterruptedException {
        synchronized (lock) {
            while (enPausa) {  // Si está pausado, espera
                lock.wait();
            }
        }
    }

}
