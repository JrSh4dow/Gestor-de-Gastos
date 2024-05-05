package javafxmlapplication;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
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

    @FXML
    private Button Aceptar;
    @FXML
    private Button Cancelar;
    @FXML
    private TextField Name;
    @FXML
    private TextField NickName;
    @FXML
    private Label NickNameExists;
    @FXML
    private TextField Email;
    @FXML
    private Label ErrorEmail;
    @FXML
    private TextField Pass;
    @FXML
    private Label ErrorPass;
    @FXML
    private TextField SurName;

    private String Avatar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        validEmail = new SimpleBooleanProperty();
        validPassword = new SimpleBooleanProperty();
        ExistsNickName = new SimpleBooleanProperty();

        validPassword.setValue(Boolean.FALSE);
        validEmail.setValue(Boolean.FALSE);
        ExistsNickName.setValue(Boolean.FALSE);

        Aceptar.disableProperty().bind(validEmail.not().or(validPassword.not().or(ExistsNickName.not())));
        Cancelar.setOnAction((event) -> {
            Cancelar.getScene().getWindow().hide();
        });

        // Check values when user leaves email edits
        Email.focusedProperty()
                .addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    if (!newValue) { // focus lost.
                        if (User.checkEmail(Email.getText())) {
                            manageCorrect(ErrorEmail, Email, validEmail);
                        } else {
                            manageError(ErrorEmail, Email, validEmail);
                        }
                    }
                });

        // Check values when user leaves password edits
        Pass.focusedProperty()
                .addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    if (!newValue) { // focus lost.
                        if (!User.checkPassword(Pass.getText())) {
                            manageCorrect(ErrorPass, Pass, validPassword);
                        } else {
                            manageError(ErrorPass, Pass, validPassword);
                        }
                    }
                });
        NickName.focusedProperty()
                .addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    if (!newValue) { // focus lost.
                        if (!User.checkNickName(NickName.getText())) {
                            manageCorrect(NickNameExists, NickName, ExistsNickName);
                        } else {
                            manageError(NickNameExists, NickName, ExistsNickName);
                        }
                    }
                });

    }

    @FXML
    private void handleAction(ActionEvent event) throws AcountDAOException, IOException {
        // ----------------------------------------------
        // Al darle al boton no funciona y no sé porque
        // ----------------------------------------------
        if (Avatar == null) {
            Avatar = "/avatars/default.png";
        }
        LocalDate date = LocalDate.now();
        if ((!Name.getText().isEmpty())
                && (Name.getText().trim().length() != 0) && (!SurName.getText().isEmpty())
                && (SurName.getText().trim().length() != 0)
                && validEmail.equals(true) && validPassword.equals(true) && ExistsNickName.equals(false)) {
            // ============================================
            // creamos usuario
            boolean isOK = Acount.getInstance().registerUser(Name.getText(), SurName.getText(), Email.getText(),
                    NickName.getText(), Pass.getText(), new Image(Avatar), date);
            if (isOK) {
                Alert alert = new Alert(AlertType.INFORMATION,
                        "You signed Up correctly, please Login to enter the app");
                alert.showAndWait();
            }
            // vaciamos los campos despues de añadir a la lista
            Name.clear();
            NickName.clear();
            Email.clear();
            Pass.clear();
            SurName.clear();

            // cambio del foco al textfield inicial
            Name.requestFocus();
            Aceptar.getScene().getWindow().hide();
        }

    }

    private void manageError(Label errorLabel, TextField textField, BooleanProperty boolProp) {
        boolProp.setValue(Boolean.FALSE);
        showErrorMessage(errorLabel, textField);
        textField.requestFocus();

    }

    private void manageCorrect(Label errorLabel, TextField textField, BooleanProperty boolProp) {
        boolProp.setValue(Boolean.TRUE);
        hideErrorMessage(errorLabel, textField);

    }

    private void showErrorMessage(Label errorLabel, TextField textField) {
        errorLabel.visibleProperty().set(true);
        textField.styleProperty().setValue("-fx-background-color: #FCE5E0");
    }

    private void hideErrorMessage(Label errorLabel, TextField textField) {
        errorLabel.visibleProperty().set(false);
        textField.styleProperty().setValue("");
    }

    @FXML
    private void SelectImage(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Abrir fichero");
            fileChooser.getExtensionFilters().addAll(
                    new ExtensionFilter("Imágenes", "*.png", "*.jpg"));
            File selectedFile = fileChooser.showOpenDialog(
                    ((Node) event.getSource()).getScene().getWindow());
            if (selectedFile != null) {
                Avatar = selectedFile.getAbsolutePath();
            }
        } catch (Exception e) {
            System.out.println("Se produjo un error al seleccionar la imagen: " + e.getMessage());
        }
    }

}