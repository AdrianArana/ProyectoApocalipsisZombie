package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;

public class Humano extends Thread {
    private String id;
    private int comida;//H____
    Mapa mapa;
    private boolean marcado;
    Random random = new Random();

    public Humano(Mapa mapa, String id) {
        this.mapa = mapa;
        this.id = id;
        this.comida = 0;
    }

    @Override
    public void run() {


        while (true) {
            //Generados en la zona comun
            try {
                int tunelElegido = (int) (Math.random() * 4 + 1);
                mapa.pasarTunelIda(tunelElegido, this);// Aqui se hace la espera
                // para entrar al tunel, y después de entrar, se elimina de la zona comun

                mapa.entrarZonaRiesgo(tunelElegido, this);
                sleep(3000 + (int) (Math.random() * 2000));//Tiempo en la zona de riesgo
                mapa.salirZonaRiesgo(tunelElegido, this);

                mapa.pasarTunelVuelta(tunelElegido, this);

                mapa.entrarZonaDescanso(this);
                sleep(random.nextInt(2000) + 2000);//Descansa 2-4 segundos
                mapa.salirZonaDescanso(this);

                mapa.entrarZonaComedor(this);
                sleep(3000 + (int) (Math.random() * 2000));//Come durante 3-5 segundos
                mapa.salirZonaComedor(this);

                if (marcado) {
                    marcado = false;
                    mapa.entrarZonaDescanso(this);
                    sleep(3000 + (int) (Math.random() * 2000));//Descanso extra de 3 a 5 segundos
                    mapa.salirZonaDescanso(this);
                }
                mapa.entrarZonaComun(this);
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // hacer cola para salir en un tunel aleatorio (cyclicBarrier??)
            // salir de 1 en 1 [1 sec]
            //Despues del cb un semaforo de cada tunel con un permit cada uno (fair) para que nunca
            // le adelanten otros de otro grupo
            //Coger comida de la zona de riesgo (2/persona)[3-5 sec]
            //si es atacado:
            //si pierde:
            // renace como zombie
            //si gana:
            // marcado
            // vuelve inmediatamente a un tunel [1s] SIN COMIDA todo??
            // descanso [2-4sec]
            // comedor [3-5sec]
            // descanso extra [3-5sec]
            //fin del bucle---------
            //si no es atacado:
            // depositar comida
            // descanso [2-4sec]
            // comedor [3-5sec] (comida--;) (SI NO HAY COMIDA ESPERAR A QUE LLEGUE COMIDA (monitores??)
            // fin del bucle-----------


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
}
