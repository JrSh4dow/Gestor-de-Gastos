package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
import javafx.stage.Modality;
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

    @FXML
    private Button botonVolver;

    /**
     * Initializes the controller class.
     */

    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void Acceptar(ActionEvent event) throws AcountDAOException, IOException {
        Acount acount = Acount.getInstance();
        Boolean ok = acount.logInUserByCredentials(nickName.getText(), pass.getText());

        if (nickName.getText().isEmpty() && (nickName.getText().trim().length() != 0)
                || pass.getText().isEmpty() && (pass.getText().trim().length() != 0)) {
            mostrarAlerta("Por favor rellena los campos para poder iniciar sesión");

        } else if (!acount.existsLogin(nickName.getText())) {
            mostrarAlerta("No existe el nickname. Por favor regístrate");
        } else if (ok == true) {
            Stage stage = (Stage) Aceptar.getScene().getWindow();
            stage.close();
            mostrarAlerta("Inicio de sesión con éxito, bienvenido " + nickName.getText());
            FXMLLoader miCargador = new FXMLLoader(getClass().getResource("../vista/Main.fxml"));
            Parent root = miCargador.load();
            Stage mainStage = new Stage();
            mainStage.setScene(new Scene(root));
            mainStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/imagenes/logo-sin.png")));
            mainStage.setTitle("Principal");
            mainStage.setResizable(false);
            mainStage.initModality(Modality.APPLICATION_MODAL);
            mainStage.show();
            Stage mainStage2 = (Stage) Aceptar.getScene().getWindow();
            mainStage2.close();

        } else {
            mostrarAlerta("Por favor proporciona los datos necesarios");
        }

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

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
