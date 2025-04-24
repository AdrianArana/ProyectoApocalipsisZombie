package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo;

public class Humano extends Thread {
    private String id; //H____
    Mapa mapa;

    @Override
    public void run() {

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
                                // vuelve inmediatamente a un tunel [1s] SIN COMIDA
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
