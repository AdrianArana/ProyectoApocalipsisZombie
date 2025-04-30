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
                mapa.pasarTunelIda(tunelElegido, this);// Aqui se hace la espera
                // para entrar al tunel, y después de entrar, se elimina de la zona comun
                mapa.entrarZonaRiesgo(tunelElegido, this);
                sleep(3000 + (int) (Math.random() * 2000));//Tiempo en la zona de riesgo
                if (muerte) {
                    break;
                } else {
                    if (marcado) {
                        mapa.salirZonaRiesgo(tunelElegido, this);
                        marcado = false;
                        mapa.entrarZonaDescanso(this);
                        sleep(random.nextInt(2000) + 2000);//Descansa 2-4 segundos
                        mapa.salirZonaDescanso(this);

                        mapa.entrarZonaComedor(this);
                        sleep(3000 + (int) (Math.random() * 2000));//Come durante 3-5 segundos
                        mapa.salirZonaComedor(this);

                        mapa.entrarZonaDescanso(this);
                        sleep(3000 + (int) (Math.random() * 2000));//Descanso extra de 3 a 5 segundos
                        mapa.salirZonaDescanso(this);
                    } else {
                        mapa.salirZonaRiesgo(tunelElegido, this);
                        mapa.pasarTunelVuelta(tunelElegido, this);
                        mapa.entrarZonaDescanso(this);
                        sleep(random.nextInt(2000) + 2000);//Descansa 2-4 segundos
                        mapa.salirZonaDescanso(this);

                        mapa.entrarZonaComedor(this);
                        sleep(3000 + (int) (Math.random() * 2000));//Come durante 3-5 segundos
                        mapa.salirZonaComedor(this);

                    }
                    mapa.entrarZonaComun(this);
                    sleep(1000);
                }

            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                System.out.printf("El humano con id: " + id + " ha sido convertido en zombie\n");
            }
        }
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

    public synchronized void setSiendoAtacado() {//Synchronized para que no puedan atacarle 2 zombies a la vez
        this.siendoAtacado = true;
    }
}
