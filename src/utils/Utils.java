package utils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import model.Acount;
import model.AcountDAOException;
import model.Category;
import model.Charge;

/**
 *
 * @author
 */
public class Utils {

    public static Boolean AcabarSesion() throws AcountDAOException, IOException {
        String css = Utils.class.getResource("/estilos/Alert.css").toExternalForm(); // Mover la definición de css aquí
        Alert alert = new Alert(AlertType.CONFIRMATION);

        alert.setTitle("Diálogo de confirmación");
        alert.setHeaderText("Cerrar sesión");
        alert.setContentText("¿Seguro que quieres cerrar sesión?");
        alert.getDialogPane().getStylesheets().add(css);
        alert.getDialogPane().getStyleClass().add("custom-alert");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/imagenes/logo.jpeg"));
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            return true;
        }
        return false;
    }

    public static String ElegirImagen(Window n) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg"));
            File initialDirectory = new File(System.getProperty("user.home") + "/Downloads");
            fileChooser.setInitialDirectory(initialDirectory);
            // Mostrar el diálogo de selección de archivos
            File file = fileChooser.showOpenDialog(n);
            if (file != null) {
                // Convertir la ruta del archivo a una URL y cargar la imagen
                return file.toURI().toString();
            }
        } catch (Exception e) {
            System.out.println("Se produjo un error al seleccionar la imagen: " + e.getMessage());
        }
        return null;
    }

    public static Boolean checkNames(String n) {

        // If the password is empty , return false
        if (n == null) {
            return false;
        }
        // Regex to check valid password.
        String regex = "^[a-zA-ZñÑáéíóúÁÉÍÓÚüÜ\\s]+$";

        // Compile the ReGex
        Pattern pattern = Pattern.compile(regex);
        // Match ReGex with value to check
        Matcher matcher = pattern.matcher(n);
        return matcher.matches();

    }

    public static Boolean checkDigit(String n) {

        // If the password is empty , return false
        if (n == null) {
            return false;
        }
        // Regex to check valid password.
        String regex = "^\\d+(\\.\\d+)?$";

        // Compile the ReGex
        Pattern pattern = Pattern.compile(regex);
        // Match ReGex with value to check
        Matcher matcher = pattern.matcher(n);
        return matcher.matches();

    }

    public static void mostrarAlerta(String mensaje) {
        String css = Utils.class.getResource("/estilos/Alert.css").toExternalForm();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.getDialogPane().getStylesheets().add(css);
        alert.getDialogPane().getStyleClass().add("warning-alert");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/imagenes/logo.jpeg"));
        alert.showAndWait();
    }

    public static void mostrarError(String mensaje) {
        String css = Utils.class.getResource("/estilos/Alert.css").toExternalForm();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.getDialogPane().getStylesheets().add(css);
        alert.getDialogPane().getStyleClass().add("error-alert");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/imagenes/logo.jpeg"));
        alert.showAndWait();
    }

    public static void mostrarInfo(String mensaje) {
        String css = Utils.class.getResource("/estilos/Alert.css").toExternalForm();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Información");
        alert.setContentText(mensaje);
        alert.getDialogPane().getStylesheets().add(css);
        alert.getDialogPane().getStyleClass().add("info-alert");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/imagenes/logo.jpeg"));
        alert.showAndWait();
    }

    static Tooltip tooltip = new Tooltip("Formato no válido");

    public static void error(TextField n) {
        n.styleProperty().setValue("-fx-background-color: #EC7063");
        // Asociar el Tooltip con el TextField
        n.setTooltip(tooltip);
        tooltip.setStyle("-fx-background-color: #ea4343; -fx-text-fill: white");
        Point2D p = n.localToScreen(n.getLayoutBounds().getMaxX(),
                n.getLayoutBounds().getMaxY()); // Posición del TextField
        tooltip.show(n, p.getX(), p.getY());

    }

    public static void error(TextArea n) {
        n.styleProperty().setValue("-fx-background-color: #EC7063");
        // Asociar el Tooltip con el TextField
        n.setTooltip(tooltip);
        tooltip.setStyle("-fx-background-color: #ea4343; -fx-text-fill: white");
        Point2D p = n.localToScreen(n.getLayoutBounds().getMaxX(),
                n.getLayoutBounds().getMaxY()); // Posición del TextField
        tooltip.show(n, p.getX(), p.getY());

    }

    public static void correct(TextField n) {
        n.styleProperty().setValue("-fx-background-color: #ffffff");
        tooltip.hide();
    }

    public static void correct(TextArea n) {
        n.styleProperty().setValue("-fx-background-color: #ffffff");
        tooltip.hide();
    }

    public static Boolean exist(Category cat) throws AcountDAOException, IOException {
        int elem = 0;
        List<Charge> charges = Acount.getInstance().getUserCharges();
        for (Charge charge : charges) {
            if (charge.getCategory().getName().equals(cat.getName())) {
                elem += 1;
            }
        }
        return elem > 0;
    }

    // Método para aplicar un filtro numérico a un campo de texto
    public static void applyFilter(TextField textField) {
        // Crear un formateador de texto que solo acepte números enteros
        TextFormatter<Integer> textFormatter = new TextFormatter<>(new IntegerStringConverter(), null,
                c -> {
                    if (c.getControlNewText().matches("\\d*")) { // Solo permite dígitos
                        return c;
                    } else {
                        return null; // Rechaza la entrada si no es un dígito
                    }
                });

        // Aplicar el formateador de texto al campo de texto
        textField.setTextFormatter(textFormatter);
    }

    public static void applyDoubleFilter(TextField textField) {
        // Crear un formateador de texto que solo acepte números decimales
        TextFormatter<Double> textFormatter = new TextFormatter<>(new DoubleStringConverter(), null, c -> {
            // Permitir solo dígitos, comas, y puntos
            if (c.getControlNewText().matches("\\d*|\\d+([.,]\\d*)?")) {
                return c;
            }
            return null;
        });

        // Añadir un listener para reemplazar comas con puntos sin añadir un cero detrás
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.contains(",")) {
                textField.setText(newValue.replace(',', '.'));
            }
        });

        // Aplicar el formateador de texto al campo de texto
        textField.setTextFormatter(textFormatter);
    }
}
