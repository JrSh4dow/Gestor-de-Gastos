package utils;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import model.Acount;
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

    public static Boolean checkEmail(String email) {
        if (email == null) {
            return false;
        }
        // Regex to check valid email.
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        // Compile the ReGex
        Pattern pattern = Pattern.compile(regex);
        // Match ReGex with value to check
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static Boolean checkPass(String password) {

        // If the password is empty , return false
        if (password == null) {
            return false;
        }
        // Regex to check valid password.
        String regex = "^[A-Za-z0-9]{6,15}$";

        // Compile the ReGex
        Pattern pattern = Pattern.compile(regex);
        // Match ReGex with value to check
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();

    }

    public static boolean validarDatos(String t) {
        return (!t.isEmpty()) && (t.trim().length() != 0);
    }

    public static Boolean checkNickName(String nickname) throws AcountDAOException, IOException {
        Acount ac = Acount.getInstance();
        boolean existe = ac.existsLogin(nickname);
        String regex = "^[A-Za-z0-9_-]{6,15}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(nickname);
        return matcher.matches() && !existe;
    }
}
