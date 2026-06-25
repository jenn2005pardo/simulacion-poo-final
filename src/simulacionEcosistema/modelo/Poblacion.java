package simulacionEcosistema.modelo;

public class Poblacion {
    private int cantidad;
    private int limiteEspacio;
    private Especie especie;

    /**
     * Crea una población asociada a una especie.
     *
     * @throws EcoSimException si la especie es nula, la cantidad es menor a 1
     *                         o el límite de espacio es menor a 1.
     */
    public Poblacion(int cantidad, int limiteEspacio, Especie especie) throws EcoSimException {
        if (especie == null) {
            throw new EcoSimException("La especie asociada a la población no puede ser nula.");
        }
        if (cantidad < 1) {
            throw new EcoSimException(
                "La cantidad inicial de la población debe ser al menos 1. Valor recibido: " + cantidad);
        }
        if (limiteEspacio < 1) {
            throw new EcoSimException(
                "El límite de espacio (K) debe ser al menos 1. Valor recibido: " + limiteEspacio);
        }

        this.cantidad      = cantidad;
        this.limiteEspacio = limiteEspacio;
        this.especie       = especie;
    }

    /**
     * Actualiza la población aplicando tasas de natalidad y mortalidad.
     * Si hay sobrepoblación, los nacimientos se reducen a la mitad.
     */
    public void actualizarPoblacion() {
        double r = especie.getTasaNatalidad();
        double m = especie.getTasaMortalidad();

        int nacimientos = (int) (cantidad * r);
        int muertes     = (int) (cantidad * m);

        if (cantidad > limiteEspacio) {
            System.out.println("¡Sobrepoblación detectada en " + especie.getNombre() + "!");
            nacimientos = nacimientos / 2;
        }

        cantidad = Math.max(0, cantidad + nacimientos - muertes);
        System.out.println("La población de " + especie.getNombre()
                + " fue actualizada a " + cantidad + " individuos.");
    }

    /** Establece una cantidad, validando que sea >= 0. */
    public void setCantidad(int cantidad) throws EcoSimException {
        if (cantidad < 0) {
            throw new EcoSimException(
                "La cantidad de la población no puede ser negativa. Valor recibido: " + cantidad);
        }
        this.cantidad = cantidad;
    }

    public int getCantidad()      { return cantidad; }
    public int getLimiteMaximo()  { return limiteEspacio; }
    public Especie getEspecie()   { return especie; }
}
