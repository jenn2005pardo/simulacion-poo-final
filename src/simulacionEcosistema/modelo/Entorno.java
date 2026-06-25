package simulacionEcosistema.modelo;

import java.util.ArrayList;
import java.util.List;

public class Entorno {
    private int alimentoDisponible;
    private int tasaConsumo;
    private int regeneracionVegetal;
    private List<String> alertas;

    /**
     * Crea el entorno de la simulación con validación de parámetros.
     *
     * @throws EcoSimException si algún valor es negativo o la tasa de consumo es 0.
     */
    public Entorno(int alimentoDisponible, int tasaConsumo, int regeneracionVegetal)
            throws EcoSimException {

        if (alimentoDisponible < 0) {
            throw new EcoSimException(
                "El alimento disponible no puede ser negativo. Valor recibido: " + alimentoDisponible);
        }
        if (tasaConsumo <= 0) {
            throw new EcoSimException(
                "La tasa de consumo debe ser mayor a 0. Valor recibido: " + tasaConsumo);
        }
        if (regeneracionVegetal < 0) {
            throw new EcoSimException(
                "La regeneración vegetal no puede ser negativa. Valor recibido: " + regeneracionVegetal);
        }

        this.alimentoDisponible  = alimentoDisponible;
        this.tasaConsumo         = tasaConsumo;
        this.regeneracionVegetal = regeneracionVegetal;
        this.alertas             = new ArrayList<>();
    }

    // RF7 — Desgaste de Recursos
    /**
     * Calcula el consumo de recursos según las poblaciones activas.
     *
     * @throws EcoSimException si la lista de poblaciones es nula.
     */
    public void calcularConsumo(List<Poblacion> poblaciones) throws EcoSimException {
        if (poblaciones == null) {
            throw new EcoSimException("La lista de poblaciones no puede ser nula al calcular el consumo.");
        }

        int poblacionTotal = 0;
        for (Poblacion p : poblaciones) {
            poblacionTotal += p.getCantidad();
        }

        int consumoTotal     = poblacionTotal * tasaConsumo;
        int alimentoRestante = alimentoDisponible + regeneracionVegetal - consumoTotal;

        this.alimentoDisponible = Math.max(0, alimentoRestante);
        System.out.println("Recursos del entorno actualizados. Alimento restante: " + alimentoDisponible);
        generarAlerta();
    }

    // RF8 — Alertas de Desequilibrio
    public void generarAlerta() {
        if (alimentoDisponible == 0) {
            aplicarCrisis();
        } else {
            alertas.remove("¡Colapso por escasez de recursos!");
        }
    }

    public void generarAlertaSobrepoblacion(String nombreEspecie) {
        String mensaje = "¡Sobrepoblacion detectada en " + nombreEspecie + "!";
        if (!alertas.contains(mensaje)) {
            alertas.add(mensaje);
            System.out.println("ALERTA: " + mensaje);
        }
    }

    public void limpiarAlertaSobrepoblacion(String nombreEspecie) {
        alertas.remove("¡Sobrepoblacion detectada en " + nombreEspecie + "!");
    }

    public void aplicarCrisis() {
        String mensaje = "¡Colapso por escasez de recursos!";
        if (!alertas.contains(mensaje)) {
            alertas.add(mensaje);
        }
    }

    /**
     * Agrega recursos al entorno.
     *
     * @throws EcoSimException si la cantidad a restablecer es negativa.
     */
    public void restablecerRecursos(int cantidad) throws EcoSimException {
        if (cantidad < 0) {
            throw new EcoSimException(
                "La cantidad a restablecer no puede ser negativa. Valor recibido: " + cantidad);
        }
        this.alimentoDisponible += cantidad;
    }

    /**
     * Establece manualmente el alimento disponible.
     *
     * @throws EcoSimException si el valor es negativo.
     */
    public void setAlimentoDisponible(int alimentoDisponible) throws EcoSimException {
        if (alimentoDisponible < 0) {
            throw new EcoSimException(
                "El alimento disponible no puede ser negativo. Valor recibido: " + alimentoDisponible);
        }
        this.alimentoDisponible = alimentoDisponible;
    }

    /**
     * Establece la regeneración vegetal por turno.
     *
     * @throws EcoSimException si el valor es negativo.
     */
    public void setRegeneracionVegetal(int regeneracionVegetal) throws EcoSimException {
        if (regeneracionVegetal < 0) {
            throw new EcoSimException(
                "La regeneración vegetal no puede ser negativa. Valor recibido: " + regeneracionVegetal);
        }
        this.regeneracionVegetal = regeneracionVegetal;
    }

    public int getAlimentoDisponible()   { return alimentoDisponible; }
    public int getTasaConsumo()          { return tasaConsumo; }
    public int getRegeneracionVegetal()  { return regeneracionVegetal; }
    public List<String> getAlertas()     { return alertas; }
}
