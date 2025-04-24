module programacionconcurrenteydistrubuida.proyectoapocalipsiszombie {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens programacionconcurrenteydistrubuida.proyectoapocalipsiszombie to javafx.fxml;
    exports programacionconcurrenteydistrubuida.proyectoapocalipsiszombie;
}