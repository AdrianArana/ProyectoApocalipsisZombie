package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;


public class Mapa  {
    private static final Logger logger = Config_log.getLogger();
    private boolean iniciado = false;

    ZonaRefugio zonaRefugio;
    ZonaRiesgo zonaRiesgo;
    ZonaTuneles zonaTuneles;
    private boolean pausado;

    public Mapa() {
        this.zonaRefugio = new ZonaRefugio();
        this.zonaRiesgo = new ZonaRiesgo();
        this.zonaTuneles = new ZonaTuneles();
        pausado = false;
    }


    public void setPausado(boolean pausado) {
        this.pausado = pausado;
    }

    public ZonaRefugio getZonaRefugio() {
        return zonaRefugio;
    }


    public ZonaRiesgo getZonaRiesgo() {
        return zonaRiesgo;
    }


    public ZonaTuneles getZonaTuneles() {
        return zonaTuneles;
    }


    Random random = new Random();
    //private int kills = 0;
    private final AtomicInteger[] quierenVolver = {
            new AtomicInteger(0), new AtomicInteger(0), new AtomicInteger(0), new AtomicInteger(0)
    };
    CyclicBarrier[] cb_tuneles = {
            new CyclicBarrier(3),
            new CyclicBarrier(3),
            new CyclicBarrier(3),
            new CyclicBarrier(3)
    };

    private final Semaphore[] puedeEntrarIda = {
            new Semaphore(1, true), new Semaphore(1, true), new Semaphore(1, true), new Semaphore(1, true)
    };
    private final Semaphore[] puedeEntrarVuelta = {
            new Semaphore(1, true), new Semaphore(1, true), new Semaphore(1, true), new Semaphore(1, true)
    };

    private final Semaphore[] ticketPasarTunel = {
            new Semaphore(1), new Semaphore(1), new Semaphore(1), new Semaphore(1)
    };

    Lock[] lockZonaRiesgo = new Lock[]{new ReentrantLock(), new ReentrantLock(), new ReentrantLock(), new ReentrantLock()};
    Lock lockZonaDescanso = new ReentrantLock();
    Lock lockZonaComedor = new ReentrantLock();
    Lock lockZonaComun = new ReentrantLock();
    Lock[] locks_tuneles = new Lock[]{new ReentrantLock(), new ReentrantLock(), new ReentrantLock(), new ReentrantLock()};


    /*Condition[] espera_ida = new Condition[]{locks_tuneles[0].newCondition(), locks_tuneles[1].newCondition(), locks_tuneles[2].newCondition(), locks_tuneles[3].newCondition()};
    //Para que esperen los individuos a que pasen los que vueven al salir a la zona de riesgo
    Condition[] espera_vuelta = new Condition[]{locks_tuneles[0].newCondition(), locks_tuneles[1].newCondition(), locks_tuneles[2].newCondition(), locks_tuneles[3].newCondition()};

    boolean[] tuneles_ocupados = new boolean[]{false, false, false, false};
*/
    Condition esperarComida = lockZonaComedor.newCondition();

    //HUMANO:
    // En la clase Mapa, añade estos nuevos campos:
    private final ReentrantLock[] tunelLocks = {
            new ReentrantLock(true), // fair lock para evitar inanición
            new ReentrantLock(true),
            new ReentrantLock(true),
            new ReentrantLock(true)
    };
    private final Semaphore[] IDA = {
            new Semaphore(1, true), new Semaphore(1, true), new Semaphore(1, true), new Semaphore(1, true)
    };
    private final Semaphore[] VUELTA = {
            new Semaphore(1, true), new Semaphore(1, true), new Semaphore(1, true), new Semaphore(1, true)
    };

    private final Semaphore[] TICKET = {
            new Semaphore(1), new Semaphore(1), new Semaphore(1), new Semaphore(1)
    };
    public void borrarHumanodelRiesgo(Humano h) {
        for (int i = 0; i < 4; i++) {
            zonaRiesgo.zonas[i].remove(h);
        }
    }


