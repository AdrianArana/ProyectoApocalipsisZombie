package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Mapa {
    ZonaRefugio zonaRefugio;
    ZonaRiesgo zonaRiesgo;
    ZonaTuneles zonaTuneles;

    CyclicBarrier cb_Tunel1;
    CyclicBarrier cb_Tunel2;
    CyclicBarrier cb_Tunel3;
    CyclicBarrier cb_Tunel4;

    Semaphore sem_Tunel1 = new Semaphore(1, true);
    Semaphore sem_Tunel2;
    Semaphore sem_Tunel3;
    Semaphore sem_Tunel4;

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
}
