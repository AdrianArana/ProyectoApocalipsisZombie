package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class Mapa {
    ZonaRefugio zonaRefugio;
    ZonaRiesgo zonaRiesgo;
    ZonaTuneles zonaTuneles;
    private boolean pausado;
    private int killsTotales;
    private int numeroHumanos;
    private int mayoresKills;
    private String mejorZombie;

    public Mapa() {
        this.zonaRefugio = new ZonaRefugio();
        this.zonaRiesgo = new ZonaRiesgo();
        this.zonaTuneles = new ZonaTuneles();
        pausado = false;
        killsTotales = 0;
        numeroHumanos = 0;
        mayoresKills = 0;
        mejorZombie = "_____";
        quierenVolver = new int[4];
    }

    public int getMayoresKills() {
        return mayoresKills;
    }

    public void setMayoresKills(int mayoresKills) {
        this.mayoresKills = mayoresKills;
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
    private final int[] quierenVolver;
    CyclicBarrier[] cb_tuneles = {
            new CyclicBarrier(3),
            new CyclicBarrier(3),
            new CyclicBarrier(3),
            new CyclicBarrier(3)
    };

    Lock[] lockZonaRiesgo = new Lock[]{new ReentrantLock(), new ReentrantLock(), new ReentrantLock(), new ReentrantLock()};
    Lock lockZonaDescanso = new ReentrantLock();
    Lock lockZonaComedor = new ReentrantLock();
    Lock lockZonaComun = new ReentrantLock();
    Lock[] locks_tuneles = new Lock[]{new ReentrantLock(), new ReentrantLock(), new ReentrantLock(), new ReentrantLock()};


    Condition[] espera_ida = new Condition[]{locks_tuneles[0].newCondition(), locks_tuneles[1].newCondition(), locks_tuneles[2].newCondition(), locks_tuneles[3].newCondition()};
    //Para que esperen los individuos a que pasen los que vueven al salir a la zona de riesgo
    Condition[] espera_vuelta = new Condition[]{locks_tuneles[0].newCondition(), locks_tuneles[1].newCondition(), locks_tuneles[2].newCondition(), locks_tuneles[3].newCondition()};

    boolean[] tuneles_ocupados = new boolean[]{false, false, false, false};

    Condition esperarComida = lockZonaComedor.newCondition();

    //HUMANO:
    public void pasarTunelIda(int tunelElegido, Humano humano) throws BrokenBarrierException, InterruptedException {
        cb_tuneles[tunelElegido].await();
        zonaTuneles.zonasEspera[tunelElegido].add(humano);

        zonaRefugio.zonaComun.remove(humano);
        locks_tuneles[tunelElegido].lock();

        while (tuneles_ocupados[tunelElegido]) {
            espera_ida[tunelElegido].await();//wait o await? todo
        }

        tuneles_ocupados[tunelElegido] = true;
        zonaTuneles.zonasEspera[tunelElegido].remove(humano);


        zonaTuneles.tuneles[tunelElegido].add(humano);

        sleep(1000);

        zonaTuneles.tuneles[tunelElegido].remove(humano);
        tuneles_ocupados[tunelElegido] = false;

        if (quierenVolver[tunelElegido] > 0) {
            espera_vuelta[tunelElegido].signal();
        } else {
            espera_ida[tunelElegido].signal();
        }
        locks_tuneles[tunelElegido].unlock();
    }

    public synchronized void pasarTunelVuelta(int tunelElegido, Humano humano) throws InterruptedException {
        quierenVolver[tunelElegido]++;
        locks_tuneles[tunelElegido].lock();
        while (tuneles_ocupados[tunelElegido]) {
            espera_vuelta[tunelElegido].wait();
        }


        tuneles_ocupados[tunelElegido] = true;
        System.out.println("[RIESGO] Humano " + humano.getIde() + " sale de la zona de riesgo " + tunelElegido);
        zonaRiesgo.zonas[tunelElegido].remove(humano);

        zonaTuneles.tuneles[tunelElegido].add(humano);
        sleep(1000);
        zonaTuneles.tuneles[tunelElegido].remove(humano);
        tuneles_ocupados[tunelElegido] = false;
        quierenVolver[tunelElegido]--;

        if (quierenVolver[tunelElegido] > 0) {
            espera_vuelta[tunelElegido].signal();
        } else {
            espera_ida[tunelElegido].signal();
        }
        locks_tuneles[tunelElegido].unlock();
    }

    /*
        public void pasarTunelIdaa(int tunelElegido, Humano humano) throws BrokenBarrierException, InterruptedException {
            System.out.println("[TUNEL IDA] Humano " + humano.getIde() + " esperando barrera en túnel " + tunelElegido);
            cb_tuneles[tunelElegido].await();
            System.out.println("[TUNEL IDA] Humano " + humano.getIde() + " esperando semáforo túnel " + tunelElegido);
            sem_Tuneles[tunelElegido].acquire();
            locks_tuneles[tunelElegido].lock();
            try {
                System.out.println("[TUNEL IDA] Humano " + humano.getIde() + " entra al túnel " + tunelElegido);
                zonaRefugio.zonaComun.remove(humano);
                zonaTuneles.tuneles[tunelElegido].add(humano);
                tuneles_ocupados[tunelElegido] = true;
            } finally {
                locks_tuneles[tunelElegido].unlock();
            }

            sleep(1000);

            locks_tuneles[tunelElegido].lock();
            try {
                zonaTuneles.tuneles[tunelElegido].remove(humano);
                tuneles_ocupados[tunelElegido] = false;

                while (quierenVolver[tunelElegido] > 0) {
                    espera_sala_intermedia[tunelElegido].await();
                    espera_ida[tunelElegido].signal();
                }
            } finally {
                locks_tuneles[tunelElegido].unlock();
            }
            sem_Tuneles[tunelElegido].release();
            System.out.println("[TUNEL IDA] Humano " + humano.getIde() + " ha cruzado el túnel " + tunelElegido);
        }

        public void pasarTunelVueltaa(int tunelElegido, Humano humano) throws InterruptedException {
            System.out.println("[TUNEL VUELTA] Humano " + humano.getIde() + " quiere volver por túnel " + tunelElegido);
            quierenVolver[tunelElegido]++;
            locks_tuneles[tunelElegido].lock();
            try {
                while (tuneles_ocupados[tunelElegido]) {
                    espera_ida[tunelElegido].await();
                }

                System.out.println("[TUNEL VUELTA] Humano " + humano.getIde() + " entrando en túnel " + tunelElegido);
                zonaTuneles.tuneles[tunelElegido].add(humano);
            } finally {
                locks_tuneles[tunelElegido].unlock();
            }

            sleep(1000);

            locks_tuneles[tunelElegido].lock();
            try {
                zonaTuneles.tuneles[tunelElegido].remove(humano);
                espera_sala_intermedia[tunelElegido].signal();
                quierenVolver[tunelElegido]--;
            } finally {
                locks_tuneles[tunelElegido].unlock();
            }
            System.out.println("[TUNEL VUELTA] Humano " + humano.getIde() + " ha cruzado el túnel de vuelta " + tunelElegido);
        }
    */
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
        lockZonaRiesgo[tunelTomado].lock();
        zonaRiesgo.zonas[tunelTomado].add(humano);
        humano.setComida(2);
        System.out.println("[RIESGO] Humano " + humano.getIde() + " entra a la zona de riesgo " + tunelTomado);
        lockZonaRiesgo[tunelTomado].unlock();
    }

    public void entrarZonaComedor(Humano humano) throws InterruptedException {
        lockZonaComedor.lock();
        zonaRefugio.zonaComedor.add(humano);
        System.out.println("[COMEDOR] Humano " + humano.getIde() + " entra al comedor.");
        while (zonaRefugio.getAlmacen_comida() <= 0) {
            System.out.println("[COMEDOR] Humano " + humano.getIde() + " espera comida.");
            esperarComida.await();
        }
        zonaRefugio.takeComida();
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

    //ZOMBIE
    public int cambiarDeZona(int zonaInicial, Zombie zombie) {
        lockZonaRiesgo[zonaInicial].lock();
        zonaRiesgo.zonas[zonaInicial].remove(zombie);
        lockZonaRiesgo[zonaInicial].unlock();

        int nueva_zona = random.nextInt(4);
        lockZonaRiesgo[nueva_zona].lock();

        zonaRiesgo.zonas[nueva_zona].add(zombie);
        lockZonaRiesgo[nueva_zona].unlock();
        return nueva_zona;
    }

    public void atacar(Zombie zombie, int zonaZombie) throws InterruptedException {
        boolean dejarDeAtacar = false;
        Humano humanoAtacado = null;

        //System.out.println("[DEBUG] Zombie " + zombie.getIde() + " buscando humano para ataque en zona " + zonaZombie);

        synchronized (zonaRiesgo.zonas[zonaZombie]) {
            ArrayList<Humano> posiblesAtaques = new ArrayList<>();
            ArrayList<Thread> copia = new ArrayList<>(zonaRiesgo.zonas[zonaZombie]);
            for (Thread individuo : copia) {
                if (individuo instanceof Humano) {
                    if (!((Humano) individuo).getSiendoAtacado()) {
                        posiblesAtaques.add((Humano) individuo);
                    }
                }
            }

            //System.out.println("[DEBUG] Humanos posibles para ataque: " + posiblesAtaques.size());

            if (!posiblesAtaques.isEmpty()) {
                boolean encontrado = false;
                while (!encontrado) {
                    humanoAtacado = posiblesAtaques.get(random.nextInt(posiblesAtaques.size()));
                    // System.out.println("[DEBUG] Zombie " + zombie.getIde() + " intenta atacar a " + humanoAtacado.getIde());

                    synchronized (humanoAtacado.lock) {
                        if (!humanoAtacado.getSiendoAtacado()) {
                            humanoAtacado.setSiendoAtacado(true);
                            //System.out.println("[DEBUG] Humano " + humanoAtacado.getIde() + " marcado como siendo atacado.");
                            encontrado = true;
                        } else {
                            //System.out.println("[DEBUG] Humano " + humanoAtacado.getIde() + " ya estaba siendo atacado. Reintentando...");
                            posiblesAtaques.clear();

                            synchronized (lockZonaRiesgo[zonaZombie]) {
                                for (Thread individuo : zonaRiesgo.zonas[zonaZombie]) {
                                    if (individuo instanceof Humano) {
                                        posiblesAtaques.add((Humano) individuo);
                                    }
                                }
                            }

                            if (posiblesAtaques.isEmpty()) {
                                // System.out.println("[DEBUG] No quedan humanos disponibles para atacar en zona " + zonaZombie);
                                dejarDeAtacar = true;
                                encontrado = true;
                            }
                        }
                    }
                }
            }

            if (!dejarDeAtacar && humanoAtacado != null) {
                synchronized (humanoAtacado.lock) {
                    boolean pierde = (((int) (Math.random() * 3)) == 0);

                    if (!pierde) {
                        //System.out.println("[ATAQUE] Zombie " + zombie.getIde() + " mata a humano " + humanoAtacado.getIde());
                        humanoAtacado.morir(true);
                        restarNumeroHumanos(1);
                        Thread.sleep(random.nextInt(1000));//todo
                        zombie.sumarKills();
                        if (zombie.getNumeroKills() > mayoresKills) {
                            mejorZombie = zombie.getIde();
                            setMayoresKills(zombie.getNumeroKills());
                        }
                        sumarKillsTotales();
                        zonaRiesgo.zonas[zonaZombie].remove(humanoAtacado);

                        String idZombieNuevo = "Z" + humanoAtacado.getIde().substring(1, 5);
                        //System.out.println("[DEBUG] Se crea nuevo zombie con ID " + idZombieNuevo);
                        Zombie zombieNuevo = new Zombie(idZombieNuevo, this, zonaZombie);
                        zombieNuevo.start();
                    } else {
                        // System.out.println("[ATAQUE] Humano " + humanoAtacado.getIde() + " sobrevive al ataque del zombie " + zombie.getIde());
                        humanoAtacado.setSiendoAtacado(false);

                        int milisAtaque = random.nextInt(1000);//todo
                        humanoAtacado.sleep(milisAtaque);
                        humanoAtacado.marcarHumano();
                        humanoAtacado.setComida(0);
                        sleep(milisAtaque);
                        //System.out.println("[DEBUG] Humano " + humanoAtacado.getIde() + " huye por túnel tras sobrevivir.");
                        pasarTunelVuelta(zonaZombie, humanoAtacado);
                    }
                }
            }
        }
    }

    private void sumarKillsTotales() {
        killsTotales++;
    }

    public synchronized void verificarPausa() {
        while (pausado) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void pausarHilos() {
        pausado = true;
    }

    public synchronized void reanudarHilos() {
        pausado = false;
        notifyAll();
    }

    public int getNumeroKills() {
        return killsTotales;
    }

    public int getNumeroHumanos() {
        return numeroHumanos;
    }

    public synchronized void sumarNumeroHumanos(int i) {
        numeroHumanos += i;
    }

    public synchronized void restarNumeroHumanos(int i) {
        numeroHumanos -= i;
    }

    public String getMejorZombie() {
        return mejorZombie;
    }
}
    /*public void atacarnuevo(Zombie zombie, int zonaZombie) throws InterruptedException {
        ArrayList<Humano> posAt = new ArrayList<>();
        ArrayList<Thread> copia = new ArrayList<>(zonaRiesgo.zonas[zonaZombie]);

        for (Thread individuo : copia) {
            if (individuo instanceof Humano) {
                if (!((Humano) individuo).getSiendoAtacado()) {
                    posAt.add((Humano) individuo);
                }
            }
        }
        Humano humanoAtacado = posAt.get(random.nextInt(posAt.size()));
        int milisAtaque = 1000 + random.nextInt(500);
        if ((((int) (Math.random() * 3)) == 0)) {
            humanoAtacado.morir(true);
            humanoAtacado.dormir(milisAtaque);
            zombie.dormir(milisAtaque);
            zombie.sumarKills();
            zonaRiesgo.zonas[zonaZombie].remove(humanoAtacado);
            String idZombieNuevo = "Z" + humanoAtacado.getIde().substring(1, 5);
            Zombie zombieNuevo = new Zombie(idZombieNuevo, this, zonaZombie);
            zombieNuevo.start();
        } else {
            humanoAtacado.marcarHumano();
            humanoAtacado.setComida(0);
            humanoAtacado.dormir(milisAtaque);
            zombie.dormir(milisAtaque);
            pasarTunelVuelta(zonaZombie, humanoAtacado);
        }

    }


    public void atacarr(Zombie zombie, int zonaZombie) throws InterruptedException {
        boolean dejarDeAtacar = false;
        Humano humanoAtacado = null;

        //System.out.println("[DEBUG] Zombie " + zombie.getIde() + " buscando humano para ataque en zona " + zonaZombie);

        synchronized (zonaRiesgo.zonas[zonaZombie]) {
            ArrayList<Humano> posiblesAtaques = new ArrayList<>();
            ArrayList<Thread> copia = new ArrayList<>(zonaRiesgo.zonas[zonaZombie]);
            for (Thread individuo : copia) {
                if (individuo instanceof Humano) {
                    if (!((Humano) individuo).getSiendoAtacado()) {
                        posiblesAtaques.add((Humano) individuo);
                    }
                }
            }

            //System.out.println("[DEBUG] Humanos posibles para ataque: " + posiblesAtaques.size());

            if (!posiblesAtaques.isEmpty()) {
                boolean encontrado = false;
                while (!encontrado) {
                    humanoAtacado = posiblesAtaques.get(random.nextInt(posiblesAtaques.size()));
                    // System.out.println("[DEBUG] Zombie " + zombie.getIde() + " intenta atacar a " + humanoAtacado.getIde());

                    synchronized (humanoAtacado.lock) {
                        if (!humanoAtacado.getSiendoAtacado()) {
                            humanoAtacado.setSiendoAtacado(true);
                            //System.out.println("[DEBUG] Humano " + humanoAtacado.getIde() + " marcado como siendo atacado.");
                            encontrado = true;
                        } else {
                            //System.out.println("[DEBUG] Humano " + humanoAtacado.getIde() + " ya estaba siendo atacado. Reintentando...");
                            posiblesAtaques.clear();

                            synchronized (lockZonaRiesgo[zonaZombie]) {
                                for (Thread individuo : zonaRiesgo.zonas[zonaZombie]) {
                                    if (individuo instanceof Humano) {
                                        posiblesAtaques.add((Humano) individuo);
                                    }
                                }
                            }

                            if (posiblesAtaques.isEmpty()) {
                                // System.out.println("[DEBUG] No quedan humanos disponibles para atacar en zona " + zonaZombie);
                                dejarDeAtacar = true;
                                encontrado = true;
                            }
                        }
                    }
                }
            }

            if (!dejarDeAtacar && humanoAtacado != null) {
                synchronized (humanoAtacado.lock) {
                    boolean gana = (((int) (Math.random() * 3)) == 0);
                    int milisAtaque = 1000 + random.nextInt(500);

                    if (gana) {
                        //System.out.println("[ATAQUE] Zombie " + zombie.getIde() + " mata a humano " + humanoAtacado.getIde());
                        humanoAtacado.morir(true);
                        //Thread.sleep(milisAtaque);
                        humanoAtacado.dormir(milisAtaque);
                        zombie.sumarKills();
                        zonaRiesgo.zonas[zonaZombie].remove(humanoAtacado);


                        String idZombieNuevo = "Z" + humanoAtacado.getIde().substring(1, 5);
                        //System.out.println("[DEBUG] Se crea nuevo zombie con ID " + idZombieNuevo);
                        Zombie zombieNuevo = new Zombie(idZombieNuevo, this, zonaZombie);
                        zombieNuevo.start();
                    } else {
                        // System.out.println("[ATAQUE] Humano " + humanoAtacado.getIde() + " sobrevive al ataque del zombie " + zombie.getIde());
                        humanoAtacado.setSiendoAtacado(true);
                        humanoAtacado.marcarHumano();
                        humanoAtacado.setComida(0);
                        humanoAtacado.dormir(milisAtaque);
                        //sleep(milisAtaque);
                        //System.out.println("[DEBUG] Humano " + humanoAtacado.getIde() + " huye por túnel tras sobrevivir.");
                        //humanoAtacado.verificarPausa();
                        pasarTunelVuelta(zonaZombie, humanoAtacado);
                    }
                }
            }
        }

        //System.out.println("[DEBUG] Zombie " + zombie.getIde() + " finalizó intento de ataque en zona " + zonaZombie);
    }
*/