    public void pasarTunelIda(int tunelElegido, Humano humano) throws BrokenBarrierException, InterruptedException {
        // Esperar a que haya 3 humanos para formar grupo
        cb_tuneles[tunelElegido].await();
        logger.info("Humanos han superado la barrera y por tanto esperan a entrar en el tunel en orden");


        lockZonaDescanso.lock();
        zonaRefugio.zonaComun.remove(humano);
        logger.info("Humano " + humano.getIde() + " abandona la zona comun");

        lockZonaDescanso.unlock();

        zonaTuneles.zonasEsperaIda[tunelElegido].add(humano);
        logger.info("Humano " + humano.getIde() + " entra en la zona de esperas ida");

        puedeEntrarIda[tunelElegido].acquire();//Se pone a la cola una vez está en la sala de espera
        verificarPausa();
        ticketPasarTunel[tunelElegido].acquire();
        verificarPausa();
        zonaTuneles.zonasEsperaIda[tunelElegido].remove(humano);
        logger.info("Humano " + humano.getIde() + " sale en la zona de esperas ida");


        zonaTuneles.tuneles[tunelElegido].add(humano);
        logger.info("Humano " + humano.getIde() + " entra el tunel: "+tunelElegido);


        sleep(1000);
        zonaTuneles.tuneles[tunelElegido].remove(humano);
        logger.info("Humano " + humano.getIde() + " sale del tunel: "+tunelElegido);



        if (quierenVolver[tunelElegido].intValue() > 0) { //Así aseguramos la prioridad de los que vuelven
            verificarPausa();
            puedeEntrarVuelta[tunelElegido].release(); //CLAVE PARA ENTENDERLO esta en que no hemos hecho release del  zonaesperaida entonces nadie de ahi puedo coger el ticket
            ticketPasarTunel[tunelElegido].release();

        } else {
            verificarPausa();
            puedeEntrarIda[tunelElegido].release();
            ticketPasarTunel[tunelElegido].release();

        }

    }

    public void pasarTunelVuelta(int tunelElegido, Humano humano) throws InterruptedException {
        quierenVolver[tunelElegido].incrementAndGet();

        locks_tuneles[tunelElegido].lock();
        zonaTuneles.zonasEsperaVuelta[tunelElegido].add(humano);
        logger.info("Humano " + humano.getIde() + " entra en la zona de esperas vuelta");


        locks_tuneles[tunelElegido].unlock();

        lockZonaRiesgo[tunelElegido].lock();
        zonaRiesgo.zonas[tunelElegido].remove(humano);
        lockZonaRiesgo[tunelElegido].unlock();

        puedeEntrarVuelta[tunelElegido].acquire();//Una vez hecho el acquire aseguramos exclusion mutua
        verificarPausa();
        ticketPasarTunel[tunelElegido].acquire();
        verificarPausa();
        zonaTuneles.zonasEsperaVuelta[tunelElegido].remove(humano);
        logger.info("Humano " + humano.getIde() + " sale de la zona de esperas vuelta");


        zonaTuneles.tuneles[tunelElegido].add(humano);
        logger.info("Humano " + humano.getIde() + " entra el tunel: "+tunelElegido);
        sleep(1000);
        zonaTuneles.tuneles[tunelElegido].remove(humano);
        logger.info("Humano " + humano.getIde() + " sale del tunel: "+tunelElegido);


        quierenVolver[tunelElegido].decrementAndGet();
        if (quierenVolver[tunelElegido].intValue() > 0) {//Si alguien quiere volver le damos paso, si no pasan los que quieran salir a la zona de riestgo
            verificarPausa();
            puedeEntrarVuelta[tunelElegido].release();
            ticketPasarTunel[tunelElegido].release();

        } else {
            verificarPausa();
            puedeEntrarIda[tunelElegido].release();
            ticketPasarTunel[tunelElegido].release();

        }
    }

    public void entrarZonaDescanso(Humano humano) {
        lockZonaDescanso.lock();
        zonaRefugio.zonaDescanso.add(humano);
        logger.info("Humano " + humano.getIde() + " entra en zona de descanso con comida: " + humano.getComida());
        lockZonaDescanso.unlock();
        if (humano.getComida() > 0) {
            zonaRefugio.addComida(humano.getComida());
            humano.setComida(0);
            lockZonaComedor.lock();
            esperarComida.signal();
            esperarComida.signal();
            logger.info("Se ha añadido comida al almacén.");
            lockZonaComedor.unlock();
        }
    }

    public void salirZonaDescanso(Humano humano) {
        lockZonaDescanso.lock();
        zonaRefugio.zonaDescanso.remove(humano);
        logger.info("Humano " + humano.getIde() + " sale de la zona de descanso.");
        lockZonaDescanso.unlock();
    }

    public void entrarZonaRiesgo(int tunelTomado, Humano humano) throws InterruptedException {
        lockZonaRiesgo[tunelTomado].lock();
        zonaRiesgo.zonas[tunelTomado].add(humano);
        humano.setComida(2);
        logger.info("Humano " + humano.getIde() + " entra a la zona de riesgo " + tunelTomado);
        lockZonaRiesgo[tunelTomado].unlock();
    }

