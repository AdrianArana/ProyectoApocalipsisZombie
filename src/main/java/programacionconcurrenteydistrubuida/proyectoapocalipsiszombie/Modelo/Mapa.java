package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo;

import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class Mapa {
    ZonaRefugio zonaRefugio;
    ZonaRiesgo zonaRiesgo;
    ZonaTuneles zonaTuneles;

    CyclicBarrier[] cb_tuneles = {
            new CyclicBarrier(3),
            new CyclicBarrier(3),
            new CyclicBarrier(3),
            new CyclicBarrier(3)
    };
    Semaphore[] sem_Tuneles = {
            new Semaphore(1, true),
            new Semaphore(1, true),
            new Semaphore(1, true),
            new Semaphore(1, true)
    };

    Lock lockZonaRiesgo = new ReentrantLock();
    Lock lockZonaDescanso = new ReentrantLock();
    Lock lockZonaComedor = new ReentrantLock();
    Lock lockZonaComun = new ReentrantLock();

    Condition esperarComida = lockZonaComedor.newCondition();
    private ArrayList<Humano> humanos_zona1 = new ArrayList<>();
    private ArrayList<Humano> humanos_zona2 = new ArrayList<>();
    private ArrayList<Humano> humanos_zona3 = new ArrayList<>();
    private ArrayList<Humano> humanos_zona4 = new ArrayList<>();


    //FUNCIONES:
    /*
        Funciones del humano:
            void entrarTunel(int tunelElegido, Humano humano, boolean preferencia?)// hay preferencia al volver
                void salirTunel(lo mismo)
            void entrarZonaRiesgo(int tunelTomado, Humano humano)
            void salirZonaRiesgo(lo mismo)
            void entrarZonaDescanso(Humano humano) // cuenta con estar un tiempo dentro [2-4s]
            void salirZonaDescanso(Humano humano)
            void entrarZonaComedor(Humano humano) //Dentro de esta comer yestar un tiempo [3-5s]
            void salirZonaComedor(Humano humano)
            void entrarZonaComun(Humano humano) // y ya se pone a hacer cola para volver a salir
        Funciones del zombie
            void cambiarDeZona(int zonaInicial, int zonaDestino) // cambiar al zombie de una lista a otra
            void atacar(
                if (hay humanos) {
                (for individuo in zonas[mizona]) la recorre
                    elegir uno al azar
                    le ataca
                        if (gana) {
                            le matamos (humano.interrupt (matarle) y metemos un zombie nuevo con su id)
                        } else{
                            le marcamos (humano.setMarcado(true) y humano.quitarComida())
                        }
                    }
                else {salir de la funcion}

     */
    public void pasarTunelIda(int tunelElegido, Humano humano) throws BrokenBarrierException, InterruptedException {
        cb_tuneles[tunelElegido - 1].await();
        sem_Tuneles[tunelElegido - 1].acquire();
        zonaRefugio.zonaComun.remove(humano);
        // Pasan de 1 en 1, porque el semaforo es fair
        sleep(1000); // Pasan de lado a lado
        sem_Tuneles[tunelElegido - 1].release();
        //Humano "id" ha pasado a la zona: "tunelElegido"
    }

    public void pasarTunelVuelta(int tunelElegido, Humano humano) {
        /*lockTuneles.lock();
        pasandoDeVuelta = true;
        sem_Tuneles[tunelElegido-1].acquire();
        sleep(1000);
        pasandoDeVuelta = false;
        sem_Tuneles[tunelElegido-1].release();*/
    }

    public void entrarZonaDescanso(Humano humano) {
        lockZonaDescanso.lock();
        zonaRefugio.zonaDescanso.add(humano);
        if (humano.getComida() > 0) {
            zonaRefugio.addComida(humano.getComida());
            humano.setComida(0);
            esperarComida.signal();
            //Avisamos a 2 humanos porque siempre se trae 2 de comida y cada uno come solo 1.
            esperarComida.signal();
        }
        lockZonaDescanso.unlock();

    }

    public void salirZonaDescanso(Humano humano) {
        lockZonaDescanso.lock();
        zonaRefugio.zonaDescanso.remove(humano);
        lockZonaDescanso.unlock();
    }


    public void entrarZonaRiesgo(int tunelTomado, Humano humano) throws InterruptedException {
        lockZonaRiesgo.lock();
        zonaRiesgo.zonas[tunelTomado - 1].add(humano);
        humano.setComida(2);
        lockZonaRiesgo.unlock();
    }

    public void salirZonaRiesgo(int tunelTomado, Humano humano) throws InterruptedException {
        lockZonaRiesgo.lock();
        zonaRiesgo.zonas[tunelTomado - 1].remove(humano);
        lockZonaRiesgo.unlock();
    }


    public void entrarZonaComedor(Humano humano) throws InterruptedException {
        lockZonaComedor.lock();
        zonaRefugio.zonaComedor.add(humano);
        while (zonaRefugio.getAlmacen_comida() <= 0) {
            esperarComida.await();
        }

        zonaRefugio.takeComida(humano.getComida());
        lockZonaComedor.unlock();
    }


    public void salirZonaComedor(Humano humano) throws InterruptedException {
        lockZonaComedor.lock();
        zonaRefugio.zonaComedor.remove(humano);
        zonaRefugio.addComida(humano.getComida());
    }

    public void entrarZonaComun(Humano humano){
        lockZonaComun.lock();
        zonaRefugio.zonaComun.add(humano);
        lockZonaComun.unlock();
    }

}
