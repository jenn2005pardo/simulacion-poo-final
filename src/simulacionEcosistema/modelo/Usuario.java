package simulacionEcosistema.modelo;

public abstract class Usuario {
    protected String nombreCompleto;
    protected String cedula;
    protected String correo;
    protected String nombreUsuario;
    protected String contrasena;
    protected String estado;

    /**
     * Construye un usuario con validación completa de campos.
     *
     * @throws EcoSimException si algún campo obligatorio está vacío o nulo,
     *                         o si la contraseña tiene menos de 4 caracteres.
     */
    public Usuario(String nombreCompleto, String cedula, String correo,
                   String nombreUsuario, String contrasena, String estado) throws EcoSimException {

        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
            throw new EcoSimException("El nombre completo no puede estar vacío.");
        }
        if (cedula == null || cedula.trim().isEmpty()) {
            throw new EcoSimException("La cédula no puede estar vacía.");
        }
        if (correo == null || correo.trim().isEmpty() || !correo.contains("@")) {
            throw new EcoSimException(
                "El correo electrónico es inválido: '" + correo + "'. Debe contener '@'.");
        }
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            throw new EcoSimException("El nombre de usuario no puede estar vacío.");
        }
        if (nombreUsuario.contains(" ")) {
            throw new EcoSimException(
                "El nombre de usuario no puede contener espacios: '" + nombreUsuario + "'.");
        }
        if (contrasena == null || contrasena.length() < 4) {
            throw new EcoSimException(
                "La contraseña debe tener al menos 4 caracteres.");
        }
        if (estado == null || estado.trim().isEmpty()) {
            throw new EcoSimException("El estado del usuario no puede estar vacío.");
        }

        this.nombreCompleto = nombreCompleto.trim();
        this.cedula         = cedula.trim();
        this.correo         = correo.trim();
        this.nombreUsuario  = nombreUsuario.trim();
        this.contrasena     = contrasena;
        this.estado         = estado.trim();
    }

    public boolean iniciarSesion(String usr, String pass) {
        return this.nombreUsuario.equals(usr) && this.contrasena.equals(pass);
    }

    public boolean estaActivo() {
        return "Activo".equalsIgnoreCase(this.estado);
    }

    public String getNombreCompleto() { return nombreCompleto; }
    public String getCedula()         { return cedula; }
    public String getCorreo()         { return correo; }
    public String getNombreUsuario()  { return nombreUsuario; }
    public String getContrasena()     { return contrasena; }
    public String getEstado()         { return estado; }
}
