package controlador;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Acount;
import model.AcountDAOException;
import model.User;

/**
 * FXML Controller class
 *
 * @author MEG
 */
public class SignUpController implements Initializable {

    @SuppressWarnings("unused")
    private Button Aceptar;
    @FXML
    private TextField Name;
    @FXML
    private TextField NickName;
    @FXML
    private TextField Email;
    @FXML
    private TextField SurName;
    @FXML
    private String Avatar;
    @FXML
    private Button botonVolver;
    @FXML
    private PasswordField Rpass;
    @FXML
    private PasswordField Pass;
    @FXML
    private Button Registrar;

    // When to strings are equal, compareTo returns zero
    private final int EQUALS = 0;
    @FXML
    private ImageView avatar;
    @FXML
    private Button Imagen;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String image = "/avatars/default.png";
        avatar.setImage(new Image(image));
    }

    private Boolean checkEditEmail() {
        return User.checkEmail(Email.getText());
    }

    private Boolean checkEditPass() {
        return User.checkPassword(Pass.getText());
    }

    private int checkEquals() {
        return Pass.getText().compareTo(Rpass.getText());
    }

    private boolean validarNickname(TextField t) throws IOException, AcountDAOException {
        Acount ac = Acount.getInstance();
        boolean existe = ac.existsLogin(t.getText());
        return validarDatos(t) && !existe;
    }

    private boolean validarDatos(TextField t) {
        String s = t.getText();
        return (!s.isEmpty()) && (s.trim().length() != 0);
    }

    @FXML
    private void volverClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Inicio.fxml"));
        Parent userRoot = loader.load();
        Stage inicioStage = new Stage();
        inicioStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/imagenes/logo-sin.png")));
        inicioStage.setTitle("Expense Tracker");
        inicioStage.setScene(new Scene(userRoot));
        inicioStage.show();
        Stage stage = (Stage) botonVolver.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void registrarClicked(ActionEvent event) throws IOException, AcountDAOException {
        Acount acount = Acount.getInstance();
        int same = checkEquals();
        if (validarDatos(Name) && validarDatos(SurName) && validarNickname(NickName) && checkEditPass()
                && checkEditEmail() && same == EQUALS) {
            Image img;
            if (Avatar == null) {
                img = new Image("/avatars/default.png");
            } else {
                img = new Image(Avatar);
            }

            // registrar el nuevo miembro
            LocalDate date = LocalDate.now();
            Boolean ok = acount.registerUser(Name.getText(), SurName.getText(), Email.getText(), NickName.getText(),
                    Pass.getText(), img, date);
            if (ok) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setTitle("Información");
                alert.setContentText("Se ha registrado correctamente");
                alert.showAndWait();
            }
            Stage stage = (Stage) Registrar.getScene().getWindow();
            stage.close();
        } else {
            if (acount.existsLogin(NickName.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Error");
                alert.setContentText("Nickname ya existente");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Error");
                alert.setContentText("Completa correctamente los campos de registro");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void SetImage(ActionEvent event) {
        try {

            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg"));

            // Mostrar el diálogo de selección de archivos
            File file;
            file = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
            if (file != null) {
                // Convertir la ruta del archivo a una URL y cargar la imagen
                Avatar = file.toURI().toString();
                avatar.setImage(new Image(Avatar));
            }
        } catch (Exception e) {
            System.out.println("Se produjo un error al seleccionar la imagen: " + e.getMessage());
        }
    }
}