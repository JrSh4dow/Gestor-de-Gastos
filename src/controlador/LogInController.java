package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Acount;
import model.AcountDAOException;
import model.User;
import utils.CargaVistas;
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
    Acount acount;

    public void initialize(URL url, ResourceBundle rb) {
        try {
            acount = Acount.getInstance();

            // Añadir un ChangeListener a los campos TextField
            nickName.focusedProperty()
                    .addListener(
                            (ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                                if (!newValue) { // focus lost.
                                    String t = nickName.getText();
                                    if (!User.checkNickName(t)) {
                                        Utils.error(nickName);
                                        nickName.requestFocus();
                                    } else if (!acount.existsLogin(nickName.getText())) {
                                        Utils.mostrarError("No existe el nickname. Por favor regístrate");
                                        nickName.clear();
                                        nickName.requestFocus();

                                    } else {
                                        Utils.correct(nickName);
                                    }
                                }
                            });
        } catch (AcountDAOException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Inicialmente, el botón Aceptar está deshabilitado
        Aceptar.setDisable(true);
        pass.focusedProperty()
                .addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    if (!newValue && !pass.getText().isEmpty()) { // focus lost.
                        String t = pass.getText();
                        if (!User.checkPassword(t)) {
                            Utils.error(pass);
                        } else {
                            Utils.correct(pass);
                            Aceptar.setDisable(false);
                        }
                    }
                });

    }

    @FXML
    private void Acceptar(ActionEvent event) throws AcountDAOException, IOException {

        Boolean ok = acount.logInUserByCredentials(nickName.getText(), pass.getText());
        if (ok == true) {
            Stage mainStage2 = (Stage) Aceptar.getScene().getWindow();
            mainStage2.close();
            CargaVistas.MAIN();

        } else {
            Utils.mostrarError("Contraseña incorrecta");
        }
    }

    @FXML
    private void volverClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) botonVolver.getScene().getWindow();
        stage.close();
        CargaVistas.INICIO();
    }

}
