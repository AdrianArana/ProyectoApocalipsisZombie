package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo;

public class Main {
    public static void comenzarApocalipsis() {
        ZonaRefugio zonaRefugio = new ZonaRefugio();
        ZonaRiesgo zonaRiesgo = new ZonaRiesgo();
        ZonaTuneles zonaTuneles = new ZonaTuneles();
        Mapa mapa = new Mapa(zonaRefugio,zonaRiesgo,zonaTuneles);
        for (int i = 0; i < 10000; i++) {
            String id = String.format("H%04d", i);
            System.out.println(id);
            Humano humano = new Humano(mapa,id);
            humano.start();
        }
        Zombie zombie = new Zombie(("Z0000"),mapa,0);
        zombie.start();
    }
    public static void main(String[] args) {
        comenzarApocalipsis();
    }
}
