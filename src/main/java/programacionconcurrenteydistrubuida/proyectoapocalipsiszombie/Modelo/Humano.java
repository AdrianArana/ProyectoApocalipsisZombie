package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;

public class Humano extends Thread {
    private String id;
    private int comida;//H____
    Mapa mapa;
    private boolean marcado;
    Random random = new Random();
    private boolean siendoAtacado = false;
    private boolean muerte = false;



    public void morir(boolean muerto) {
        this.muerte = muerto;
    }

    public Humano(Mapa mapa, String id) {
        this.mapa = mapa;
        this.id = id;
        this.comida = 0;
    }

    public String getIde() {

        return id;
    }

    @Override
    public void run() {
        mapa.zonaRefugio.zonaComun.add(this);
        while (true) {
            //Generados en la zona comun
            try {
                int tunelElegido = (int) (Math.random() * 4);
                mapa.verificarPausa();
                mapa.pasarTunelIda(tunelElegido, this);// Aqui se hace la espera
                // para entrar al tunel, y después de entrar, se elimina de la zona comun
                mapa.verificarPausa();
                mapa.entrarZonaRiesgo(tunelElegido, this);
                mapa.verificarPausa();
                dormir(3000 + (int) (Math.random() * 2000));//Tiempo en la zona de riesgo
                if (muerte) {

                    break;
                } else {
                    if (marcado) {
                        marcado = false;
                        mapa.entrarZonaDescanso(this);
                        dormir(random.nextInt(2000) + 2000);//Descansa 2-4 segundos
                        mapa.verificarPausa();
                        mapa.salirZonaDescanso(this);
                        mapa.verificarPausa();
                        mapa.entrarZonaComedor(this);
                        mapa.verificarPausa();
                        dormir(3000 + (int) (Math.random() * 2000));//Come durante 3-5 segundos
                        mapa.verificarPausa();

                        mapa.salirZonaComedor(this);
                        mapa.verificarPausa();
                        mapa.entrarZonaDescanso(this);
                        mapa.verificarPausa();
                        dormir(3000 + (int) (Math.random() * 2000));//Descanso extra de 3 a 5 segundos
                        mapa.verificarPausa();
                        mapa.salirZonaDescanso(this);
                    } else {
                        mapa.verificarPausa();
                        mapa.pasarTunelVuelta(tunelElegido, this);
                        mapa.verificarPausa();
                        mapa.entrarZonaDescanso(this);
                        dormir(random.nextInt(2000) + 2000);//Descansa 2-4 segundos
                        mapa.verificarPausa();
                        mapa.salirZonaDescanso(this);
                        mapa.verificarPausa();

                        mapa.entrarZonaComedor(this);
                        dormir(3000 + (int) (Math.random() * 2000));//Come durante 3-5 segundos
                        mapa.verificarPausa();

                        mapa.salirZonaComedor(this);
                        mapa.verificarPausa();
                    }
                    mapa.entrarZonaComun(this);
                    dormir(2000);
                }

            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                System.out.printf("El humano con id: " + id + " ha sido convertido en zombie\n");
            }
        }
        mapa.borrarHumanodelRiesgo(this);
    }


    public int getComida() {
        return comida;
    }

    public void setComida(int comida) {
        this.comida = comida;
    }


    public void marcarHumano() {
        this.marcado = true;
    }

    public boolean getSiendoAtacado() {
        return this.siendoAtacado;
    }

    public synchronized void setSiendoAtacado(boolean siendoAtacado) {//Synchronized para que no puedan atacarle 2 zombies a la vez
        this.siendoAtacado = siendoAtacado;
    }

    public void dormir(int milis) throws InterruptedException {
        sleep(milis);
    }
}
