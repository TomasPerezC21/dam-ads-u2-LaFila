package vista.views;

import servicio.ClubDeportivo;
import modelo.*;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.function.Consumer;

public class CambiarDisponibilidadView extends GridPane {
    public CambiarDisponibilidadView(ClubDeportivo club) {
        setPadding(new Insets(12));
        setHgap(8); setVgap(8);

        ComboBox<Pista> id = new ComboBox();
        CheckBox disponible = new CheckBox("Disponible");
        Button cambiar = new Button("Aplicar");

        addRow(0, new Label("idPista"), id);
        addRow(1, new Label("Estado"), disponible);
        add(cambiar, 1, 2);

        cambiar.setOnAction(e -> {
            try {
                // 1. Obtener el objeto Pista seleccionado del desplegable
                Pista pistaSeleccionada = id.getValue();

                // 2. Verificar que no sea nulo (que el usuario haya elegido algo)
                if (pistaSeleccionada == null) {
                    showError("Por favor, selecciona una pista.");
                    return;
                }

                // 3. Sacar el ID (String) del objeto Pista
                String idString = pistaSeleccionada.getIdPista();

                // 4. Llamar al metodo usando ese String
                boolean exito = club.cambiarDisponibilidadPista(idString, disponible.isSelected());

                if (exito) {
                    showInfo("Disponibilidad actualizada correctamente.");
                } else {
                    showError("No se pudo actualizar la pista.");
                }

            } catch (Exception ex) {
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
