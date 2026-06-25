package simulacionEcosistema.modelo;

import java.util.ArrayList;
import java.util.List;

public class Simulacion {
    private int tiempoTotal;
    private int turnoActual;
    private boolean activa;
    private List<Especie>     especies;
    private List<Poblacion>   poblaciones;
    private Entorno           entorno;
    private List<Interaccion> interacciones;
    private List<String>      historialTurnos;

    /**
     * Crea una simulación con validación de parámetros.
     *
     * @throws EcoSimException si el tiempo total es menor a 1 o el entorno es nulo.
     */
    public Simulacion(int tiempoTotal, Entorno entorno) throws EcoSimException {
        if (tiempoTotal < 1) {
            throw new EcoSimException(
                "El tiempo total de la simulación debe ser al menos 1 turno. Valor recibido: " + tiempoTotal);
        }
        if (entorno == null) {
            throw new EcoSimException("El entorno de la simulación no puede ser nulo.");
        }

        this.tiempoTotal     = tiempoTotal;
        this.turnoActual     = 0;
        this.activa          = false;
        this.especies        = new ArrayList<>();
        this.poblaciones     = new ArrayList<>();
        this.interacciones   = new ArrayList<>();
        this.entorno         = entorno;
        this.historialTurnos = new ArrayList<>();
    }

    /** Reinicia la simulación desde el turno 0. */
    public void reiniciarSimulacion() {
        this.turnoActual = 0;
        this.activa      = true;
        this.historialTurnos.clear();
    }

    /**
     * Agrega una especie a la simulación.
     *
     * @throws EcoSimException si la especie es nula o ya existe con el mismo nombre.
     */
    public void agregarEspecie(Especie especie) throws EcoSimException {
        if (especie == null) {
            throw new EcoSimException("La especie a agregar no puede ser nula.");
        }
        for (Especie e : especies) {
            if (e.getNombre().equalsIgnoreCase(especie.getNombre())) {
                throw new EcoSimException(
                    "Ya existe una especie con el nombre '" + especie.getNombre() + "' en la simulación.");
            }
        }
        this.especies.add(especie);
    }

    /**
     * Agrega una población a la simulación.
     *
     * @throws EcoSimException si la población es nula.
     */
    public void agregarPoblacion(Poblacion poblacion) throws EcoSimException {
        if (poblacion == null) {
            throw new EcoSimException("La población a agregar no puede ser nula.");
        }
        this.poblaciones.add(poblacion);
    }

    /**
     * Agrega una interacción a la simulación.
     *
     * @throws EcoSimException si la interacción es nula.
     */
    public void agregarInteraccion(Interaccion interaccion) throws EcoSimException {
        if (interaccion == null) {
            throw new EcoSimException("La interacción a agregar no puede ser nula.");
        }
        this.interacciones.add(interaccion);
    }

    /**
     * Avanza el turno en 1.
     *
     * @throws EcoSimException si la simulación no está activa o ya finalizó.
     */
    public void avanzarTurno() throws EcoSimException {
        if (!activa) {
            throw new EcoSimException("No se puede avanzar: la simulación no está activa.");
        }
        if (turnoActual >= tiempoTotal) {
            throw new EcoSimException(
                "No se puede avanzar: ya se completaron todos los " + tiempoTotal + " turnos.");
        }
        this.turnoActual++;
    }

    /** Agrega un mensaje al historial de turnos. */
    public void agregarAlHistorial(String mensaje) {
        if (mensaje != null && !mensaje.trim().isEmpty()) {
            historialTurnos.add(mensaje);
        }
    }

    public void setTurnoActual(int turnoActual) { this.turnoActual = turnoActual; }
    public void setActiva(boolean activa)       { this.activa = activa; }

    public int getTurnoActual()              { return turnoActual; }
    public int getTiempoTotal()              { return tiempoTotal; }
    public boolean isActiva()                { return activa; }
    public Entorno getEntorno()              { return entorno; }
    public List<Especie> getEspecies()       { return especies; }
    public List<Poblacion> getPoblaciones()  { return poblaciones; }
    public List<Interaccion> getInteracciones() { return interacciones; }
    public List<String> getHistorial()       { return historialTurnos; }
}
