package simulacionEcosistema.modelo;

public class Administrador extends Usuario {

    /**
     * Crea un administrador heredando las validaciones de Usuario.
     *
     * @throws EcoSimException si algún campo es inválido (hereda de Usuario).
     */
    public Administrador(String nombreCompleto, String cedula, String correo,
                         String nombreUsuario, String contrasena, String estado)
            throws EcoSimException {
        super(nombreCompleto, cedula, correo, nombreUsuario, contrasena, estado);
    }

    /**
     * Muestra el reporte de un estudiante supervisado.
     *
     * @throws EcoSimException si el estudiante es nulo.
     */
    public void supervisarEstudiante(Estudiante est) throws EcoSimException {
        if (est == null) {
            throw new EcoSimException("No se puede supervisar un estudiante nulo.");
        }
        System.out.println("=== Supervision de Estudiante ===");
        System.out.println("Nombre   : " + est.getNombreCompleto());
        System.out.println("Cedula   : " + est.getCedula());
        System.out.println("Paralelo : " + est.getParalelo());
        System.out.println("Estado   : " + est.getEstado());
        System.out.println("Simulaciones completadas: " + est.getSimulacionesCompletadas());
        System.out.println("Recompensa actual: " + est.obtenerRecompensa());
    }
}
