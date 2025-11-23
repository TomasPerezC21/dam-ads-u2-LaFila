package vista.views;

import servicio.ClubDeportivo;
import modelo.*;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.function.Consumer;

public class BajaSocioView extends GridPane {

    public BajaSocioView(ClubDeportivo club) {
        setPadding(new Insets(12));
        setHgap(8); setVgap(8);

        ComboBox<Socio> id = new ComboBox<>();
        Button baja = new Button("Dar de baja");

        if (club.getSocios() != null) {
            id.getItems().addAll(club.getSocios());
        }

        addRow(0, new Label("Socio"), id);
        add(baja, 1, 1);

        baja.setOnAction(e -> {
            Socio socioSeleccionado = id.getValue();

            // 1. Evitar NullPointerException si el usuario no selecciona nada
            if (socioSeleccionado == null) {
                showError("Por favor, selecciona un socio de la lista.");
                return;
            }

            try {

                boolean exito = club.bajaSocio(socioSeleccionado);

                if (exito) {
                    // Si devuelve TRUE: Se borró correctamente
                    showInfo("Socio dado de baja con éxito");

                    // Actualizar la vista
                    id.getItems().remove(socioSeleccionado);
                    id.setValue(null);

                } else {
                    // Si devuelve FALSE: No se pudo borrar (Regla de negocio activada)
                    showError("No se pudo dar de baja. Posiblemente tenga reservas pendientes o ya no exista.");
                }

            } catch (Exception ex) {
                // Excepciones técnicas (Base de datos caída, etc.)
                showError("Error técnico al dar de baja: " + ex.getMessage());
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
