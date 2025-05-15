package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo;

import java.util.Random;
import java.util.logging.Logger;

public class Zombie extends Thread {
    private static final Logger logger = Config_log.getLogger();

    private final int zonaInicial;
    private final Mapa mapa;

    public String getIde() {
        return id;
    }

    private final String id; //Z____
    Random random = new Random();
    private int kills = 0;


    public void sumarKills() {
        kills++;
    }


    public Zombie(String z0000, Mapa mapa, int zonaInicial) {
        this.id = z0000;
        this.mapa = mapa;
        this.zonaInicial = zonaInicial;
    }

    @Override
    public void run() {
        mapa.zonaRiesgo.zonas[zonaInicial].add(this);
        int nuevaZona = zonaInicial;
        while (!isInterrupted()) {
            try {
                mapa.verificarPausa();
                nuevaZona = mapa.cambiarDeZona(nuevaZona, this);
                dormir(random.nextInt(1000) + 1000);
                mapa.verificarPausa();
                mapa.atacar(this, nuevaZona);
                dormir(random.nextInt(1000));
            } catch (InterruptedException e) {
                logger.info("Excepcion en zombie:\n" + e.getMessage());
                interrupt();// Pa salir
            }
        }
    }


    public void dormir(int milis) {
        try {
            sleep(milis);
        } catch (InterruptedException e) {
            logger.info("Error en la función dormir en Zombie:\n"+e.getMessage());
        }
    }

    public int getNumeroKills() {
        return kills;
    }
}
