package simulacionEcosistema.negocio;

import simulacionEcosistema.modelo.*;

public class GeneradorEcosistema {

    /**
     * Ecosistema TERRESTRE — cadena trófica de tierra:
     *   León y Lobo (Carnívoros) → Ciervo y Conejo (Herbívoros) → Hierba (Planta)
     *
     * @throws EcoSimException si algún dato de configuración es inválido.
     */
    public static Simulacion generarEcosistemaTerresre() throws EcoSimException {
        Entorno entorno = new Entorno(1200, 2, 80);
        Simulacion sim  = new Simulacion(10, entorno);

        // Especies
        Especie leon   = new Especie("Leon",   "Carnivoro", 0.10, 0.05);
        Especie lobo   = new Especie("Lobo",   "Carnivoro", 0.12, 0.06);
        Especie ciervo = new Especie("Ciervo", "Herbivoro", 0.30, 0.10);
        Especie conejo = new Especie("Conejo", "Herbivoro", 0.45, 0.15);
        Especie hierba = new Especie("Hierba", "Planta",    0.60, 0.05);

        sim.agregarEspecie(leon);
        sim.agregarEspecie(lobo);
        sim.agregarEspecie(ciervo);
        sim.agregarEspecie(conejo);
        sim.agregarEspecie(hierba);

        // Poblaciones
        sim.agregarPoblacion(new Poblacion(8,   40,  leon));
        sim.agregarPoblacion(new Poblacion(12,  60,  lobo));
        sim.agregarPoblacion(new Poblacion(60,  250, ciervo));
        sim.agregarPoblacion(new Poblacion(100, 400, conejo));
        sim.agregarPoblacion(new Poblacion(500, 2000, hierba));

        // Interacciones (depredador → presa)
        sim.agregarInteraccion(new Interaccion("Leon",   "Ciervo", 0.20, 0.50));
        sim.agregarInteraccion(new Interaccion("Lobo",   "Conejo", 0.25, 0.45));
        sim.agregarInteraccion(new Interaccion("Ciervo", "Hierba", 0.15, 0.30));
        sim.agregarInteraccion(new Interaccion("Conejo", "Hierba", 0.20, 0.25));

        System.out.println("Ecosistema TERRESTRE cargado: "
                + sim.getEspecies().size() + " especies, "
                + sim.getInteracciones().size() + " interacciones.");
        return sim;
    }

    /**
     * Ecosistema MARINO — cadena trófica oceánica:
     *   Tiburón y Orca (Carnívoros) → Pez y Calamar (Herbívoros) → Alga y Coral (Plantas)
     *
     * @throws EcoSimException si algún dato de configuración es inválido.
     */
    public static Simulacion generarEcosistemaMarino() throws EcoSimException {
        Entorno entorno = new Entorno(1500, 3, 100);
        Simulacion sim  = new Simulacion(10, entorno);

        // Especies
        Especie tiburon = new Especie("Tiburon", "Carnivoro", 0.08, 0.04);
        Especie orca    = new Especie("Orca",    "Carnivoro", 0.06, 0.03);
        Especie pez     = new Especie("Pez",     "Herbivoro", 0.35, 0.12);
        Especie calamar = new Especie("Calamar", "Herbivoro", 0.40, 0.14);
        Especie alga    = new Especie("Alga",    "Planta",    0.55, 0.05);
        Especie coral   = new Especie("Coral",   "Planta",    0.30, 0.04);

        sim.agregarEspecie(tiburon);
        sim.agregarEspecie(orca);
        sim.agregarEspecie(pez);
        sim.agregarEspecie(calamar);
        sim.agregarEspecie(alga);
        sim.agregarEspecie(coral);

        // Poblaciones
        sim.agregarPoblacion(new Poblacion(6,   30,  tiburon));
        sim.agregarPoblacion(new Poblacion(4,   20,  orca));
        sim.agregarPoblacion(new Poblacion(120, 500, pez));
        sim.agregarPoblacion(new Poblacion(80,  300, calamar));
        sim.agregarPoblacion(new Poblacion(600, 3000, alga));
        sim.agregarPoblacion(new Poblacion(300, 1500, coral));

        // Interacciones (depredador → presa)
        sim.agregarInteraccion(new Interaccion("Tiburon", "Pez",     0.18, 0.45));
        sim.agregarInteraccion(new Interaccion("Orca",    "Calamar", 0.22, 0.40));
        sim.agregarInteraccion(new Interaccion("Pez",     "Alga",    0.20, 0.30));
        sim.agregarInteraccion(new Interaccion("Calamar", "Coral",   0.15, 0.25));

        System.out.println("Ecosistema MARINO cargado: "
                + sim.getEspecies().size() + " especies, "
                + sim.getInteracciones().size() + " interacciones.");
        return sim;
    }
}
