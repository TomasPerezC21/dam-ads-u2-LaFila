package vista.views;
import modelo.*;
import servicio.ClubDeportivo;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.function.Consumer;

public class CancelarReservaView extends GridPane {
    public CancelarReservaView(ClubDeportivo club) {
        setPadding(new Insets(12));
        setHgap(8); setVgap(8);

        ComboBox<Reserva> id = new ComboBox();

        if (club.getReservas() != null) {
            id.getItems().addAll(club.getReservas());
        }

        Button cancelar = new Button("Cancelar reserva");

        addRow(0, new Label("Reserva"), id);
        add(cancelar, 1, 1);

        cancelar.setOnAction(e -> {

            try {
                // 1. Obtener la reserva seleccionada del ComboBox
                Reserva reservaSeleccionada = id.getValue();

                // 2. Validar que haya seleccionado algo
                if (reservaSeleccionada == null) {
                    showError("Por favor, selecciona una reserva de la lista.");
                    return;
                }

                // 3. Llamar al metodo del club usando el ID de la reserva
                boolean exito = club.cancelarReserva(reservaSeleccionada.getIdReserva());

                // 4. Mostrar mensaje según el resultado
                if (exito) {
                    showInfo("La reserva ha sido cancelada correctamente.");

                    // 5. Eliminar la reserva de la lista desplegable para que no salga más
                    id.getItems().remove(reservaSeleccionada);
                    id.setValue(null);
                } else {
                    showError("No se pudo cancelar la reserva.");
                }
            }catch (Exception ex) {
                showError(ex.getMessage());
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
