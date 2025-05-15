package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIInterfaz extends Remote {
    boolean getIniciado() throws  RemoteException;
    String getMejoresZombies() throws RemoteException;
    int getNumeroHumanosRefugio() throws RemoteException;
    int getNumeroHumanosTunel(int tunel) throws RemoteException;
    int getNumeroHumanosZonaRiesgo(int zona) throws RemoteException;
    int getNumeroZombiesZonaRiesgo(int zona) throws RemoteException;
    void setParado(boolean b) throws RemoteException;
    int getNumeroHumanosEsperaIda(int i) throws RemoteException;
    int getNumeroHumanosEsperaVuelta(int i) throws RemoteException;

}
