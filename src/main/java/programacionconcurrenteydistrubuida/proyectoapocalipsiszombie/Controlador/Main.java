package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Controlador;

import static java.lang.Thread.sleep;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Hilo h = new Hilo();
        h.start();

        for (int i = 0; i < 100; i++) {
            sleep(500);

            h.parar();
        }
    }
}
