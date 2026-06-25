package simulacionEcosistema.negocio;

import simulacionEcosistema.modelo.*;

public class GestorSimulacion {
    private Simulacion simulacionActual;
    private Estudiante estudianteJugando;

    /**
     * Crea el gestor de simulación.
     *
     * @throws EcoSimException si la simulación es nula.
     */
    public GestorSimulacion(Simulacion simulacionActual, Estudiante estudianteJugando)
            throws EcoSimException {
        if (simulacionActual == null) {
            throw new EcoSimException("La simulación no puede ser nula al crear el gestor.");
        }
        this.simulacionActual  = simulacionActual;
        this.estudianteJugando = estudianteJugando;
    }

    /**
     * Inicia (o reinicia) la simulación.
     *
     * @throws EcoSimException si la simulación ya está activa.
     */
    public void iniciar() throws EcoSimException {
        if (simulacionActual.isActiva()) {
            throw new EcoSimException("La simulación ya está en curso. Detenla antes de reiniciar.");
        }
        simulacionActual.reiniciarSimulacion();
        if (estudianteJugando != null) {
            System.out.println("Simulacion iniciada por " + estudianteJugando.getNombreCompleto());
        } else {
            System.out.println("Simulacion iniciada (modo administrador)");
        }
    }

    /**
     * Ejecuta un turno completo de la simulación.
     *
     * @throws EcoSimException si la simulación no está activa o ya finalizó.
     */
    public void ejecutarTurno() throws EcoSimException {
        if (!simulacionActual.isActiva()) {
            throw new EcoSimException("No se puede ejecutar un turno: la simulación no está activa.");
        }
        if (simulacionActual.getTurnoActual() >= simulacionActual.getTiempoTotal()) {
            throw new EcoSimException("No se puede ejecutar un turno: la simulación ya finalizó.");
        }

        // Avanza el turno (valida internamente)
        simulacionActual.avanzarTurno();
        int turno = simulacionActual.getTurnoActual();
        System.out.println("\n--- Turno " + turno + " ---");

        StringBuilder resumen = new StringBuilder("[Turno ").append(turno).append("] ");

        // 1. Actualizar poblaciones (RF4)
        for (Poblacion p : simulacionActual.getPoblaciones()) {
            int antes = p.getCantidad();
            p.actualizarPoblacion();

            // RF8 — alertas de sobrepoblación
            if (p.getCantidad() > p.getLimiteMaximo()) {
                simulacionActual.getEntorno().generarAlertaSobrepoblacion(p.getEspecie().getNombre());
            } else {
                simulacionActual.getEntorno().limpiarAlertaSobrepoblacion(p.getEspecie().getNombre());
            }

            resumen.append(p.getEspecie().getNombre())
                   .append(": ").append(antes).append("->").append(p.getCantidad()).append(" | ");
        }

        // 2. Ejecutar interacciones (RF5 + RF6)
        for (Interaccion i : simulacionActual.getInteracciones()) {
            Poblacion dep   = buscarPoblacion(i.getNombreDepredador());
            Poblacion presa = buscarPoblacion(i.getNombrePresa());
            if (dep != null && presa != null) {
                i.simularCaza(dep, presa); // puede lanzar EcoSimException
            }
        }

        // 3. Consumo de entorno y alertas (RF7 + RF8)
        simulacionActual.getEntorno().calcularConsumo(simulacionActual.getPoblaciones());

        for (String alerta : simulacionActual.getEntorno().getAlertas()) {
            System.out.println("ALERTA AMBIENTAL: " + alerta);
        }

        resumen.append("Alimento: ").append(simulacionActual.getEntorno().getAlimentoDisponible());
        simulacionActual.agregarAlHistorial(resumen.toString());

        mostrarEstadoEcosistema();

        // ¿Finalizó?
        if (simulacionActual.getTurnoActual() == simulacionActual.getTiempoTotal()) {
            terminarManualmente();
        }
    }

    /**
     * Detiene manualmente la simulación y entrega la recompensa al estudiante.
     *
     * @throws EcoSimException si la simulación ya estaba detenida.
     */
    public void terminarManualmente() throws EcoSimException {
        if (!simulacionActual.isActiva()) {
            throw new EcoSimException("La simulación ya está detenida.");
        }
        simulacionActual.setActiva(false);
        System.out.println("\nSimulacion terminada. Turnos jugados: " + simulacionActual.getTurnoActual());

        if (estudianteJugando != null) {
            estudianteJugando.registrarSimulacion();
            String recompensa = estudianteJugando.obtenerRecompensa(simulacionActual.getTurnoActual());
            System.out.println("Recompensa para " + estudianteJugando.getNombreCompleto() + ": " + recompensa);
        }
    }

    /**
     * Registra una nueva población en la simulación.
     *
     * @throws EcoSimException si la población es nula.
     */
    public void registrarPoblacion(Poblacion p) throws EcoSimException {
        if (p == null) {
            throw new EcoSimException("La población a registrar no puede ser nula.");
        }
        simulacionActual.agregarPoblacion(p);
        boolean yaExiste = simulacionActual.getEspecies().stream()
                .anyMatch(e -> e.getNombre().equalsIgnoreCase(p.getEspecie().getNombre()));
        if (!yaExiste) {
            simulacionActual.agregarEspecie(p.getEspecie());
        }
    }

    /**
     * Registra una interacción en la simulación.
     *
     * @throws EcoSimException si la interacción es nula.
     */
    public void registrarInteraccion(Interaccion i) throws EcoSimException {
        if (i == null) {
            throw new EcoSimException("La interacción a registrar no puede ser nula.");
        }
        simulacionActual.agregarInteraccion(i);
    }

    /**
     * Muestra el historial de todos los turnos ejecutados.
     */
    public void mostrarHistorial() {
        java.util.List<String> historial = simulacionActual.getHistorial();
        if (historial.isEmpty()) {
            System.out.println("No hay turnos registrados en el historial.");
        } else {
            System.out.println("\n=== HISTORIAL DE TURNOS ===");
            for (String entrada : historial) {
                System.out.println(entrada);
            }
        }
    }

    /**
     * Busca una especie por nombre (RF1).
     *
     * @throws EcoSimException si el nombre está vacío.
     * @return La especie encontrada, o null si no existe.
     */
    public Especie buscarEspecie(String nombre) throws EcoSimException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new EcoSimException("El nombre de la especie no puede estar vacío para la búsqueda.");
        }
        for (Especie e : simulacionActual.getEspecies()) {
            if (e.getNombre().equalsIgnoreCase(nombre.trim())) {
                return e;
            }
        }
        return null;
    }

    private Poblacion buscarPoblacion(String nombreEspecie) {
        for (Poblacion p : simulacionActual.getPoblaciones()) {
            if (p.getEspecie().getNombre().equalsIgnoreCase(nombreEspecie)) {
                return p;
            }
        }
        return null;
    }

    private void mostrarEstadoEcosistema() {
        System.out.println("Estado actual del ecosistema:");
        for (Poblacion p : simulacionActual.getPoblaciones()) {
            System.out.println("  - " + p.getEspecie().getNombre()
                    + ": " + p.getCantidad()
                    + " individuos (limite: " + p.getLimiteMaximo() + ")");
        }
        System.out.println("  Alimento restante: " + simulacionActual.getEntorno().getAlimentoDisponible());
    }

    public Simulacion getSimulacionActual() { return simulacionActual; }
}
