package modelo;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reserva {
    private final String idReserva; // único, inmutable
    private final String idSocio;
    private final String idPista;
    private  LocalDate fecha;
    private  LocalTime horaInicio;
    private int duracionMin; // > 0
    private double precio;

    public Reserva(String idReserva, String idSocio, String idPista, LocalDate fecha, LocalTime horaInicio, int duracionMin, double precio) throws IdObligatorioException {
        if (idReserva == null || idReserva.isBlank()) throw new IdObligatorioException("idReserva obligatorio");
        if (idSocio == null || idSocio.isBlank()) throw new IdObligatorioException("idSocio obligatorio");
        if (idPista == null || idPista.isBlank()) throw new IdObligatorioException("idPista obligatorio");
        if (fecha == null) throw new IdObligatorioException("fecha obligatoria");
        if (horaInicio == null) throw new IdObligatorioException("horaInicio obligatoria");
        if (duracionMin <= 0) throw new IdObligatorioException("duración debe ser > 0");
        if (precio < 0) throw new IdObligatorioException("precio debe ser >= 0");

        this.idReserva = idReserva;
        this.idSocio = idSocio;
        this.idPista = idPista;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.duracionMin = duracionMin;
        this.precio = precio;
    }

    public String getIdReserva() {
        return idReserva;
    }

    public String getIdSocio() {
        return idSocio;
    }

    public String getIdPista() {
        return idPista;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public int getDuracionMin() {
        return duracionMin;
    }

    public void setDuracionMin(int duracionMin) {
        this.duracionMin = duracionMin;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