    public void entrarZonaComedor(Humano humano) throws InterruptedException {
        lockZonaComedor.lock();
        zonaRefugio.zonaComedor.add(humano);
        logger.info("Humano " + humano.getIde() + " entra al comedor.");
        while (zonaRefugio.getAlmacen_comida() <= 0) {
            logger.info("Humano " + humano.getIde() + " espera comida.");
            esperarComida.await();
        }
        zonaRefugio.takeComida();
        lockZonaComedor.unlock();
        logger.info("Humano " + humano.getIde() + " ha comido.");
    }

    public void salirZonaComedor(Humano humano) throws InterruptedException {
        lockZonaComedor.lock();
        zonaRefugio.zonaComedor.remove(humano);
        zonaRefugio.addComida(humano.getComida());
        logger.info("Humano " + humano.getIde() + " sale del comedor.");
        lockZonaComedor.unlock();
    }

    public void entrarZonaComun(Humano humano) {
        lockZonaComun.lock();
        zonaRefugio.zonaComun.add(humano);
        logger.info("Humano " + humano.getIde() + " entra a la zona común.");
        lockZonaComun.unlock();
    }




    //FUNCIONES DEL ZOMBIE
    public int cambiarDeZona(int zonaInicial, Zombie zombie) {
        lockZonaRiesgo[zonaInicial].lock();
        zonaRiesgo.zonas[zonaInicial].remove(zombie);
        lockZonaRiesgo[zonaInicial].unlock();

        int nueva_zona = random.nextInt(4);
        lockZonaRiesgo[nueva_zona].lock();

        zonaRiesgo.zonas[nueva_zona].add(zombie);
        lockZonaRiesgo[nueva_zona].unlock();
        logger.info("Zombie " + zombie.getIde() + " cambia de zona.");

        return nueva_zona;

    }

    public void atacar(Zombie zombie, int zonaZombie) throws InterruptedException {
        ArrayList<Humano> posiblesAtaques = new ArrayList<>();
        synchronized (zonaRiesgo.zonas[zonaZombie]) {
            try{
                for (Thread individuo : zonaRiesgo.zonas[zonaZombie]) {
                    if (individuo instanceof Humano) {
                        if (!((Humano) individuo).getSiendoAtacado()) {
                            posiblesAtaques.add((Humano) individuo);
                        }
                    }
                }
            }catch(Exception ignored){}
        }
        if (!posiblesAtaques.isEmpty()) {
            boolean gana = (((int) (Math.random() * 3)) == 0);
            Humano humanoAtacado = posiblesAtaques.get(random.nextInt(posiblesAtaques.size()));
            humanoAtacado.setSiendoAtacado(true);
            if (gana) {
                if (zonaRiesgo.zonas[zonaZombie].remove(humanoAtacado)) {
                    logger.info("HUMANO ELIMINADO CORRECTAMENTE");
                } else {
                    logger.info("ERROR AL ELIMINAR A HUMANO: " + humanoAtacado.getIde());
                }
                logger.info("Zombie " + zombie.getIde() + " mata a humano " + humanoAtacado.getIde());
                humanoAtacado.morir(true);
                Thread.sleep(random.nextInt(500) + 1000);
                zombie.sumarKills();
                String idZombieNuevo = "Z" + humanoAtacado.getIde().substring(1, 5);
                Zombie zombieNuevo = new Zombie(idZombieNuevo, this, zonaZombie);
                zombieNuevo.start();
            } else {
                logger.info("Humano " + humanoAtacado.getIde() + " sobrevive al ataque del zombie " + zombie.getIde());
                humanoAtacado.setSiendoAtacado(true);

                int milisAtaque = random.nextInt(500) + 1000;
                humanoAtacado.marcarHumano();
                humanoAtacado.setComida(0);
                humanoAtacado.dormir(milisAtaque);
                Thread.sleep(milisAtaque);
                logger.info("Humano " + humanoAtacado.getIde() + " huye por túnel tras sobrevivir.");
                pasarTunelVuelta(zonaZombie, humanoAtacado);
                zonaRiesgo.zonas[zonaZombie].remove(humanoAtacado);
                humanoAtacado.setSiendoAtacado(false);

            }
        }

    }

    public synchronized void verificarPausa() {
        while (pausado) {
            try {
                logger.info("Verificando pausa de los hilos");

                wait();
            } catch (InterruptedException e) {
                logger.info(e.getMessage());
            }
        }
    }

    public void pausarHilos() {
        logger.info("Pausando todos los hilos");

        pausado = true;
    }

    public synchronized void reanudarHilos() {
        logger.info("Reanudando todos los hilos");

        pausado = false;
        notifyAll();
    }

    public boolean getIniciado() {
        return iniciado;
    }

    public void setIniciado(boolean b) {
        this.iniciado = b;
    }

    public void finalizar() {
        logger.info("Finalizando todos los hilos");

        System.exit(0);
    }

}