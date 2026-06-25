package simulacionEcosistema.modelo;

/**
 * Excepción personalizada para errores de validación en el
 * Sistema de Simulación de Ecosistemas.
 * Se lanza cuando un dato ingresado no cumple las reglas del negocio.
 */
public class EcoSimException extends Exception {

    public EcoSimException(String mensaje) {
        super(mensaje);
    }

    public EcoSimException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
