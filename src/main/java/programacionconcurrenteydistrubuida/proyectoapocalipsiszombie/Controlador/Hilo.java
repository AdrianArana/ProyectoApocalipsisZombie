package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Controlador;

public class Hilo extends Thread {
    private boolean parado = false;

    @Override
    public void run() {
        while (true) {
            for (int i = 0; i < 4; i++)
                System.out.print("h");
            if (parado) {
                try {
                    wait();
                    parado = false;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void parar() throws InterruptedException {
        parado = true;
    }
}
