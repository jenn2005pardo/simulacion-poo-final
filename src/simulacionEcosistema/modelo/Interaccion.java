package simulacionEcosistema.modelo;

public class Interaccion {
    private String nombreDepredador;
    private String nombrePresa;
    private double factorCaza;           // probabilidad de caza exitosa por depredador
    private double eficienciaConversion; // energía convertida en nuevos depredadores

    /**
     * Crea una interacción de tipo depredador-presa.
     *
     * @throws EcoSimException si los nombres están vacíos o los factores
     *                         están fuera del rango [0.0, 1.0].
     */
    public Interaccion(String nombreDepredador, String nombrePresa,
                       double factorCaza, double eficienciaConversion) throws EcoSimException {

        if (nombreDepredador == null || nombreDepredador.trim().isEmpty()) {
            throw new EcoSimException("El nombre del depredador no puede estar vacío.");
        }
        if (nombrePresa == null || nombrePresa.trim().isEmpty()) {
            throw new EcoSimException("El nombre de la presa no puede estar vacío.");
        }
        if (nombreDepredador.equalsIgnoreCase(nombrePresa)) {
            throw new EcoSimException(
                "El depredador y la presa no pueden ser la misma especie: '" + nombreDepredador + "'.");
        }
        if (factorCaza < 0.0 || factorCaza > 1.0) {
            throw new EcoSimException(
                "El factor de caza debe estar entre 0.0 y 1.0. Valor recibido: " + factorCaza);
        }
        if (eficienciaConversion < 0.0 || eficienciaConversion > 1.0) {
            throw new EcoSimException(
                "La eficiencia de conversión debe estar entre 0.0 y 1.0. Valor recibido: " + eficienciaConversion);
        }

        this.nombreDepredador    = nombreDepredador.trim();
        this.nombrePresa         = nombrePresa.trim();
        this.factorCaza          = factorCaza;
        this.eficienciaConversion = eficienciaConversion;
    }

    /**
     * Simula un evento de caza entre el depredador y la presa.
     *
     * @throws EcoSimException si alguna de las poblaciones es nula.
     */
    public void simularCaza(Poblacion depredadores, Poblacion presas) throws EcoSimException {
        if (depredadores == null) {
            throw new EcoSimException(
                "La población del depredador '" + nombreDepredador + "' no existe en la simulación.");
        }
        if (presas == null) {
            throw new EcoSimException(
                "La población de la presa '" + nombrePresa + "' no existe en la simulación.");
        }

        int presasCazadas = (int) (depredadores.getCantidad() * factorCaza);
        if (presasCazadas > presas.getCantidad()) {
            presasCazadas = presas.getCantidad();
        }

        presas.setCantidad(presas.getCantidad() - presasCazadas);
        System.out.println("La interaccion de caza fue ejecutada correctamente ("
                + nombreDepredador + " cazo " + presasCazadas + " " + nombrePresa + ").");

        transferirEnergia(depredadores, presasCazadas);
    }

    /**
     * Transfiere energía de las presas cazadas hacia el depredador.
     *
     * @throws EcoSimException si la población del depredador es nula
     *                         o presasCazadas es negativo.
     */
    public void transferirEnergia(Poblacion depredadores, int presasCazadas) throws EcoSimException {
        if (depredadores == null) {
            throw new EcoSimException("La población del depredador no puede ser nula al transferir energía.");
        }
        if (presasCazadas < 0) {
            throw new EcoSimException(
                "El número de presas cazadas no puede ser negativo. Valor recibido: " + presasCazadas);
        }

        int nuevosDepredadores = (int) (presasCazadas * eficienciaConversion);
        depredadores.setCantidad(depredadores.getCantidad() + nuevosDepredadores);
        System.out.println("Transferencia de energia realizada correctamente (+"
                + nuevosDepredadores + " " + nombreDepredador + ").");
    }

    public String getNombreDepredador()      { return nombreDepredador; }
    public String getNombrePresa()           { return nombrePresa; }
    public double getFactorAfectacion()      { return factorCaza; }
    public double getTasaExito()             { return factorCaza; }
    public double getEficienciaConversion()  { return eficienciaConversion; }
}
