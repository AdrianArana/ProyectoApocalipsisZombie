package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo;

public class Zombie extends Thread {
    private String id; //Z____

    @Override
    public void run() {
        //elige zona aleatoria (1-4)
        // hay algun humano para atacar??
                //si:
                    //selecciona uno al azar
                    //le ataca (.interrupt())
                        //.sleep([2-3sec])
                            //si gana: (1/3posibilidades)
                                    //lista.remove(humanomatado) y lista.add(zombinuevo) ZOMBIE CON ID Zhhhh del humano
                            //si pierde: (2/3posibilidades)
                                    //.start() de nuevo y le quitas la comida y se le marca
    }
}
