package controlador;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.Acount;
import model.AcountDAOException;

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
    private Button Cancelar;

    private BooleanProperty validAcount;
    @FXML
    private Button botonVolver;

    /**
     * Initializes the controller class.
     */

    public void initialize(URL url, ResourceBundle rb) {
        validAcount = new SimpleBooleanProperty();

        Aceptar.disableProperty().bind(validAcount);
        Cancelar.setOnAction((event) -> {
            Cancelar.getScene().getWindow().hide();
        });
    }

    @FXML
    private void Acceptar(ActionEvent event) throws AcountDAOException, IOException {
        Acount acount = Acount.getInstance();
        Boolean ok = acount.logInUserByCredentials(nickName.getText(), pass.getText());

        if (nickName.getText().isEmpty() || pass.getText().isEmpty()) {
            mostrarAlerta("Por favor rellena los campos para poder iniciar sesión");
            return;
        } else if (!acount.existsLogin(nickName.getText())) {
            mostrarAlerta("No existe el nickname. Por favor regístrate");
            return;
        }

        else if (ok != true) {
            // ----------------------------------------------------------------------------------------------
            // ----- Descomenta lo de abajo cuando creas la vista de la pagina principal en
            // la carpeta vista
            // ----------------------------------------------------------------------------------------------
            // Stage stage = (Stage) Aceptar.getScene().getWindow();
            // stage.close();
            // mostrarAlerta("Inicio de sesión con éxito, bienvenido " +
            // nickName.getText());
            // Control.cambiarLoggedIn(true);
            // FXMLLoader miCargador = new
            // FXMLLoader(getClass().getResource("../vista/Main.fxml"));
            // Parent root = miCargador.load();
            // MainController controlador = miCargador.getController();
            // Scene scene = new Scene(root, 600, 388.66666666666663);
            // Stage mainStage = new Stage();
            // mainStage.setScene(scene);
            // mainStage.getIcons().add(new
            // Image(this.getClass().getResourceAsStream("../imagenes/logo-sin.jpeg")));
            // mainStage.setTitle("Principal");
            // //stage2.setResizable(false);
            // mainStage.initModality(Modality.APPLICATION_MODAL);
            // mainStage.show();
            // mainStage = (Stage) Aceptar.getScene().getWindow();
            // mainStage.close();

        } else {
            mostrarAlerta("Por favor proporciona los datos necesarios");
        }

    }

    @FXML
    private void volverClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Inicio.fxml"));
        Parent userRoot = loader.load();
        Stage mainStage = new Stage();
        mainStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/imagenes/logo-sin.jpeg")));
        mainStage.setTitle("Expense Tracker");
        mainStage.setScene(new Scene(userRoot));
        mainStage.show();
        Stage stage = (Stage) botonVolver.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
