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
            String sqlComprobar = "SELECT COUNT(*) FROM socios WHERE id_socio = ? OR dni = ? OR email = ?\"";
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
                String sql = "DELETE from socios where id_socios=? ";
                PreparedStatement pst = conexion.prepareStatement(sql);
                pst.setString(1, socio.getIdSocio());
                pst.executeUpdate();
                return true;
            }
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
