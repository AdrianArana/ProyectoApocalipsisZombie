package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface RMIInterfaz extends Remote {
    boolean getIniciado() throws  RemoteException;
    String getMejoresZombies() throws RemoteException;
    int getNumeroHumanosRefugio() throws RemoteException;
    int getNumeroHumanosTunel(int tunel) throws RemoteException;
    int getNumeroHumanosZonaRiesgo(int zona) throws RemoteException;
    int getNumeroZombiesZonaRiesgo(int zona) throws RemoteException;
    void setParado(boolean b) throws RemoteException;
    void finalizar() throws RemoteException;
    int getNumeroHumanosEsperaIda(int i) throws RemoteException;
    int getNumeroHumanosEsperaVuelta(int i) throws RemoteException;

}
