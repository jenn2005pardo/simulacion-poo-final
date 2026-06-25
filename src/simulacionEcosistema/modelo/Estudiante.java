package simulacionEcosistema.modelo;

public class Estudiante extends Usuario {
    private String paralelo;
    private int simulacionesCompletadas;

    /**
     * Crea un estudiante con validación del paralelo.
     *
     * @throws EcoSimException si el paralelo está vacío o hereda error de Usuario.
     */
    public Estudiante(String nombreCompleto, String cedula, String correo,
                      String nombreUsuario, String contrasena, String estado,
                      String paralelo) throws EcoSimException {
        super(nombreCompleto, cedula, correo, nombreUsuario, contrasena, estado);

        if (paralelo == null || paralelo.trim().isEmpty()) {
            throw new EcoSimException("El paralelo del estudiante no puede estar vacío.");
        }

        this.paralelo                = paralelo.trim().toUpperCase();
        this.simulacionesCompletadas = 0;
    }

    /** Registra una simulación completada. */
    public void registrarSimulacion() {
        simulacionesCompletadas++;
        System.out.println("Simulacion registrada para el estudiante " + nombreCompleto
                + ". Total completadas: " + simulacionesCompletadas);
    }

    /**
     * Calcula la recompensa según los turnos jugados en la simulación actual.
     *
     * @throws EcoSimException si el número de turnos es negativo.
     */
    public String obtenerRecompensa(int turnosJugados) throws EcoSimException {
        if (turnosJugados < 0) {
            throw new EcoSimException(
                "El número de turnos jugados no puede ser negativo. Valor recibido: " + turnosJugados);
        }
        if (turnosJugados >= 10) {
            return "🏆 Recompensa GOLD: Ecosistema dominado. ¡Excelente manejo del ecosistema!";
        } else if (turnosJugados >= 5) {
            return "🥈 Recompensa SILVER: Buen progreso. ¡Sigue aprendiendo!";
        } else if (turnosJugados >= 1) {
            return "🥉 Recompensa BRONZE: Primeros pasos. ¡Inicia mas simulaciones!";
        } else {
            return "Sin recompensa. No se ejecutaron turnos.";
        }
    }

    /** Recompensa basada en el historial acumulado del estudiante. */
    public String obtenerRecompensa() throws EcoSimException {
        return obtenerRecompensa(simulacionesCompletadas);
    }

    /**
     * Establece el paralelo del estudiante.
     *
     * @throws EcoSimException si el paralelo está vacío.
     */
    public void setParalelo(String paralelo) throws EcoSimException {
        if (paralelo == null || paralelo.trim().isEmpty()) {
            throw new EcoSimException("El paralelo no puede estar vacío.");
        }
        this.paralelo = paralelo.trim().toUpperCase();
    }

    public String getParalelo()                { return paralelo; }
    public int getSimulacionesCompletadas()    { return simulacionesCompletadas; }
}
