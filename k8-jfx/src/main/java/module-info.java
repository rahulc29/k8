module org.waoss.k8.jfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.waoss.k8;
    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens org.waoss.k8.jfx to javafx.fxml;
    exports org.waoss.k8.jfx;
}