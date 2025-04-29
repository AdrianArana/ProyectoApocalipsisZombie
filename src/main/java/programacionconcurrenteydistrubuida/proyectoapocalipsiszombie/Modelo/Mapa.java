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

    Semaphore[] sem_TunelesVuelta = {
            new Semaphore(1, true),
            new Semaphore(1, true),
            new Semaphore(1, true),
            new Semaphore(1, true)
    };
    Lock lockZonaRiesgo = new ReentrantLock();
    Lock lockZonaDescanso = new ReentrantLock();
    Lock lockZonaComedor = new ReentrantLock();
    Lock lockZonaComun = new ReentrantLock();



    Lock[] locks_tuneles = new Lock[]{new ReentrantLock(), new ReentrantLock(), new ReentrantLock(), new ReentrantLock()};

    boolean[] tuneles_ocupados = new boolean[]{false, false, false, false};


    Lock volverLock = new ReentrantLock();
    Condition esperaDeVuelta = volverLock.newCondition();
    private boolean quierenVolver = false;
    Condition esperarComida = lockZonaComedor.newCondition();


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
        zonaTuneles.tuneles[tunelElegido - 1].add(humano);
        // Pasan de 1 en 1, porque el semaforo es fair

        locks_tuneles[tunelElegido - 1].lock();
        tuneles_ocupados[tunelElegido - 1] = true;
        locks_tuneles[tunelElegido - 1].unlock();

        sleep(1000); // Pasan de lado a lado
        zonaTuneles.tuneles[tunelElegido - 1].remove(humano);
        while (quierenVolver) {
            esperaDeVuelta.notify();//Monitor
            //Espera a que vuelva el que queria volver antes de dejar a otro entrar con el sem_tuneles.release();
        }
        sem_Tuneles[tunelElegido - 1].release(); // Antes del release, dejamos pasar a los que quieran volver
        //Humano "id" ha pasado a la zona: "tunelElegido"
    }

    public synchronized void pasarTunelVuelta(int tunelElegido, Humano humano) throws InterruptedException {

        quierenVolver = true; // Ponemos a true
        while (tuneles_ocupados[tunelElegido - 1]) {

            esperaDeVuelta.await();
            sleep(1000);
            quierenVolver = false;
            sem_TunelesVuelta[tunelElegido - 1].acquire();
            sleep(1000);
            sem_TunelesVuelta[tunelElegido - 1].release();
        }
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

    public void entrarZonaComun(Humano humano) {
        lockZonaComun.lock();
        zonaRefugio.zonaComun.add(humano);
        lockZonaComun.unlock();
    }

}

