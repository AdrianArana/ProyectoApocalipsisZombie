module programacionconcurrenteydistrubuida.proyectoapocalipsiszombie {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.rmi;
    requires java.management.rmi;
    requires java.desktop;
    requires java.logging;

    opens programacionconcurrenteydistrubuida.proyectoapocalipsiszombie to javafx.fxml;
    exports programacionconcurrenteydistrubuida.proyectoapocalipsiszombie;
    exports programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Controlador;
    opens programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Controlador to javafx.fxml;
    exports programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo;
    opens programacionconcurrenteydistrubuida.proyectoapocalipsiszombie.Modelo to javafx.fxml;
}