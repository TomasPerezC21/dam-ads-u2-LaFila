package servicio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import modelo.*;

public class ClubDeportivo {
   private Connection conexion;

    public ClubDeportivo() throws SQLException {
        conexion= DriverManager.getConnection("jdbc:mysql://localhost:3306/club_dama",
                                        "root","alumno");
    }

    public ArrayList<Socio> getSocios() {
        ArrayList<Socio> socios = new ArrayList<>();
        return socios;
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
