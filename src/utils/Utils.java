package utils;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import model.AcountDAOException;

/**
 *
 * @author
 */
public class Utils {

    public static Boolean AcabarSesion() throws AcountDAOException, IOException {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Diálogo de confirmación");
        alert.setHeaderText("Cabecera");
        alert.setContentText("¿Seguro que quieres cerrar sesión?");
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

            // Mostrar el diálogo de selección de archivos
            File file;
            file = fileChooser.showOpenDialog(n);
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

    public static void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public static void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public static void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Información");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public static void error(TextField n) {
        n.styleProperty().setValue("-fx-background-color: #ea4343");
    }

    public static void correct(TextField n) {
        n.styleProperty().setValue("-fx-background-color: #ffffff");
    }

}
