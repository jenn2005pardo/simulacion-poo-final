package simulacionEcosistema.modelo;

public class Especie {
    private String nombre;
    private String tipo;
    private double tasaReproduccion;
    private double tasaMortalidad;

    // Tipos de especie válidos
    private static final String[] TIPOS_VALIDOS = {"Herbivoro", "Carnivoro", "Planta"};

    /**
     * Crea una especie con validación de datos.
     *
     * @throws EcoSimException si el nombre está vacío, el tipo es inválido
     *                         o las tasas están fuera del rango [0.0, 1.0].
     */
    public Especie(String nombre, String tipo, double tasaReproduccion, double tasaMortalidad)
            throws EcoSimException {

        // Validar nombre
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new EcoSimException("El nombre de la especie no puede estar vacío.");
        }

        // Validar tipo
        boolean tipoValido = false;
        for (String t : TIPOS_VALIDOS) {
            if (t.equalsIgnoreCase(tipo)) {
                tipoValido = true;
                break;
            }
        }
        if (!tipoValido) {
            throw new EcoSimException(
                "Tipo de especie inválido: '" + tipo + "'. Tipos válidos: Herbivoro, Carnivoro, Planta.");
        }

        // Validar tasas
        if (tasaReproduccion < 0.0 || tasaReproduccion > 1.0) {
            throw new EcoSimException(
                "La tasa de reproducción debe estar entre 0.0 y 1.0. Valor recibido: " + tasaReproduccion);
        }
        if (tasaMortalidad < 0.0 || tasaMortalidad > 1.0) {
            throw new EcoSimException(
                "La tasa de mortalidad debe estar entre 0.0 y 1.0. Valor recibido: " + tasaMortalidad);
        }

        this.nombre           = nombre.trim();
        this.tipo             = tipo;
        this.tasaReproduccion = tasaReproduccion;
        this.tasaMortalidad   = tasaMortalidad;

        System.out.println("Especie registrada correctamente: " + this.nombre);

        // RF2 — Advertencia de viabilidad ecológica (no lanza excepción, solo avisa)
        double balanceVital = tasaReproduccion - tasaMortalidad;
        if (balanceVital < 0) {
            System.out.println("Advertencia (RF2): La tasa de mortalidad supera a la de reproducción "
                    + "en la especie '" + nombre + "' (Balance Vital = "
                    + String.format("%.3f", balanceVital) + "). Riesgo de extinción.");
        }
    }

    public String getNombre()            { return nombre; }
    public String getTipo()              { return tipo; }
    public double getTasaNatalidad()     { return tasaReproduccion; }
    public double getTasaMortalidad()    { return tasaMortalidad; }
    public double getBalanceVital()      { return tasaReproduccion - tasaMortalidad; }

    @Override
    public String toString() {
        return nombre + " (" + tipo + ")";
    }
}
