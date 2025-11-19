package servicio;

import java.sql.*;
import java.util.ArrayList;

import modelo.*;

public class ClubDeportivo {
    private Connection conexion;

    public ClubDeportivo() throws SQLException {
        conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/club_dama",
                "root", "alumnoDAM");

    }

    public ArrayList<Socio> getSocios() {
        ArrayList<Socio> socios = new ArrayList<>();
        return socios;
    }

    /**
     * Inserta un nuevo socio en la base de datos
     *
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
     *
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
    /**
     * Inserta una nueva pista en la base de datos
     *
     * @param pista Pista a insertar
     * @return true si se ha insertado correctamente, false en caso contrario
     * @throws SQLException si hay un error al conectarse con la base de datos
     * @throws IdObligatorioException si el objeto Pista o su ID es nulo o está vacío
     * @author Alejandro
     */
    public boolean altaPista(Pista pista) throws SQLException,IdObligatorioException {
        //Validamos que el objeto pista no sea null
        if (pista==null){
            throw new IdObligatorioException("Pista no puede ser null");
        }else if(pista.getIdPista()==null || pista.getIdPista().isBlank()){
            throw new IdObligatorioException("ID de la pista es obligatorio");
        }else{
            //Comprobamos si pista ya existe
            String sqlComprobacion="SELECT COUNT(*) FROM pistas WHERE id_pista=?";
            PreparedStatement pstComprobacion=conexion.prepareStatement(sqlComprobacion);
            pstComprobacion.setString(1,pista.getIdPista());
            ResultSet rs=pstComprobacion.executeQuery();
            rs.next();
            if(rs.getInt(1)>0){
                return false;//Si existe devolvemos false
            }else{
                //Si no existe insertamos
                String sql="INSERT INTO pistas(id_pista,deporte,descripcion,disponible) VALUES (?,?,?,?)";
                PreparedStatement pst=conexion.prepareStatement(sql);
                pst.setString(1,pista.getIdPista());
                pst.setString(2,pista.getDeporte());
                pst.setString(3,pista.getDescripcion());
                pst.setBoolean(4,pista.isDisponible());
                pst.executeUpdate();
                return true;//Si no existe devolvemos true

            }


        }
    }

    /**
     * Cambia la disponibilidad de una pista.
     *
     * @param idPista ID de la pista a modificar
     * @param disponible nuevo estado de disponibilidad
     * @return true si se actualizó correctamente, false si la pista no existe
     * @throws SQLException si hay un error al conectarse con la base de datos
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
     * Crea una nueva reserva en la base de datos.
     *
     * Comprueba que la pista esté disponible, que no existan solapes y que todos los campos sean válidos.
     *
     * @param reserva Objeto Reserva a insertar
     * @return true si se ha insertado correctamente, false si hay conflicto o la reserva ya existe
     * @throws SQLException si hay un error al conectarse con la base de datos
     * @throws IdObligatorioException si alguno de los campos de la reserva es null o inválido
     * @author Llorente
     */

    public boolean crearReserva(Reserva reserva) throws SQLException, IdObligatorioException {
        if (reserva == null) throw new IdObligatorioException("Reserva no puede ser null");
        if (reserva.getIdReserva() == null || reserva.getIdReserva().isBlank())
            throw new IdObligatorioException("ID de la reserva obligatorio");

        // Comprobar que la pista está disponible
        String sqlPista = "SELECT disponible FROM pistas WHERE id_pista=?";
        try (PreparedStatement pst = conexion.prepareStatement(sqlPista)) {
            pst.setString(1, reserva.getIdPista());
            try (ResultSet rs = pst.executeQuery()) {
                if (!rs.next()) throw new IdObligatorioException("Pista inexistente");
                if (!rs.getBoolean(1)) return false; // Pista no disponible
            }
        }

        // Comprobar solape de reservas en la misma fecha
        String sqlSolape = "SELECT COUNT(*) FROM reservas WHERE id_pista=? AND fecha=? AND " +
                "(? < ADDTIME(hora_inicio, SEC_TO_TIME(duracion_min*60)) AND ADDTIME(?, SEC_TO_TIME(?*60)) > hora_inicio)";
        try (PreparedStatement pst = conexion.prepareStatement(sqlSolape)) {
            pst.setString(1, reserva.getIdPista());
            pst.setDate(2, Date.valueOf(reserva.getFecha()));
            pst.setTime(3, Time.valueOf(reserva.getHoraInicio()));
            pst.setTime(4, Time.valueOf(reserva.getHoraInicio()));
            pst.setInt(5, reserva.getDuracionMin());
            try (ResultSet rs = pst.executeQuery()) {
                rs.next();
                if (rs.getInt(1) > 0) return false; // Solape
            }
        }

        // Insertar reserva
        String sqlInsert = "INSERT INTO reservas(id_reserva,id_socio,id_pista,fecha,hora_inicio,duracion_min,precio) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement pst = conexion.prepareStatement(sqlInsert)) {
            pst.setString(1, reserva.getIdReserva());
            pst.setString(2, reserva.getIdSocio());
            pst.setString(3, reserva.getIdPista());
            pst.setDate(4, Date.valueOf(reserva.getFecha()));
            pst.setTime(5, Time.valueOf(reserva.getHoraInicio()));
            pst.setInt(6, reserva.getDuracionMin());
            pst.setDouble(7, reserva.getPrecio());
            pst.executeUpdate();
            return true;
        }
    }


    /**
     * Cancela una reserva existente en la base de datos.
     *
     * @param idReserva ID de la reserva a eliminar
     * @return true si se eliminó correctamente, false si la reserva no existe
     * @throws SQLException si hay un error al conectarse con la base de datos
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
    public ArrayList<Pista> getPistas() {
        ArrayList<Pista> pistas = new ArrayList<>();
        return pistas;
    }

    public ArrayList<Reserva> getReservas() {
        ArrayList<Reserva> reservas = new ArrayList<>();
        return reservas;
    }
}
