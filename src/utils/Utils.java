package utils;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Acount;
import model.AcountDAOException;
import model.User;

/**
 *
 * @author
 */
public class Utils {

    public static Boolean checkEditEmail(TextField Email) {
        return User.checkEmail(Email.getText());
    }

    public static Boolean checkEditPass(TextField Pass) {
        return User.checkPassword(Pass.getText());
    }

    public static int checkEquals(TextField Pass, TextField Rpass) {
        return Pass.getText().compareTo(Rpass.getText());
    }

    public static boolean validarNickname(TextField t) throws IOException, AcountDAOException {
        Acount ac = Acount.getInstance();
        boolean existe = ac.existsLogin(t.getText());
        return User.checkNickName(t.getText()) && !existe;
    }

    public static boolean validarDatos(TextField t) {
        String s = t.getText();
        return (!s.isEmpty()) && (s.trim().length() != 0);
    }

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
}
