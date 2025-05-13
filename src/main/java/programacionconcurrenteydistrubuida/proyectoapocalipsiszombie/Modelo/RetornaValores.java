package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;


public class RetornaValores extends UnicastRemoteObject implements RMIInterfaz {
    private Mapa mapa;

    public RetornaValores(Mapa mapa) throws RemoteException {
        super();
        this.mapa = mapa;

    }


    @Override
    public boolean getIniciado() throws RemoteException {
        return mapa.getIniciado();
    }

    @Override
    public String getMejoresZombies() {

        ArrayList<Zombie> mejoresZombies = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                for (Thread z : mapa.zonaRiesgo.zonas[i]) {
                    if (z instanceof Zombie) {
                        mejoresZombies.add((Zombie) z);
                    }
                }
            }
        mejoresZombies.sort((z1, z2) -> Integer.compare(z2.getNumeroKills(), z1.getNumeroKills()));
        List<Zombie> mejoresZombiesTop3 = mejoresZombies.subList(0, Math.min(3, mejoresZombies.size()));
        StringBuilder ranking = new StringBuilder();
        for (Zombie zombie : mejoresZombiesTop3) {
            ranking.append(zombie.getIde()).append(" - ").append(zombie.getNumeroKills()).append(" muertes.\n");
        }
        return ranking.toString();}


    @Override
    public int getNumeroHumanosRefugio() {
        return mapa.getZonaRefugio().getNumeroHumanos();
    }

    @Override
    public int getNumeroHumanosTunel(int tunel) {
        return mapa.getZonaTuneles().getTuneles()[tunel].size();
    }

    @Override
    public int getNumeroHumanosZonaRiesgo(int zona) {
        int h = 0;
        synchronized (mapa.getZonaRiesgo().getZonas()[zona]) {
            for (Thread t : mapa.getZonaRiesgo().getZonas()[zona]) {
                if (t instanceof Humano) {
                    h++;
                }
            }
        }
        return h;
    }

    @Override
    public int getNumeroZombiesZonaRiesgo(int zona) {
        int z = 0;
        synchronized (mapa.getZonaRiesgo().getZonas()[zona]) {
            for (Thread t : mapa.getZonaRiesgo().getZonas()[zona]) {
                if (t instanceof Zombie) {
                    z++;
                }
            }
        }
        return z;
    }

    @Override
    public void setParado(boolean parao) {
        if (parao) {
            mapa.setPausado(parao);
            mapa.pausarHilos();
        } else {
            mapa.setPausado(parao);
            mapa.reanudarHilos();
        }
    }
}
