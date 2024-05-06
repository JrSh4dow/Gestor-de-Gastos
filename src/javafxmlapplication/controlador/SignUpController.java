package javafxmlapplication.controlador;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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

    // properties to control valid fieds values.
    private BooleanProperty validPassword;
    private BooleanProperty validEmail;
    private BooleanProperty ExistsNickName;
    private BooleanProperty equalPasswords;

    private Button Aceptar;
    @FXML
    private TextField Name;
    @FXML
    private TextField NickName;
    @FXML
    private TextField Email;
    @FXML
    private TextField SurName;

    private String Avatar;
    @FXML
    private Button botonVolver;
    @FXML
    private ComboBox<String> comboBoxRegistro;
    @FXML
    private PasswordField Rpass;
    @FXML
    private PasswordField Pass;
    @FXML
    private Button Registrar;

    // When to strings are equal, compareTo returns zero
    private final int EQUALS = 0;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        validEmail = new SimpleBooleanProperty();
        validPassword = new SimpleBooleanProperty();
        ExistsNickName = new SimpleBooleanProperty();
        equalPasswords = new SimpleBooleanProperty();

        validPassword.setValue(Boolean.FALSE);
        validEmail.setValue(Boolean.FALSE);
        ExistsNickName.setValue(Boolean.FALSE);
        equalPasswords.setValue(Boolean.FALSE);

        comboBoxRegistro.getItems().add("/avatars/default.PNG");
        comboBoxRegistro.setCellFactory(c -> new ImagenTabCell());
    }

    private Boolean checkEditEmail() {
        return User.checkEmail(Email.getText());
    }

    private Boolean checkEditPass() {
        return User.checkPassword(Pass.getText());
    }

    private void checkEquals() {
        if (Pass.getText().compareTo(Rpass.getText()) != EQUALS) {
            equalPasswords.setValue(Boolean.FALSE);
        }
    }

    private void SelectImage(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg"));

            // Mostrar el diálogo de selección de archivos
            File file;
            file = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
            if (file != null) {
                // Convertir la ruta del archivo a una URL y cargar la imagen
                Avatar = file.toURI().toString();
            }
        } catch (Exception e) {
            System.out.println("Se produjo un error al seleccionar la imagen: " + e.getMessage());
        }
    }

    private boolean validarNickname(TextField t) throws IOException, AcountDAOException {
        Acount ac = Acount.getInstance();
        boolean existe = ac.existsLogin(t.getText());
        String s = t.getText();
        return validarDatos(t) && !existe;
    }

    private boolean validarDatos(TextField t) {
        String s = t.getText();
        return (!s.isEmpty()) && (s.trim().length() != 0);
    }

    @FXML
    private void volverClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../vista/Inicio.fxml"));
        Parent userRoot = loader.load();
        Stage loginStage = new Stage();
        loginStage.getIcons().add(new Image(this.getClass().getResourceAsStream("../imagenes/logo-sin.jpeg")));
        loginStage.setTitle("Expense Tracker");
        loginStage.setScene(new Scene(userRoot));
        loginStage.show();
        Stage stage = (Stage) botonVolver.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void registrarClicked(ActionEvent event) throws IOException, AcountDAOException {
        Acount acount = Acount.getInstance();
        checkEquals();
        if (validarDatos(Name) && validarDatos(SurName) && validarNickname(NickName) && checkEditPass()
                && checkEditEmail()) {
            Image img;
            if (comboBoxRegistro.getValue() == null) {
                img = new Image("/avatars/default.png");
            } else {
                img = new Image(comboBoxRegistro.getValue());
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

    class ImagenTabCell extends ComboBoxListCell<String> {

        private ImageView view = new ImageView();
        private Image imagen;

        @Override
        public void updateItem(String t, boolean bln) {
            super.updateItem(t, bln);
            if (t == null || bln) {
                setText(null);
                setGraphic(null);
            } else {
                imagen = new Image(t, 50, 60, true, true);
                view.setImage(imagen);
                setGraphic(view);
                setText(null);
            }
        }
    }
}