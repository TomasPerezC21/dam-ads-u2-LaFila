package servicio;

import java.sql.*;
import java.util.ArrayList;

import modelo.*;

public class ClubDeportivo {
    private Connection conexion;

    public ClubDeportivo() throws SQLException {
        conexion = DriverManager.getConnection("jdbc:mysql://PMYSQL190.dns-servicio.com:3306/11158164_ClubDama2",
                "roots", "alumnoDAM-1");

    }

    /**
     * Inserta un nuevo socio en la base de datos
     * <p>
     * Compueba primero que el socio no sea null , que tenga ID
     * y que no exista ya en la base de datos
     *
     * @param socio socio a insertar
     * @return true si se ha insertado correctamente, false en caso contrario
     * @throws SQLException           si hay un error al conectarse con la base de datos
     * @throws IdObligatorioException si el socio es null o su ID no es válido
     * @author Alejandro
     */
    public boolean altaSocio(Socio socio) throws SQLException, IdObligatorioException {
        // Validamos que el socio no sea null
        if (socio == null) {
            throw new IdObligatorioException("El socio no puede ser null.");
            //Validamos que el id del socio no sea null
        } else if (socio.getIdSocio() == null || socio.getIdSocio().isBlank()) {
            throw new IdObligatorioException("El ID del socio es obligatorio.");

        } else {
            //Comprobamos si el socio ya existe en la base de datos
            String sqlComprobar = "SELECT COUNT(*) FROM socios WHERE id_socio = ? OR dni = ? OR email = ?";
            PreparedStatement pstComprobar = conexion.prepareStatement(sqlComprobar);
            pstComprobar.setString(1, socio.getIdSocio());
            pstComprobar.setString(2, socio.getDni());
            pstComprobar.setString(3, socio.getEmail());

            ResultSet rs = pstComprobar.executeQuery();
            rs.next();

            if (rs.getInt(1) > 0) {
                //Como el usuario ya existe, no se puede insertar y por lo tanto devolvemos false
                return false;
            } else {


                //Creamos la consulta para poder insertar un nuevo socio en la base de datos
                String sql = "INSERT INTO socios(id_socio,dni,nombre,apellidos,telefono,email) VALUES (?,?,?,?,?,?)";

                PreparedStatement pst = null;

                try {
                    pst = conexion.prepareStatement(sql);
                    pst.setString(1, socio.getIdSocio());
                    pst.setString(2, socio.getDni());
                    pst.setString(3, socio.getNombre());
                    pst.setString(4, socio.getApellidos());
                    pst.setString(5, socio.getTelefono());
                    pst.setString(6, socio.getEmail());
                    pst.executeUpdate();
                    return true;

                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }

    }

    /**
     * Elimina un socio de la base de datos
     * <p>
     * Comprueba que el socio no sea null y que su ID no sea null
     *
     * @param socio socio a eliminar
     * @return true si se ha eliminado correctamente, false en caso contrario
     * @throws SQLException           si hay un error al conectarse con la base de datos
     * @throws IdObligatorioException si el socio es null o su ID no es válido
     * @author Alejandro
     */
    public boolean bajaSocio(Socio socio) throws SQLException, IdObligatorioException {
        //Validamos que el socio no sea null
        if (socio == null) {
            throw new IdObligatorioException("El socio no puede ser nulo.");
            //Validamos que el id del socio no sea null
        } else if (socio.getIdSocio() == null || socio.getIdSocio().isBlank()) {
            throw new IdObligatorioException("El ID del socio es obligatorio.");

        } else {
            //Comprobamos reservas futuras
            String sqlReservas = "SELECT COUNT(*) FROM reservas WHERE id_socio=? AND fecha >= ?";
            PreparedStatement pstReservas = conexion.prepareStatement(sqlReservas);
            pstReservas.setString(1, socio.getIdSocio());
            pstReservas.setDate(2, new java.sql.Date(System.currentTimeMillis()));
            ResultSet rsReservas = pstReservas.executeQuery();
            rsReservas.next();
            if (rsReservas.getInt(1) > 0) {
                //Tiene reservas pendientes, No se puede borrar
                return false;
            } else {


                //Comprobamos si el socio ya existe en la base de datos

                String sqlComprobar = "SELECT COUNT(*) FROM socios WHERE id_socio = ? OR dni = ? OR email = ?";

                PreparedStatement pstComprobar = conexion.prepareStatement(sqlComprobar);
                pstComprobar.setString(1, socio.getIdSocio());
                pstComprobar.setString(2, socio.getDni());
                pstComprobar.setString(3, socio.getEmail());

                ResultSet rs = pstComprobar.executeQuery();
                rs.next();

                if (rs.getInt(1) == 0) {
                    //Comprobamos que el usuario no existe en la base de datos y si no lo elimina
                    return false;
                } else {
                    //Eliminamos
                    String sql = "DELETE from socios where id_socio=? ";
                    PreparedStatement pst = conexion.prepareStatement(sql);
                    pst.setString(1, socio.getIdSocio());
                    pst.executeUpdate();
                    return true;
                }
            }
        }
    }

    /**
     * Inserta una nueva pista en la base de datos
     *
     * @param pista Pista a insertar
     * @return true si se ha insertado correctamente, false en caso contrario
     * @throws SQLException           si hay un error al conectarse con la base de datos
     * @throws IdObligatorioException si el objeto Pista o su ID es nulo o está vacío
     * @author Alejandro
     */
    public boolean altaPista(Pista pista) throws SQLException, IdObligatorioException {
        //Validamos que el objeto pista no sea null
        if (pista == null) {
            throw new IdObligatorioException("Pista no puede ser null");
        } else if (pista.getIdPista() == null || pista.getIdPista().isBlank()) {
            throw new IdObligatorioException("ID de la pista es obligatorio");
        } else {
            //Comprobamos si pista ya existe
            String sqlComprobacion = "SELECT COUNT(*) FROM pistas WHERE id_pista=?";
            PreparedStatement pstComprobacion = conexion.prepareStatement(sqlComprobacion);
            pstComprobacion.setString(1, pista.getIdPista());
            ResultSet rs = pstComprobacion.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                return false;//Si existe devolvemos false
            } else {
                //Si no existe insertamos
                String sql = "INSERT INTO pistas(id_pista,deporte,descripcion,disponible) VALUES (?,?,?,?)";
                PreparedStatement pst = conexion.prepareStatement(sql);
                pst.setString(1, pista.getIdPista());
                pst.setString(2, pista.getDeporte());
                pst.setString(3, pista.getDescripcion());
                pst.setBoolean(4, pista.isDisponible());
                pst.executeUpdate();
                return true;//Si no existe devolvemos true

            }


        }
    }

    /**
     * Cambia la disponibilidad de una pista.
     *
     * @param idPista    ID de la pista a modificar
     * @param disponible nuevo estado de disponibilidad
     * @return true si se actualizó correctamente, false si la pista no existe
     * @throws SQLException           si hay un error al conectarse con la base de datos
     * @throws IdObligatorioException si el idPista es null o vacío
     * @author Llorente
     */

    public boolean cambiarDisponibilidadPista(String idPista, boolean disponible) throws SQLException {
        String sql = "UPDATE pistas SET disponible=? WHERE id_pista=?";
        try (PreparedStatement pst = conexion.prepareStatement(sql)) {
            pst.setBoolean(1, disponible);
            pst.setString(2, idPista);
            int filas = pst.executeUpdate();
            return filas > 0;
        }
    }


    /**
     * Crea una nueva reserva utilizando el PROCEDIMIENTO ALMACENADO.
     * <p>
     * REGLAS:
     * 1. Se validan los campos obligatorios en Java (ID, nulls).
     * 2. La validación de disponibilidad, solapes y el cálculo del precio (usando la función)
     * se delegan al script SQL `sp_crear_reserva`.
     *
     * @param reserva Objeto Reserva a insertar
     * @return true si se ha insertado correctamente, false si hay conflicto lógico (capturado del SP)
     * @throws SQLException           si hay un error de conexión o error SQL no controlado
     * @throws IdObligatorioException si campos obligatorios son nulos
     */
    public boolean crearReserva(Reserva reserva) throws SQLException, IdObligatorioException {
        // 1. Validaciones básicas en Java (Input validation)
        if (reserva == null) throw new IdObligatorioException("Reserva no puede ser null");
        if (reserva.getIdReserva() == null || reserva.getIdReserva().isBlank())
            throw new IdObligatorioException("ID de la reserva obligatorio");
        if (reserva.getIdSocio() == null || reserva.getIdPista() == null)
            throw new IdObligatorioException("ID de socio y pista obligatorios");

        // 2. Llamada al Procedimiento Almacenado
        // El SP valida pista operativa, valida solapes y CALCULA el precio usando fn_precio_reserva
        String sql = "{call sp_crear_reserva(?, ?, ?, ?, ?, ?)}";

        try (CallableStatement cst = conexion.prepareCall(sql)) {
            cst.setString(1, reserva.getIdReserva());
            cst.setString(2, reserva.getIdSocio());
            cst.setString(3, reserva.getIdPista());

            // Convertimos LocalDate/LocalTime de Java a SQL Date/Time
            cst.setDate(4, Date.valueOf(reserva.getFecha()));
            cst.setTime(5, Time.valueOf(reserva.getHoraInicio()));

            cst.setInt(6, reserva.getDuracionMin());

            // No pasamos el precio, el SP lo calcula e inserta.

            cst.execute();
            return true;

        } catch (SQLException e) {
            // El procedimiento almacenado lanza SIGNAL SQLSTATE '45000' si hay solape o pista no disponible.
            // Capturamos ese estado para devolver false (comportamiento esperado según tu código anterior)
            if ("45000".equals(e.getSQLState())) {
                System.err.println("No se pudo crear reserva (Regla de negocio): " + e.getMessage());
                return false;
            } else {
                // Si es otro error (conexión, clave foránea de socio no existente, etc.), lo lanzamos.
                throw e;
            }
        }
    }


    /**
     * Cancela una reserva existente en la base de datos.
     *
     * @param idReserva ID de la reserva a eliminar
     * @return true si se eliminó correctamente, false si la reserva no existe
     * @throws SQLException           si hay un error al conectarse con la base de datos
     * @throws IdObligatorioException si el idReserva es null o vacío
     * @author Llorente
     */

    public boolean cancelarReserva(String idReserva) throws SQLException {
        String sql = "DELETE FROM reservas WHERE id_reserva=?";
        try (PreparedStatement pst = conexion.prepareStatement(sql)) {
            pst.setString(1, idReserva);
            int filas = pst.executeUpdate();
            return filas > 0;
        }
    }

    /**
     * Recupera una lista de todas las pistas deportivas almacenadas en la base de datos.
     * <p>
     * La operación consulta la tabla "pistas" y mapea cada registro
     * a un objeto {@code Pista}.
     *
     * @return una lista de objetos {@code Pista} que representan las pistas del club.
     * Devuelve una lista vacía si no se encuentran pistas o si ocurre un error.
     *
     */
    public ArrayList<Pista> getPistas() {
        ArrayList<Pista> pistas = new ArrayList<>();
        String sql = "SELECT * FROM pistas";

        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                Pista p = new Pista(
                        rs.getString("id_pista"),
                        rs.getString("deporte"),
                        rs.getString("descripcion"),
                        rs.getBoolean("disponible") // Esto convierte automáticamente 1/0 a true/false
                );
                pistas.add(p);
            }

        } catch (SQLException | IdObligatorioException e) {
            System.err.println("Error cargando pistas: " + e.getMessage());
            e.printStackTrace();
        }
        return pistas;
    }

    /**
     * Recupera una lista de todas las reservas existentes en la base de datos.
     * <p>
     * La operación consulta la tabla "reservas" y transforma los registros
     * de la base de datos en objetos {@code Reserva}, realizando las conversiones
     * necesarias de tipos SQL (Date, Time) a tipos de Java (LocalDate, LocalTime).
     *
     * @return una lista de objetos {@code Reserva} que contienen todas las reservas.
     * Devuelve una lista vacía si no hay reservas o si ocurre un error en la carga.
     *
     */
    public ArrayList<Reserva> getReservas() {
        ArrayList<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT * FROM reservas";

        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                // Conversión de fechas y horas de SQL a Java
                java.time.LocalDate fecha = rs.getDate("fecha").toLocalDate();
                java.time.LocalTime hora = rs.getTime("hora_inicio").toLocalTime();

                // IMPORTANTE: Revisa el orden de tu constructor de Reserva
                Reserva r = new Reserva(
                        rs.getString("id_reserva"),
                        rs.getString("id_socio"),
                        rs.getString("id_pista"),
                        fecha,
                        hora,
                        rs.getInt("duracion_min"),
                        rs.getDouble("precio")
                );
                reservas.add(r);
            }

        } catch (SQLException e) { // Si Reserva no lanza IdObligatorioException, quita eso del catch
            System.err.println("Error cargando reservas: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            // Captura genérica por si acaso falla el constructor
            e.printStackTrace();
        }
        return reservas;
    }

    /**
     * Recupera una lista de todos los socios registrados en la base de datos.
     * <p>
     * La operación consulta la tabla "socios" y mapea cada registro
     * a un objeto {@code Socio}.
     *
     * @return una lista de objetos {@code Socio} que representan a todos los miembros del club.
     * Devuelve una lista vacía si no se encuentran socios o si ocurre un error en la carga.
     * @throws SQLException           si ocurre un error al acceder a la base de datos.
     * @throws IdObligatorioException si el constructor de {@code Socio} falla por datos nulos o inválidos.
     *
     */
    public ArrayList<Socio> getSocios() {
        ArrayList<Socio> socios = new ArrayList<>();
        String sql = "SELECT * FROM socios";

        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                Socio s = new Socio(
                        rs.getString("id_socio"),
                        rs.getString("dni"),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getString("telefono"),
                        rs.getString("email")
                );
                socios.add(s);
            }
        } catch (SQLException | IdObligatorioException e) {
            e.printStackTrace();
        }
        return socios;
    }

    /**
     * Metodo que sirve para cerrar conexión con la base de datos.
     */
    public void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
