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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Acount;
import model.AcountDAOException;
import model.User;
import utils.Utils;

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

        if (User.checkNickName(nickName.getText())
                && User.checkPassword(pass.getText())) {
            if (!acount.existsLogin(nickName.getText())) {
                Utils.mostrarError("No existe el nickname. Por favor regístrate");
                pass.clear();
                nickName.clear();
                nickName.requestFocus();

            } else if (ok == true) {
                FXMLLoader miCargador = new FXMLLoader(getClass().getResource("../vista/Main.fxml"));
                Parent root = miCargador.load();
                Stage mainStage = new Stage();
                MainController r = miCargador.getController();
                r.Pie();
                r.main();
                mainStage.setScene(new Scene(root));
                mainStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/imagenes/logo-sin.png")));
                mainStage.setTitle("Principal");
                mainStage.setResizable(true);
                mainStage.initModality(Modality.APPLICATION_MODAL);
                mainStage.show();
                Stage mainStage2 = (Stage) Aceptar.getScene().getWindow();
                mainStage2.close();

            }
        } else if (pass == null || pass.getText().isEmpty()) {
            Utils.mostrarAlerta("Por favor rellena los campos para poder iniciar sesión");
        } else {
            Utils.mostrarError("Contraseña incorrecta");
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

}
