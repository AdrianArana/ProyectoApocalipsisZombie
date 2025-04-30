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
    private boolean enPausa = false;
    private final Object lock = new Object(); // Objeto para controlar la sincronización



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
                // para entrar al tunel, y después de entrar, se elimina de la zona comu
                verificarPausa();  // Verificar si se debe pausar
                mapa.entrarZonaRiesgo(tunelElegido, this);
                verificarPausa();  // Verificar si se debe pausar
                sleep(3000 + (int) (Math.random() * 2000));//Tiempo en la zona de riesgo
                verificarPausa();  // Verificar si se debe pausar

                if (marcado){
                    mapa.salirZonaRiesgo(tunelElegido, this);
                    marcado = false;
                    mapa.entrarZonaDescanso(this);
                    sleep(random.nextInt(2000) + 2000);//Descansa 2-4 segundos
                    verificarPausa();  // Verificar si se debe pausar
                    mapa.salirZonaDescanso(this);

                    mapa.entrarZonaComedor(this);
                    sleep(3000 + (int) (Math.random() * 2000));//Come durante 3-5 segundos
                    verificarPausa();  // Verificar si se debe pausar
                    mapa.salirZonaComedor(this);

                    mapa.entrarZonaDescanso(this);
                    sleep(3000 + (int) (Math.random() * 2000));//Descanso extra de 3 a 5 segundos
                    verificarPausa();  // Verificar si se debe pausar
                    mapa.salirZonaDescanso(this);}
                else {
                    mapa.salirZonaRiesgo(tunelElegido, this);
                    mapa.pasarTunelVuelta(tunelElegido, this);
                    mapa.entrarZonaDescanso(this);
                    sleep(random.nextInt(2000) + 2000);//Descansa 2-4 segundos
                    verificarPausa();  // Verificar si se debe pausar
                    mapa.salirZonaDescanso(this);

                    mapa.entrarZonaComedor(this);
                    sleep(3000 + (int) (Math.random() * 2000));//Come durante 3-5 segundos
                    verificarPausa();  // Verificar si se debe pausar
                    mapa.salirZonaComedor(this);

                }
                mapa.entrarZonaComun(this);

                verificarPausa();  // Verificar si se debe pausar

            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                System.out.printf("El humano con id: " + id + " ha sido convertido en zombie\n");
            }
        }
    }


    public synchronized void suspender() {
        enPausa = true;  // Marca el humano como pausado
    }

    public synchronized void reanudar() {
        enPausa = false;  // Marca el humano como no pausado
        synchronized (lock) {
            lock.notify();  // Notifica al humano para que reanude su ejecución
        }
    }

    private void verificarPausa() throws InterruptedException {
        synchronized (lock) {
            while (enPausa) {  // Si está pausado, espera
                lock.wait();
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

    public boolean getSiendoAtacado(){
        return this.siendoAtacado;
    }
    public synchronized void setSiendoAtacado(){//Synchronized para que no puedan atacarle 2 zombies a la vez
        this.siendoAtacado=true;
    }
}
