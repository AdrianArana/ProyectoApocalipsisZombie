package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo;

import java.util.ArrayList;
import java.util.Random;
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

    public Mapa() {
        this.zonaRefugio = new ZonaRefugio();
        this.zonaRiesgo = new ZonaRiesgo();
        this.zonaTuneles = new ZonaTuneles();
    }


    Random random = new Random();
    //private int kills = 0;
    private int quierenVolver = 0;
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
    Lock[] locks_tuneles = new Lock[]{new ReentrantLock(), new ReentrantLock(), new ReentrantLock(), new ReentrantLock()};


    Condition[] espera_vuelta = new Condition[]{locks_tuneles[0].newCondition(), locks_tuneles[1].newCondition(), locks_tuneles[2].newCondition(), locks_tuneles[3].newCondition()};
    //Para que esperen los individuos a que pasen los que vueven al salir a la zona de riesgo
    Condition[] espera_salida = new Condition[]{locks_tuneles[0].newCondition(), locks_tuneles[1].newCondition(), locks_tuneles[2].newCondition(), locks_tuneles[3].newCondition()};

    boolean[] tuneles_ocupados = new boolean[]{false, false, false, false};

    Condition esperarComida = lockZonaComedor.newCondition();

    //FUNCIONES:


    //FUNCIONES PARA HUMANO

    public void pasarTunelIda(int tunelElegido, Humano humano) throws BrokenBarrierException, InterruptedException {
        cb_tuneles[tunelElegido].await();
        sem_Tuneles[tunelElegido].acquire();
        locks_tuneles[tunelElegido].lock();

        zonaRefugio.zonaComun.remove(humano);
        zonaTuneles.tuneles[tunelElegido].add(humano);
        tuneles_ocupados[tunelElegido] = true;
        locks_tuneles[tunelElegido].unlock();//Lo soltamos mientras pasa para que puedan esperar los de fuera o los de dentro

        sleep(1000); // Pasan de lado a lado
        locks_tuneles[tunelElegido].lock();

        zonaTuneles.tuneles[tunelElegido].remove(humano);
        tuneles_ocupados[tunelElegido] = false;

        while (quierenVolver > 0) {
            espera_vuelta[tunelElegido].signal();//Monitor
            espera_salida[tunelElegido].await();
            //Espera a que vuelva el que queria volver antes de dejar a otro entrar con el sem_tuneles.release();

        }
        locks_tuneles[tunelElegido].unlock();
        sem_Tuneles[tunelElegido].release(); // Antes del release, dejamos pasar a los que quieran volver
        //Humano "id" ha pasado a la zona: "tunelElegido"
    }

    public void pasarTunelVuelta(int tunelElegido, Humano humano) throws InterruptedException {
        quierenVolver++; // Ponemos a true
        locks_tuneles[tunelElegido].lock();
        while (tuneles_ocupados[tunelElegido]) {
            espera_vuelta[tunelElegido].await();
            //Esperamos a que el tunel se libere

        }
        zonaTuneles.tuneles[tunelElegido].add(humano);
        locks_tuneles[tunelElegido].unlock();

        sleep(1000);                                ///Pasa por el tunel
        locks_tuneles[tunelElegido].lock();

        zonaTuneles.tuneles[tunelElegido].remove(humano);
        espera_salida[tunelElegido].signal();
        quierenVolver--;
        locks_tuneles[tunelElegido].unlock();
    }

    public void entrarZonaDescanso(Humano humano) {
        lockZonaDescanso.lock();
        zonaRefugio.zonaDescanso.add(humano);
        lockZonaDescanso.unlock();
        if (humano.getComida() > 0) {
            zonaRefugio.addComida(humano.getComida());
            humano.setComida(0);
            lockZonaComedor.lock();
            esperarComida.signal();
            //Avisamos a 2 humanos porque siempre se trae 2 de comida y cada uno come solo 1.
            esperarComida.signal();
            lockZonaComedor.unlock();
        }
    }


    public void salirZonaDescanso(Humano humano) {
        lockZonaDescanso.lock();
        zonaRefugio.zonaDescanso.remove(humano);
        lockZonaDescanso.unlock();
    }


    public void entrarZonaRiesgo(int tunelTomado, Humano humano) throws InterruptedException {
        lockZonaRiesgo.lock();
        zonaRiesgo.zonas[tunelTomado].add(humano);
        humano.setComida(2);
        lockZonaRiesgo.unlock();
    }

    public void salirZonaRiesgo(int tunelTomado, Humano humano) throws InterruptedException {
        lockZonaRiesgo.lock();
        zonaRiesgo.zonas[tunelTomado].remove(humano);
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


    /// FUNCIONES PARA ZOMBIE
    public int cambiarDeZona(int zonaInicial, Zombie zombie) {
        lockZonaRiesgo.lock();
        zonaRiesgo.zonas[zonaInicial].remove(zombie);
        int nueva_zona = random.nextInt(4);
        zonaRiesgo.zonas[nueva_zona].add(zombie);
        lockZonaRiesgo.unlock();
        return nueva_zona;
    }

    public void atacar(Zombie zombie, int zonaZombie) throws InterruptedException {
        boolean dejarDeAtacar = false;
        Humano humanoAtacado = null;

        synchronized (this) {
            ArrayList<Humano> posiblesAtaques = new ArrayList<Humano>();
            for (Thread individuo : zonaRiesgo.zonas[zonaZombie]) {
                if (individuo.getClass() == Humano.class) {
                    posiblesAtaques.add((Humano) individuo); // Los metemos si es humano
                }
            }
            if (!posiblesAtaques.isEmpty()) {//Solo atacamos si hay humanos
                boolean encontrado = false;
                while (!encontrado) {//Buscamos humano para atacar mientras haya en la zona de riesgo.
                    humanoAtacado = posiblesAtaques.get(random.nextInt(posiblesAtaques.size()));
                    if (!humanoAtacado.getSiendoAtacado()) {//Si no esta siendo atacado, empezamos a atacarle
                        humanoAtacado.setSiendoAtacado();
                        encontrado = true;
                    } else { // Volvemos a buscar
                        posiblesAtaques = new ArrayList<Humano>();
                        for (Thread individuo : zonaRiesgo.zonas[zonaZombie]) {
                            if (individuo.getClass() == Humano.class) {
                                posiblesAtaques.add((Humano) individuo); // Los metemos si es humano
                            }
                        }
                        if (posiblesAtaques.isEmpty()) {
                            dejarDeAtacar = true;
                            encontrado = true;
                        }
                    }
                }
            }
        }
        if (!dejarDeAtacar) {//Por si no hay ya para atacar
            if (humanoAtacado != null) {//Si hemos encontrado humano para atacar, le atacamos
                boolean gana = (((int) (Math.random() * 3)) == 0);
                if (gana) {
                    humanoAtacado.interrupt();// Matamos al humano
                    Thread.sleep(random.nextInt(1000) + 500);//Tarda un tiempo de 1 a 1,5 segundos en atacarlo, (antes de eliminarlo)
                    zombie.sumarKills();
                    zonaRiesgo.zonas[zonaZombie].remove(humanoAtacado);
                    String idZombieNuevo = ("Z" + humanoAtacado.getIde().substring(1, 4));

                    Zombie zombieNuevo = new Zombie(idZombieNuevo, this, zonaZombie);

                    zombieNuevo.start();
                } else {
                    int milisAtaque = random.nextInt(1000) + 500;//Representa el tiempo que luchan humano contra zombie
                    humanoAtacado.sleep(milisAtaque);
                    humanoAtacado.marcarHumano();
                    humanoAtacado.setComida(0);//Pierde la comida al defenderse
                    sleep(milisAtaque);
                    pasarTunelVuelta(zonaZombie, humanoAtacado); //Humano vuelve inmediatamente si han intentado atacarle
                }
            }
        }
    }
}

