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

    public ZonaRefugio getZonaRefugio() {
        return zonaRefugio;
    }

    public void setZonaRefugio(ZonaRefugio zonaRefugio) {
        this.zonaRefugio = zonaRefugio;
    }

    public ZonaRiesgo getZonaRiesgo() {
        return zonaRiesgo;
    }

    public void setZonaRiesgo(ZonaRiesgo zonaRiesgo) {
        this.zonaRiesgo = zonaRiesgo;
    }

    public ZonaTuneles getZonaTuneles() {
        return zonaTuneles;
    }

    public void setZonaTuneles(ZonaTuneles zonaTuneles) {
        this.zonaTuneles = zonaTuneles;
    }

    Random random = new Random();
    //private int kills = 0;
    private int[] quierenVolver = new int[4];
    CyclicBarrier[] cb_tuneles = {
            new CyclicBarrier(1),
            new CyclicBarrier(1),
            new CyclicBarrier(1),
            new CyclicBarrier(1)
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




    public void pasarTunelIda(int tunelElegido, Humano humano) throws BrokenBarrierException, InterruptedException {
        System.out.println("[TUNEL IDA] Humano " + humano.getIde() + " esperando barrera en túnel " + tunelElegido);
        cb_tuneles[tunelElegido].await();
        System.out.println("[TUNEL IDA] Humano " + humano.getIde() + " esperando semáforo túnel " + tunelElegido);
        sem_Tuneles[tunelElegido].acquire();
        locks_tuneles[tunelElegido].lock();

        System.out.println("[TUNEL IDA] Humano " + humano.getIde() + " entra al túnel " + tunelElegido);
        zonaRefugio.zonaComun.remove(humano);
        zonaTuneles.tuneles[tunelElegido].add(humano);
        tuneles_ocupados[tunelElegido] = true;
        locks_tuneles[tunelElegido].unlock();

        sleep(1000);

        locks_tuneles[tunelElegido].lock();
        zonaTuneles.tuneles[tunelElegido].remove(humano);
        tuneles_ocupados[tunelElegido] = false;

        while (quierenVolver > 0) {
            System.out.println("[TUNEL IDA] Humano " + humano.getIde() + " espera a que pasen los que quieren volver en túnel " + tunelElegido);
            espera_vuelta[tunelElegido].signal();
            espera_salida[tunelElegido].await();
        }
        locks_tuneles[tunelElegido].unlock();
        sem_Tuneles[tunelElegido].release();
        System.out.println("[TUNEL IDA] Humano " + humano.getIde() + " ha cruzado el túnel " + tunelElegido);
    }

    public void pasarTunelVuelta(int tunelElegido, Humano humano) throws InterruptedException {
        System.out.println("[TUNEL VUELTA] Humano " + humano.getIde() + " quiere volver por túnel " + tunelElegido);
        quierenVolver++;
        locks_tuneles[tunelElegido].lock();
        while (tuneles_ocupados[tunelElegido]) {
            espera_vuelta[tunelElegido].await();
        }

        System.out.println("[TUNEL VUELTA] Humano " + humano.getIde() + " entrando en túnel " + tunelElegido);
        zonaTuneles.tuneles[tunelElegido].add(humano);
        locks_tuneles[tunelElegido].unlock();

        sleep(1000);

        locks_tuneles[tunelElegido].lock();
        zonaTuneles.tuneles[tunelElegido].remove(humano);
        espera_salida[tunelElegido].signal();
        quierenVolver--;
        locks_tuneles[tunelElegido].unlock();
        System.out.println("[TUNEL VUELTA] Humano " + humano.getIde() + " ha cruzado el túnel de vuelta " + tunelElegido);
    }

    public void entrarZonaDescanso(Humano humano) {
        lockZonaDescanso.lock();
        zonaRefugio.zonaDescanso.add(humano);
        System.out.println("[DESCANSO] Humano " + humano.getIde() + " entra en zona de descanso con comida: " + humano.getComida());
        lockZonaDescanso.unlock();
        if (humano.getComida() > 0) {
            zonaRefugio.addComida(humano.getComida());
            humano.setComida(0);
            lockZonaComedor.lock();
            esperarComida.signal();
            esperarComida.signal();
            System.out.println("[DESCANSO] Se ha añadido comida al almacén.");
            lockZonaComedor.unlock();
        }
    }

    public void salirZonaDescanso(Humano humano) {
        lockZonaDescanso.lock();
        zonaRefugio.zonaDescanso.remove(humano);
        System.out.println("[DESCANSO] Humano " + humano.getIde() + " sale de la zona de descanso.");
        lockZonaDescanso.unlock();
    }

    public void entrarZonaRiesgo(int tunelTomado, Humano humano) throws InterruptedException {
        lockZonaRiesgo.lock();
        zonaRiesgo.zonas[tunelTomado].add(humano);
        humano.setComida(2);
        System.out.println("[RIESGO] Humano " + humano.getIde() + " entra a la zona de riesgo " + tunelTomado);
        lockZonaRiesgo.unlock();
    }

    public void salirZonaRiesgo(int tunelTomado, Humano humano) throws InterruptedException {
        lockZonaRiesgo.lock();
        zonaRiesgo.zonas[tunelTomado].remove(humano);
        System.out.println("[RIESGO] Humano " + humano.getIde() + " sale de la zona de riesgo " + tunelTomado);
        lockZonaRiesgo.unlock();
    }

    public void entrarZonaComedor(Humano humano) throws InterruptedException {
        lockZonaComedor.lock();
        zonaRefugio.zonaComedor.add(humano);
        System.out.println("[COMEDOR] Humano " + humano.getIde() + " entra al comedor.");
        while (zonaRefugio.getAlmacen_comida() <= 0) {
            System.out.println("[COMEDOR] Humano " + humano.getIde() + " espera comida.");
            esperarComida.await();
        }
        zonaRefugio.takeComida(humano.getComida());
        lockZonaComedor.unlock();
        System.out.println("[COMEDOR] Humano " + humano.getIde() + " ha comido.");
    }

    public void salirZonaComedor(Humano humano) throws InterruptedException {
        lockZonaComedor.lock();
        zonaRefugio.zonaComedor.remove(humano);
        zonaRefugio.addComida(humano.getComida());
        System.out.println("[COMEDOR] Humano " + humano.getIde() + " sale del comedor.");
        lockZonaComedor.unlock();
    }

    public void entrarZonaComun(Humano humano) {
        lockZonaComun.lock();
        zonaRefugio.zonaComun.add(humano);
        System.out.println("[COMUN] Humano " + humano.getIde() + " entra a la zona común.");
        lockZonaComun.unlock();
    }

    public int cambiarDeZona(int zonaInicial, Zombie zombie) {
        lockZonaRiesgo.lock();
        zonaRiesgo.zonas[zonaInicial].remove(zombie);
        int nueva_zona = random.nextInt(4);
        zonaRiesgo.zonas[nueva_zona].add(zombie);
        System.out.println("[ZOMBIE] Zombie " + zombie.getId() + " se mueve de zona " + zonaInicial + " a " + nueva_zona);
        lockZonaRiesgo.unlock();
        return nueva_zona;
    }

    public void atacar(Zombie zombie, int zonaZombie) throws InterruptedException {
        boolean dejarDeAtacar = false;
        Humano humanoAtacado = null;

        synchronized (this) {
            ArrayList<Humano> posiblesAtaques = new ArrayList<>();
            for (Thread individuo : zonaRiesgo.zonas[zonaZombie]) {
                if (individuo.getClass() == Humano.class) {
                    posiblesAtaques.add((Humano) individuo);
                }
            }

            if (!posiblesAtaques.isEmpty()) {
                boolean encontrado = false;
                while (!encontrado) {
                    humanoAtacado = posiblesAtaques.get(random.nextInt(posiblesAtaques.size()));
                    if (!humanoAtacado.getSiendoAtacado()) {
                        humanoAtacado.setSiendoAtacado();
                        encontrado = true;
                    } else {
                        posiblesAtaques.clear();
                        for (Thread individuo : zonaRiesgo.zonas[zonaZombie]) {
                            if (individuo.getClass() == Humano.class) {
                                posiblesAtaques.add((Humano) individuo);
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

        if (!dejarDeAtacar && humanoAtacado != null) {
            boolean gana = (((int) (Math.random() * 3)) == 0);
            if (gana) {
                System.out.println("[ATAQUE] Zombie " + zombie.getId() + " mata a humano " + humanoAtacado.getIde());
                humanoAtacado.interrupt();
                Thread.sleep(random.nextInt(1000) + 500);
                zombie.sumarKills();
                zonaRiesgo.zonas[zonaZombie].remove(humanoAtacado);
                String idZombieNuevo = ("Z" + humanoAtacado.getIde().substring(1, 4));
                Zombie zombieNuevo = new Zombie(idZombieNuevo, this, zonaZombie);
                zombieNuevo.start();
            } else {
                System.out.println("[ATAQUE] Humano " + humanoAtacado.getIde() + " sobrevive al ataque del zombie " + zombie.getId());
                int milisAtaque = random.nextInt(1000) + 500;
                humanoAtacado.sleep(milisAtaque);
                humanoAtacado.marcarHumano();
                humanoAtacado.setComida(0);
                sleep(milisAtaque);
                pasarTunelVuelta(zonaZombie, humanoAtacado);
            }
        }
    }

//
//    //FUNCIONES PARA HUMANO
//
//    public void pasarTunelIda(int tunelElegido, Humano humano) throws BrokenBarrierException, InterruptedException {
//        System.out.println("Humano con id: " + humano.getIde() + " pasa a la zona: " + tunelElegido + "esperando entrar tunel");
//        cb_tuneles[tunelElegido].await();
//        sem_Tuneles[tunelElegido].acquire();
//        locks_tuneles[tunelElegido].lock();
//
//        System.out.println("Humano con id: " + humano.getIde() + " entra a la zona: " + tunelElegido + " esperando salida");
//        zonaRefugio.zonaComun.remove(humano);
//        zonaTuneles.tuneles[tunelElegido].add(humano);
//        tuneles_ocupados[tunelElegido] = true;
//        locks_tuneles[tunelElegido].unlock();//Lo soltamos mientras pasa para que puedan esperar los de fuera o los de dentro
//
//        sleep(1000); // Pasan de lado a lado
//        locks_tuneles[tunelElegido].lock();
//
//        zonaTuneles.tuneles[tunelElegido].remove(humano);
//        tuneles_ocupados[tunelElegido] = false;
//
//        while (quierenVolver > 0) {
//            espera_vuelta[tunelElegido].signal();//Monitor
//            espera_salida[tunelElegido].await();
//            //Espera a que vuelva el que queria volver antes de dejar a otro entrar con el sem_tuneles.release();
//
//        }
//        locks_tuneles[tunelElegido].unlock();
//        sem_Tuneles[tunelElegido].release(); // Antes del release, dejamos pasar a los que quieran volver
//        //Humano "id" ha pasado a la zona: "tunelElegido"
//    }
//
//    public void pasarTunelVuelta(int tunelElegido, Humano humano) throws InterruptedException {
//        quierenVolver++; // Ponemos a true
//        locks_tuneles[tunelElegido].lock();
//        while (tuneles_ocupados[tunelElegido]) {
//            espera_vuelta[tunelElegido].await();
//            //Esperamos a que el tunel se libere
//
//        }
//        zonaTuneles.tuneles[tunelElegido].add(humano);
//        locks_tuneles[tunelElegido].unlock();
//
//        sleep(1000);                                ///Pasa por el tunel
//        locks_tuneles[tunelElegido].lock();
//
//        zonaTuneles.tuneles[tunelElegido].remove(humano);
//        espera_salida[tunelElegido].signal();
//        quierenVolver--;
//        locks_tuneles[tunelElegido].unlock();
//    }
//
//    public void entrarZonaDescanso(Humano humano) {
//        lockZonaDescanso.lock();
//        zonaRefugio.zonaDescanso.add(humano);
//        lockZonaDescanso.unlock();
//        if (humano.getComida() > 0) {
//            zonaRefugio.addComida(humano.getComida());
//            humano.setComida(0);
//            lockZonaComedor.lock();
//            esperarComida.signal();
//            //Avisamos a 2 humanos porque siempre se trae 2 de comida y cada uno come solo 1.
//            esperarComida.signal();
//            lockZonaComedor.unlock();
//        }
//    }
//
//
//    public void salirZonaDescanso(Humano humano) {
//        lockZonaDescanso.lock();
//        zonaRefugio.zonaDescanso.remove(humano);
//        lockZonaDescanso.unlock();
//    }
//
//
//    public void entrarZonaRiesgo(int tunelTomado, Humano humano) throws InterruptedException {
//        lockZonaRiesgo.lock();
//        zonaRiesgo.zonas[tunelTomado].add(humano);
//        humano.setComida(2);
//        lockZonaRiesgo.unlock();
//    }
//
//    public void salirZonaRiesgo(int tunelTomado, Humano humano) throws InterruptedException {
//        lockZonaRiesgo.lock();
//        zonaRiesgo.zonas[tunelTomado].remove(humano);
//        lockZonaRiesgo.unlock();
//    }
//
//
//    public void entrarZonaComedor(Humano humano) throws InterruptedException {
//        lockZonaComedor.lock();
//        zonaRefugio.zonaComedor.add(humano);
//        while (zonaRefugio.getAlmacen_comida() <= 0) {
//            esperarComida.await();
//        }
//
//        zonaRefugio.takeComida(humano.getComida());
//        lockZonaComedor.unlock();
//    }
//
//
//    public void salirZonaComedor(Humano humano) throws InterruptedException {
//        lockZonaComedor.lock();
//        zonaRefugio.zonaComedor.remove(humano);
//        zonaRefugio.addComida(humano.getComida());
//    }
//
//    public void entrarZonaComun(Humano humano) {
//        lockZonaComun.lock();
//        zonaRefugio.zonaComun.add(humano);
//        lockZonaComun.unlock();
//    }
//
//
//    /// FUNCIONES PARA ZOMBIE
//    public int cambiarDeZona(int zonaInicial, Zombie zombie) {
//        lockZonaRiesgo.lock();
//        zonaRiesgo.zonas[zonaInicial].remove(zombie);
//        int nueva_zona = random.nextInt(4);
//        zonaRiesgo.zonas[nueva_zona].add(zombie);
//        lockZonaRiesgo.unlock();
//        return nueva_zona;
//    }
//
//    public void atacar(Zombie zombie, int zonaZombie) throws InterruptedException {
//        boolean dejarDeAtacar = false;
//        Humano humanoAtacado = null;
//
//        synchronized (this) {
//            ArrayList<Humano> posiblesAtaques = new ArrayList<Humano>();
//            for (Thread individuo : zonaRiesgo.zonas[zonaZombie]) {
//                if (individuo.getClass() == Humano.class) {
//                    posiblesAtaques.add((Humano) individuo); // Los metemos si es humano
//                }
//            }
//            if (!posiblesAtaques.isEmpty()) {//Solo atacamos si hay humanos
//                boolean encontrado = false;
//                while (!encontrado) {//Buscamos humano para atacar mientras haya en la zona de riesgo.
//                    humanoAtacado = posiblesAtaques.get(random.nextInt(posiblesAtaques.size()));
//                    if (!humanoAtacado.getSiendoAtacado()) {//Si no esta siendo atacado, empezamos a atacarle
//                        humanoAtacado.setSiendoAtacado();
//                        encontrado = true;
//                    } else { // Volvemos a buscar
//                        posiblesAtaques = new ArrayList<Humano>();
//                        for (Thread individuo : zonaRiesgo.zonas[zonaZombie]) {
//                            if (individuo.getClass() == Humano.class) {
//                                posiblesAtaques.add((Humano) individuo); // Los metemos si es humano
//                            }
//                        }
//                        if (posiblesAtaques.isEmpty()) {
//                            dejarDeAtacar = true;
//                            encontrado = true;
//                        }
//                    }
//                }
//            }
//        }
//        if (!dejarDeAtacar) {//Por si no hay ya para atacar
//            if (humanoAtacado != null) {//Si hemos encontrado humano para atacar, le atacamos
//                boolean gana = (((int) (Math.random() * 3)) == 0);
//                if (gana) {
//                    humanoAtacado.interrupt();// Matamos al humano
//                    Thread.sleep(random.nextInt(1000) + 500);//Tarda un tiempo de 1 a 1,5 segundos en atacarlo, (antes de eliminarlo)
//                    zombie.sumarKills();
//                    zonaRiesgo.zonas[zonaZombie].remove(humanoAtacado);
//                    String idZombieNuevo = ("Z" + humanoAtacado.getIde().substring(1, 4));
//
//                    Zombie zombieNuevo = new Zombie(idZombieNuevo, this, zonaZombie);
//
//                    zombieNuevo.start();
//                } else {
//                    int milisAtaque = random.nextInt(1000) + 500;//Representa el tiempo que luchan humano contra zombie
//                    humanoAtacado.sleep(milisAtaque);
//                    humanoAtacado.marcarHumano();
//                    humanoAtacado.setComida(0);//Pierde la comida al defenderse
//                    sleep(milisAtaque);
//                    pasarTunelVuelta(zonaZombie, humanoAtacado); //Humano vuelve inmediatamente si han intentado atacarle
//                }
//            }
//        }
//    }
}

