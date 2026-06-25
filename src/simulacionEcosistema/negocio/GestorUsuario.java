package simulacionEcosistema.negocio;

import simulacionEcosistema.modelo.EcoSimException;
import simulacionEcosistema.modelo.Estudiante;
import simulacionEcosistema.modelo.Usuario;

import java.util.ArrayList;
import java.util.List;

public class GestorUsuario {
    private List<Usuario> listaUsuarios;

    public GestorUsuario() {
        this.listaUsuarios = new ArrayList<>();
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @throws EcoSimException si el usuario es nulo, su nombre de usuario ya existe
     *                         o su cédula ya está registrada.
     */
    public void registrarUsuario(Usuario usuario) throws EcoSimException {
        if (usuario == null) {
            throw new EcoSimException("El usuario a registrar no puede ser nulo.");
        }
        if (existeNombreUsuario(usuario.getNombreUsuario())) {
            throw new EcoSimException(
                "El nombre de usuario '" + usuario.getNombreUsuario() + "' ya está registrado en el sistema.");
        }
        if (existeCedula(usuario.getCedula())) {
            throw new EcoSimException(
                "La cédula '" + usuario.getCedula() + "' ya está registrada en el sistema.");
        }
        listaUsuarios.add(usuario);
        System.out.println("Usuario registrado correctamente: " + usuario.getNombreUsuario());
    }

    /**
     * Busca un usuario por credenciales para iniciar sesión.
     *
     * @throws EcoSimException si el usuario o la contraseña están vacíos.
     * @return El usuario encontrado, o null si las credenciales no son válidas.
     */
    public Usuario buscarUsuarioParaLogin(String usr, String pass) throws EcoSimException {
        if (usr == null || usr.trim().isEmpty()) {
            throw new EcoSimException("El nombre de usuario no puede estar vacío al iniciar sesión.");
        }
        if (pass == null || pass.isEmpty()) {
            throw new EcoSimException("La contraseña no puede estar vacía al iniciar sesión.");
        }
        for (Usuario u : listaUsuarios) {
            if (u.iniciarSesion(usr, pass) && u.estaActivo()) {
                return u;
            }
        }
        return null; // credenciales incorrectas o usuario inactivo
    }

    /**
     * Obtiene la lista de estudiantes por paralelo.
     *
     * @throws EcoSimException si el paralelo está vacío.
     */
    public List<Estudiante> obtenerAlumnosPorParalelo(String paralelo) throws EcoSimException {
        if (paralelo == null || paralelo.trim().isEmpty()) {
            throw new EcoSimException("El paralelo no puede estar vacío para la búsqueda.");
        }
        List<Estudiante> alumnos = new ArrayList<>();
        for (Usuario u : listaUsuarios) {
            if (u instanceof Estudiante) {
                Estudiante est = (Estudiante) u;
                if (est.getParalelo().equalsIgnoreCase(paralelo.trim())) {
                    alumnos.add(est);
                }
            }
        }
        return alumnos;
    }

    /**
     * Busca un usuario por cédula.
     *
     * @throws EcoSimException si la cédula está vacía.
     * @return El usuario encontrado, o null si no existe.
     */
    public Usuario buscarPorCedula(String cedula) throws EcoSimException {
        if (cedula == null || cedula.trim().isEmpty()) {
            throw new EcoSimException("La cédula no puede estar vacía para la búsqueda.");
        }
        for (Usuario u : listaUsuarios) {
            if (u.getCedula().equals(cedula.trim())) {
                return u;
            }
        }
        return null;
    }

    public boolean existeNombreUsuario(String nombreUsuario) {
        for (Usuario u : listaUsuarios) {
            if (u.getNombreUsuario().equals(nombreUsuario)) {
                return true;
            }
        }
        return false;
    }

    public boolean existeCedula(String cedula) {
        for (Usuario u : listaUsuarios) {
            if (u.getCedula().equals(cedula)) {
                return true;
            }
        }
        return false;
    }

    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }
}
