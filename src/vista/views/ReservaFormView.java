package vista.views;

import modelo.*;
import servicio.ClubDeportivo;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.time.LocalTime;


public class ReservaFormView extends GridPane {
    public ReservaFormView(ClubDeportivo club) {
        setPadding(new Insets(12));
        setHgap(8); setVgap(8);

        TextField id = new TextField();
        ComboBox<Socio> idSocio = new ComboBox();

        if (club.getSocios() != null) {
            idSocio.getItems().addAll(club.getSocios());
        }

        ComboBox<Pista> idPista = new ComboBox();

        if (club.getPistas() != null) {
            idPista.getItems().addAll(club.getPistas());
        }

        DatePicker fecha = new DatePicker(LocalDate.now());
        TextField hora = new TextField("10:00");
        Spinner<Integer> duracion = new Spinner<>(30, 300, 60, 30);
        TextField precio = new TextField("10.0");
        Button crear = new Button("Reservar");

        addRow(0, new Label("idReserva*"), id);
        addRow(1, new Label("Socio*"), idSocio);
        addRow(2, new Label("Pista*"), idPista);
        addRow(3, new Label("Fecha*"), fecha);
        addRow(4, new Label("Hora inicio* (HH:mm)"), hora);
        addRow(5, new Label("Duración (min)"), duracion);
        addRow(6, new Label("Precio (€)"), precio);
        add(crear, 1, 7);

        crear.setOnAction(e -> {
            try {
                // 1. Validaciones de seguridad para evitar el NullPointerException
                if (idSocio.getValue() == null || idPista.getValue() == null || fecha.getValue() == null) {
                    showError("Faltan datos: Debes seleccionar Socio, Pista y Fecha.");
                    return;
                }

                // Regla de Negocio: No se puede reservar en el pasado
                if (fecha.getValue().isBefore(LocalDate.now())) {
                    showError("No se puede reservar en una fecha pasada.");
                    return;
                }

                // 2. Parseo de la hora (Si el formato está mal, saltará al catch)
                LocalTime t = LocalTime.parse(hora.getText());

                // 3. Crear la reserva
                Reserva r = new Reserva(
                        id.getText(),
                        idSocio.getValue().getIdSocio(),
                        idPista.getValue().getIdPista(),
                        fecha.getValue(),
                        t,
                        duracion.getValue(),
                        Double.parseDouble(precio.getText())
                );

                boolean ok = club.crearReserva(r);

                if (ok) {
                    showInfo("Reserva realizada correctamente.");

                    // 4. Limpiar campos
                    id.clear();
                    idSocio.setValue(null);
                    idPista.setValue(null);
                    fecha.setValue(null);
                    hora.clear();
                    precio.clear();

                } else {
                    showError("Error al crear Reserva (quizás ID repetido o pista ocupada).");
                }

            } catch (java.time.format.DateTimeParseException dtpe) {
                showError("El formato de la hora debe ser HH:mm (ej: 14:30)");
            } catch (NumberFormatException nfe) {
                showError("El precio debe ser un número válido.");
            } catch (Exception ex) {
                showError("Error inesperado: " + ex.getMessage());
            }
        });
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.setHeaderText("Error");
        a.showAndWait();
    }
    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }
}
