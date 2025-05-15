package programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Config_log {
    private static final Logger log= Logger.getLogger("ResgistroApocalipsis");
    static{
        configurarLogger();
    }

    private static void configurarLogger(){
        try{
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String nombreArchivo = "registro_" + timestamp + ".txt";

            FileHandler archivoLog = new FileHandler(nombreArchivo, false);
            archivoLog.setFormatter(new SimpleFormatter());
            archivoLog.setLevel(Level.ALL);

            log.setUseParentHandlers(false);
            log.addHandler(archivoLog);
            log.setLevel(Level.ALL);
        }catch (IOException e){
            System.err.println("Error al inicializar el sistema de losgs: "+e.getMessage());

        }


    }
    public static Logger getLogger(){
        return log;
    }
}