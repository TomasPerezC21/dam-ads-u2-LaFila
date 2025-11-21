package vista;

import modelo.*;
import servicio.ClubDeportivo;
import vista.views.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.sql.SQLException;

/**
 * MainApp es el punto de entrada de la aplicación y extiende la clase Application de JavaFX.
 * Se encarga de inicializar y gestionar la interfaz gráfica de usuario (GUI) para el sistema del Club DAMA Sports.
 * La ventana de la aplicación incluye funcionalidades para la gestión de socios, pistas, reservas
 * y distintas vistas generales, además de opciones para guardar y salir.
 *
 */
public class MainApp extends Application {

    private ClubDeportivo club;
    private BorderPane root;
    private Label status;

    /**
     * Inicia el escenario principal de la aplicación JavaFX. Inicializa el diseño principal, incluyendo
     * la barra de menú, la barra de estado y la vista predeterminada. Además, establece la conexión con
     * el sistema de gestión del club y maneja posibles errores de conectividad.
     *
     * @param stage el escenario principal de la aplicación JavaFX, que servirá como la ventana
     *              principal para mostrar la interfaz de usuario.
     */
    @Override
    public void start(Stage stage) {
        try {
            club = new ClubDeportivo();
            showInfo("Conectado");
        } catch (SQLException e) {
            showError("Error de conexion: " + e.getMessage());
        }


        root = new BorderPane();
        root.setTop(buildMenuBar());
        status = new Label("Listo");
        status.setPadding(new Insets(4));
        root.setBottom(status);

        // Vista por defecto
        root.setCenter(new DashboardView(club));

        Scene scene = new Scene(root, 960, 640);
        stage.setTitle("Club DAMA Sports");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Construye e inicializa la barra de menús de la aplicación, incluyendo todas las opciones necesarias
     * para la gestión del sistema. La barra de menús se organiza en las siguientes secciones:
     * <p>
     * - Archivo: permite guardar los datos y cerrar la aplicación.
     * - Socios: ofrece opciones para registrar y eliminar socios.
     * - Pistas: incluye funciones para crear nuevas pistas y modificar su disponibilidad.
     * - Reservas: permite crear y cancelar reservas.
     * - Ver: facilita el acceso a diferentes vistas, como el panel principal (dashboard).
     * <p>
     * Cada elemento del menú está asociado a una acción que actualiza la vista central o modifica el estado
     * general de la aplicación.
     *
     * @return la instancia de MenuBar completamente configurada con todos sus menús y opciones.
     */
    private MenuBar buildMenuBar() {
        MenuBar mb = new MenuBar();

        // ---- SOCIOS ----
        Menu socios = new Menu("Socios");
        MenuItem altaSocio = new MenuItem("Alta socio");
        altaSocio.setOnAction(e -> root.setCenter(new SocioFormView(club)));
        MenuItem bajaSocio = new MenuItem("Baja socio");
        bajaSocio.setOnAction(e -> root.setCenter(new BajaSocioView(club)));
        socios.getItems().addAll(altaSocio, bajaSocio);

        // ---- PISTAS ----
        Menu pistas = new Menu("Pistas");
        MenuItem altaPista = new MenuItem("Alta pista");
        altaPista.setOnAction(e -> root.setCenter(new PistaFormView(club)));
        MenuItem cambiarDisp = new MenuItem("Cambiar disponibilidad");
        cambiarDisp.setOnAction(e -> root.setCenter(new CambiarDisponibilidadView(club)));
        pistas.getItems().addAll(altaPista, cambiarDisp);

        // ---- RESERVAS ----
        Menu reservas = new Menu("Reservas");
        MenuItem crearReserva = new MenuItem("Crear reserva");
        crearReserva.setOnAction(e -> root.setCenter(new ReservaFormView(club)));
        MenuItem cancelarReserva = new MenuItem("Cancelar reserva");
        cancelarReserva.setOnAction(e -> root.setCenter(new CancelarReservaView(club)));
        reservas.getItems().addAll(crearReserva, cancelarReserva);

        // ---- VER ----
        Menu ver = new Menu("Ver");
        MenuItem dashboard = new MenuItem("Dashboard");
        dashboard.setOnAction(e -> root.setCenter(new DashboardView(club)));
        ver.getItems().addAll(dashboard);

        // ---- ARCHIVO ----
        Menu archivo = new Menu("Archivo");
        MenuItem guardar = new MenuItem("Guardar");
        guardar.setOnAction(e -> {
            try {
                //    LLamo al método del modelo para guardar los datos en fichero

            } catch (Exception ex) {
                showError("Error guardando: " + ex.getMessage());
            }
        });
        MenuItem salir = new MenuItem("Salir");
        /**
         * metodo para salir de la app, llama a un metodo de la clase
         * servicio que cierra la conexión con el servidor de la base de datos
         *
         */
        salir.setOnAction(e -> {
            try {
                club.cerrarConexion();
            } catch (Exception ignored) {
            }
            Platform.exit();
        });
        archivo.getItems().addAll(guardar, new SeparatorMenuItem(), salir);

        mb.getMenus().addAll(archivo, socios, pistas, reservas, ver);
        return mb;
    }


    public void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }

    public void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.setHeaderText("Error");
        a.showAndWait();
    }

    /**
     * Detiene la aplicación JavaFX y ejecuta las tareas de limpieza necesarias.
     * Este método se asegura de que los recursos utilizados se liberen correctamente
     * y de que se persistan los datos necesarios antes de que la aplicación finalice.
     *
     * Este método sobrescrito intenta primero llamar a un método específico del modelo
     * encargado de guardar datos o realizar operaciones necesarias para la finalización
     * de la aplicación. Si ocurre alguna excepción durante este proceso, se ignora
     * intencionadamente para evitar interrumpir la secuencia de cierre.
     *
     * Tras completar las operaciones de limpieza personalizadas, se invoca el método
     * stop de la superclase para completar el proceso de cierre de la aplicación.
     *
     * @throws Exception si ocurre un error durante el proceso de apagado.
     */
    @Override
    public void stop() throws Exception {
        try {
            //   LLamo al método del modelo para guardar los datos
        } catch (Exception ignored) {
        }
        super.stop();
    }

    /**
     * Punto de entrada principal de la aplicación. Este método inicia la aplicación JavaFX
     * llamando al método {@code launch}, al cual se le pasan los argumentos recibidos.
     *
     * @param args los argumentos de línea de comandos proporcionados a la aplicación
     */
    public static void main(String[] args) {
        launch(args);
    }
}
