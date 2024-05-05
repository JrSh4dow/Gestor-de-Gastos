package javafxmlapplication;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;

/**
 * FXML Controller class
 *
 * @author MEG
 */
public class LogInController implements Initializable {

    @FXML
    private TextField nickName;
    @FXML
    private TextField pass;
    @FXML
    private Button Aceptar;
    @FXML
    private Button Cancelar;
    @FXML
    private Label ErrorLogIn;

    private BooleanProperty validAcount;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        validAcount = new SimpleBooleanProperty();

        Aceptar.disableProperty().bind(validAcount);
        Cancelar.setOnAction((event) -> {
            Cancelar.getScene().getWindow().hide();
        });
    }

    @FXML
    private void Acceptar(ActionEvent event) throws AcountDAOException, IOException {
        checkLogIn();
        // ----------------------------------------------------------------------------------------------
        // ----- Descomenta lo de abajo cuando creas la vista de la pagina principal en
        // la carpeta vista
        // ----------------------------------------------------------------------------------------------
        // FXMLLoader miCargador = new
        // FXMLLoader(getClass().getResource("/vista/Main.fxml"));
        // Parent root = miCargador.load();
        //
        // // acceso al controlador de datos persona
        // FXMLMainController main = miCargador.getController();
        //
        // Scene scene = new Scene(root,500,300);
        // Stage stage = new Stage();
        // stage.setScene(scene);
        // stage.setTitle("Home Page");
        // stage.initModality(Modality.APPLICATION_MODAL);
        // //la ventana se muestra modal
        // stage.show();
    }

    @FXML
    private void SignUpAction(ActionEvent event) throws IOException {
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("\\vista\\SignUp.fxml"));
        Parent root = miCargador.load();

        // acceso al controlador de datos persona
        // SignUpController signup = miCargador.getController();

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Sign Up");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        // la ventana se muestra modal
        stage.showAndWait();
    }

    private void checkLogIn() throws AcountDAOException, IOException {
        boolean isOK = Acount.getInstance().logInUserByCredentials(nickName.getText(), pass.getText());
        if (!isOK) {
            // Incorrect email
            manageError(ErrorLogIn, pass, validAcount);
            manageError(ErrorLogIn, nickName, validAcount);
            nickName.requestFocus();
        } else {
            manageCorrect(ErrorLogIn, pass, validAcount);
            manageCorrect(ErrorLogIn, nickName, validAcount);
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

}
