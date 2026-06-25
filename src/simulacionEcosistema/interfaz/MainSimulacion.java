package simulacionEcosistema.interfaz;

import simulacionEcosistema.modelo.*;
import simulacionEcosistema.negocio.*;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MainSimulacion {

    private static Scanner scanner = new Scanner(System.in);

    // main declara throws: las excepciones de negocio/modelo suben naturalmente
    public static void main(String[] args) throws EcoSimException {

        GestorUsuario gestorUsuario = new GestorUsuario();

        // ---- Usuarios de prueba ----
        Estudiante    est1  = new Estudiante("Juan Perez",   "1234567890", "juan@test.com",  "juanp",  "1234",     "Activo", "A");
        Estudiante    est2  = new Estudiante("Ana Torres",   "0987654321", "ana@test.com",   "ana",    "4567",     "Activo", "B");
        Administrador admin = new Administrador("Profe Maria","1111111111","maria@test.com", "admin",  "admin123", "Activo");

        gestorUsuario.registrarUsuario(est1);
        gestorUsuario.registrarUsuario(est2);
        gestorUsuario.registrarUsuario(admin);

        // ---- Login ----
        imprimirBanner();
        System.out.print("Usuario   : ");
        String usr  = scanner.nextLine().trim();
        System.out.print("Contrasena: ");
        String pass = scanner.nextLine().trim();

        Usuario usuarioActual = gestorUsuario.buscarUsuarioParaLogin(usr, pass);

        if (usuarioActual == null) {
            System.out.println("Credenciales incorrectas o usuario inactivo. Saliendo...");
            return;
        }

        System.out.println("\nBienvenido/a, " + usuarioActual.getNombreCompleto()
                + " [" + (usuarioActual instanceof Administrador ? "ADMINISTRADOR" : "ESTUDIANTE") + "]");

        // ---- Selección de ecosistema ----
        Simulacion simulacion = elegirEcosistema();

        GestorSimulacion gestorSimulacion;
        if (usuarioActual instanceof Estudiante) {
            gestorSimulacion = new GestorSimulacion(simulacion, (Estudiante) usuarioActual);
        } else {
            gestorSimulacion = new GestorSimulacion(simulacion, null);
        }

        // ---- Menú principal ----
        boolean salir = false;
        while (!salir) {
            if (usuarioActual instanceof Administrador) {
                salir = menuAdministrador(gestorSimulacion, gestorUsuario, (Administrador) usuarioActual);
            } else {
                salir = menuEstudiante(gestorSimulacion);
            }
        }

        scanner.close();
    }

    // ============================================================
    //  MENÚ ESTUDIANTE
    // ============================================================
    private static boolean menuEstudiante(GestorSimulacion gs) throws EcoSimException {
        System.out.println("\n========== MENU PRINCIPAL ==========");
        System.out.println("  1. Iniciar Simulacion");
        System.out.println("  2. Ejecutar Turno");
        System.out.println("  3. Ver Estado del Entorno");
        System.out.println("  4. Agregar Especie");
        System.out.println("  5. Agregar Poblacion");
        System.out.println("  6. Agregar Interaccion");
        System.out.println("  7. Buscar Especie");
        System.out.println("  8. Ver Historial de Turnos");
        System.out.println("  9. Detener Simulacion");
        System.out.println(" 10. Salir");
        System.out.print("Seleccione una opcion: ");

        int opcion = leerEntero();
        switch (opcion) {
            case 1:  gs.iniciar();           break;
            case 2:  gs.ejecutarTurno();     break;
            case 3:  mostrarEntorno(gs);     break;
            case 4:  agregarEspecie(gs);     break;
            case 5:  agregarPoblacion(gs);   break;
            case 6:  agregarInteraccion(gs); break;
            case 7:  buscarEspecie(gs);      break;
            case 8:  gs.mostrarHistorial();  break;
            case 9:  gs.terminarManualmente(); break;
            case 10: System.out.println("Saliendo del sistema..."); return true;
            default: System.out.println("Opcion no valida. Ingrese un numero del 1 al 10.");
        }
        return false;
    }

    // ============================================================
    //  MENÚ ADMINISTRADOR
    // ============================================================
    private static boolean menuAdministrador(GestorSimulacion gs,
                                              GestorUsuario gu,
                                              Administrador admin) throws EcoSimException {
        System.out.println("\n======== MENU ADMINISTRADOR ========");
        System.out.println("  1. Iniciar Simulacion");
        System.out.println("  2. Ejecutar Turno");
        System.out.println("  3. Ver Estado del Entorno");
        System.out.println("  4. Agregar Especie");
        System.out.println("  5. Agregar Poblacion");
        System.out.println("  6. Agregar Interaccion");
        System.out.println("  7. Buscar Especie");
        System.out.println("  8. Ver Historial de Turnos");
        System.out.println("  9. Detener Simulacion");
        System.out.println(" 10. Ver estudiantes por paralelo");
        System.out.println(" 11. Buscar usuario por cedula");
        System.out.println(" 12. Salir");
        System.out.print("Seleccione una opcion: ");

        int opcion = leerEntero();
        switch (opcion) {
            case 1:  gs.iniciar();                               break;
            case 2:  gs.ejecutarTurno();                         break;
            case 3:  mostrarEntorno(gs);                         break;
            case 4:  agregarEspecie(gs);                         break;
            case 5:  agregarPoblacion(gs);                       break;
            case 6:  agregarInteraccion(gs);                     break;
            case 7:  buscarEspecie(gs);                          break;
            case 8:  gs.mostrarHistorial();                      break;
            case 9:  gs.terminarManualmente();                   break;
            case 10: verEstudiantesPorParalelo(gu, admin);       break;
            case 11: buscarPorCedula(gu, admin);                 break;
            case 12: System.out.println("Saliendo del sistema..."); return true;
            default: System.out.println("Opcion no valida. Ingrese un numero del 1 al 12.");
        }
        return false;
    }

    // ============================================================
    //  ACCIONES COMPARTIDAS
    // ============================================================

    /** RF1 — Agregar Especie */
    private static void agregarEspecie(GestorSimulacion gs) throws EcoSimException {
        System.out.println("\n--- Agregar Especie (RF1) ---");
        System.out.print("Nombre de la especie              : ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Tipo (Herbivoro/Carnivoro/Planta) : ");
        String tipo   = scanner.nextLine().trim();
        System.out.print("Tasa de reproduccion (0.0-1.0)    : ");
        double tasaR  = leerDouble();
        System.out.print("Tasa de mortalidad   (0.0-1.0)    : ");
        double tasaM  = leerDouble();

        // La validación ocurre dentro del constructor de Especie (lanza EcoSimException)
        Especie especie = new Especie(nombre, tipo, tasaR, tasaM);
        gs.getSimulacionActual().agregarEspecie(especie);
        System.out.println("Especie '" + nombre + "' agregada a la simulacion.");
    }

    /** RF4 — Agregar Población */
    private static void agregarPoblacion(GestorSimulacion gs) throws EcoSimException {
        System.out.println("\n--- Agregar Poblacion (RF4) ---");
        List<Especie> especies = gs.getSimulacionActual().getEspecies();
        if (especies.isEmpty()) {
            System.out.println("Primero debes agregar al menos una especie.");
            return;
        }
        System.out.println("Especies disponibles:");
        for (int i = 0; i < especies.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + especies.get(i));
        }
        System.out.print("Seleccione el numero de especie   : ");
        int idx = leerEntero() - 1;
        if (idx < 0 || idx >= especies.size()) {
            System.out.println("Seleccion invalida.");
            return;
        }
        System.out.print("Cantidad inicial de individuos    : ");
        int cantidad = leerEntero();
        System.out.print("Limite de espacio (capacidad K)   : ");
        int limite   = leerEntero();

        // Validación ocurre en el constructor de Poblacion
        Poblacion p = new Poblacion(cantidad, limite, especies.get(idx));
        gs.registrarPoblacion(p);
        System.out.println("Poblacion de '" + especies.get(idx).getNombre()
                + "' agregada: " + cantidad + " individuos (K=" + limite + ").");
    }

    /** RF5 — Agregar Interacción */
    private static void agregarInteraccion(GestorSimulacion gs) throws EcoSimException {
        System.out.println("\n--- Agregar Interaccion depredador-presa (RF5) ---");
        System.out.print("Nombre del depredador             : ");
        String dep   = scanner.nextLine().trim();
        System.out.print("Nombre de la presa                : ");
        String presa = scanner.nextLine().trim();
        System.out.print("Factor de caza        (0.0-1.0)   : ");
        double fc    = leerDouble();
        System.out.print("Eficiencia conversion (0.0-1.0)   : ");
        double ef    = leerDouble();

        // Validación ocurre en el constructor de Interaccion
        Interaccion inter = new Interaccion(dep, presa, fc, ef);
        gs.registrarInteraccion(inter);
        System.out.println("Interaccion '" + dep + " -> " + presa + "' registrada.");
    }

    /** RF1 — Buscar Especie por nombre */
    private static void buscarEspecie(GestorSimulacion gs) throws EcoSimException {
        System.out.print("\nIngrese el nombre de la especie a buscar: ");
        String nombre = scanner.nextLine().trim();

        // Validacion en gs.buscarEspecie (lanza si nombre es vacío)
        Especie e = gs.buscarEspecie(nombre);
        if (e == null) {
            System.out.println("Especie '" + nombre + "' no encontrada.");
        } else {
            System.out.println("\n--- Informacion de Especie ---");
            System.out.println("  Nombre             : " + e.getNombre());
            System.out.println("  Tipo               : " + e.getTipo());
            System.out.println("  Tasa Reproduccion  : " + e.getTasaNatalidad());
            System.out.println("  Tasa Mortalidad    : " + e.getTasaMortalidad());
            double bv = e.getBalanceVital();
            System.out.println("  Balance Vital (r-m): " + String.format("%.3f", bv)
                    + (bv < 0 ? "  ⚠ RIESGO DE EXTINCION" : "  ✓ Viable"));
        }
    }

    /** RF7/RF8 — Ver estado del entorno */
    private static void mostrarEntorno(GestorSimulacion gs) {
        Entorno e = gs.getSimulacionActual().getEntorno();
        System.out.println("\n--- ESTADO DEL ENTORNO ---");
        System.out.println("  Alimento Disponible  : " + e.getAlimentoDisponible());
        System.out.println("  Tasa de Consumo      : " + e.getTasaConsumo());
        System.out.println("  Regeneracion Vegetal : " + e.getRegeneracionVegetal());
        System.out.println("  Alertas activas      :");
        if (e.getAlertas().isEmpty()) {
            System.out.println("    Ninguna.");
        } else {
            for (String a : e.getAlertas()) {
                System.out.println("    ⚠ " + a);
            }
        }
    }

    // ============================================================
    //  ACCIONES EXCLUSIVAS DE ADMINISTRADOR
    // ============================================================

    private static void verEstudiantesPorParalelo(GestorUsuario gu, Administrador admin)
            throws EcoSimException {
        System.out.print("\nIngrese el paralelo (ej. A, B): ");
        String paralelo = scanner.nextLine().trim();

        // Validacion en gu.obtenerAlumnosPorParalelo
        List<Estudiante> lista = gu.obtenerAlumnosPorParalelo(paralelo);
        if (lista.isEmpty()) {
            System.out.println("No hay estudiantes en el paralelo '" + paralelo + "'.");
        } else {
            System.out.println("\n--- Estudiantes del Paralelo " + paralelo + " ---");
            for (Estudiante est : lista) {
                admin.supervisarEstudiante(est);  // lanza si est es nulo
                System.out.println("---");
            }
        }
    }

    private static void buscarPorCedula(GestorUsuario gu, Administrador admin)
            throws EcoSimException {
        System.out.print("\nIngrese la cedula del usuario: ");
        String cedula = scanner.nextLine().trim();

        // Validacion en gu.buscarPorCedula
        Usuario u = gu.buscarPorCedula(cedula);
        if (u == null) {
            System.out.println("No se encontro usuario con cedula '" + cedula + "'.");
        } else if (u instanceof Estudiante) {
            admin.supervisarEstudiante((Estudiante) u);
        } else {
            System.out.println("Usuario encontrado: " + u.getNombreCompleto()
                    + " | Rol: Administrador | Estado: " + u.getEstado());
        }
    }

    // ============================================================
    //  SELECCIÓN DE ECOSISTEMA
    // ============================================================

    private static Simulacion elegirEcosistema() throws EcoSimException {
        System.out.println("\n¿Que ecosistema deseas simular?");
        System.out.println("  1. Terrestre  (Leon, Lobo, Ciervo, Conejo, Hierba)");
        System.out.println("  2. Marino     (Tiburon, Orca, Pez, Calamar, Alga, Coral)");
        System.out.print("Seleccione: ");
        int op = leerEntero();

        if (op == 2) {
            System.out.println("Ecosistema MARINO cargado.");
            return GeneradorEcosistema.generarEcosistemaMarino();
        }
        System.out.println("Ecosistema TERRESTRE cargado.");
        return GeneradorEcosistema.generarEcosistemaTerresre();
    }

    // ============================================================
    //  UTILIDADES
    // ============================================================

    private static void imprimirBanner() {
        System.out.println("============================================");
        System.out.println("   SISTEMA DE SIMULACION DE ECOSISTEMAS    ");
        System.out.println("============================================");
        System.out.println("  RF1-RF8 | POO | Excepciones              ");
        System.out.println("  Ecosistemas: Terrestre & Marino           ");
        System.out.println("============================================\n");
    }

    /** Lee un entero desde consola; devuelve -1 si la entrada no es un número. */
    private static int leerEntero() {
        try {
            int val = scanner.nextInt();
            scanner.nextLine();
            return val;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            System.out.println("Entrada invalida. Se esperaba un numero entero.");
            return -1;
        }
    }

    /** Lee un double desde consola; devuelve -1 si la entrada no es un número. */
    private static double leerDouble() {
        try {
            double val = scanner.nextDouble();
            scanner.nextLine();
            return val;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            System.out.println("Entrada invalida. Se esperaba un numero decimal.");
            return -1;
        }
    }
}
