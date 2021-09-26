module org.waoss.k8 {
    requires kotlin.stdlib;
    requires slf4j.api;
    requires kotlinx.coroutines.core.jvm;
    exports org.waoss.k8;
    exports org.waoss.k8.gpu;
    exports org.waoss.k8.cpu;
    exports org.waoss.k8.io;
    exports org.waoss.k8.input;
}