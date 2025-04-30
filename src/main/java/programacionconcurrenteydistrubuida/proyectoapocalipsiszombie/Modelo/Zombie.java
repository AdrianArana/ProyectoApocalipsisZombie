package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo;

import java.util.Random;

public class Zombie extends Thread {
    private final int zonaInicial;
    private Mapa mapa;
    private String id; //Z____
    Random random=new Random();
    private int kills=0;


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
                Thread.sleep(random.nextInt(1000) + 2000);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
