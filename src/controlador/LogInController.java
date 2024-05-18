package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
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
    private BooleanProperty validPassword;
    private BooleanProperty validNick;

    /**
     * Initializes the controller class.
     */
    Acount acount;
    private boolean passwordVisible = false;
    @FXML
    private TextField passHidden;
    @FXML
    private Button togglePasswordVisibility;
    @FXML
    private ImageView tick;
    @FXML
    private ImageView notick;

    public void initialize(URL url, ResourceBundle rb) {
        validNick = new SimpleBooleanProperty();
        validPassword = new SimpleBooleanProperty();
        validPassword.setValue(Boolean.FALSE);
        validNick.setValue(Boolean.FALSE);
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
                                    } else if (!acount.existsLogin(nickName.getText())) {
                                        Utils.mostrarError("No existe el nickname. Por favor regístrate");
                                        nickName.clear();
                                        nickName.requestFocus();

                                    } else {
                                        Utils.correct(nickName);
                                        validNick.setValue(Boolean.TRUE);
                                    }
                                }
                            });
        } catch (AcountDAOException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Aceptar.disableProperty().bind(validNick.not().or(validPassword.not()));

        pass.focusedProperty()
                .addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    if (!newValue && !pass.getText().isEmpty()) { // focus lost.
                        String t = pass.getText();
                        if (!User.checkPassword(t)) {
                            Utils.error(pass);
                        } else {
                            Utils.correct(pass);
                            validPassword.setValue(Boolean.TRUE);
                        }
                    }
                });

        if (passwordVisible) {
            pass.setText(passHidden.getText());
        } else {
            passHidden.setText(pass.getText());
        }
        
        tick.setVisible(true); // Mostrar tick cuando se muestra el campo de texto
        notick.setVisible(false);

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
            pass.clear();
            pass.requestFocus();
        }
    }

    @FXML
    private void volverClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) botonVolver.getScene().getWindow();
        stage.close();
        CargaVistas.INICIO();
    }

    @FXML
    private void togglePasswordVisibility() {
        // Si la contraseña está oculta, hacerla visible
        if (passwordVisible) {
            pass.setText(passHidden.getText());
            pass.setManaged(true);
            pass.setVisible(true);
            passHidden.setManaged(false);
            passHidden.setVisible(false);
            tick.setVisible(true); // Mostrar tick cuando se muestra el campo de texto
            notick.setVisible(false); // Ocultar notick
            passwordVisible = false;
        } else { // Si la contraseña es visible, ocultarla
            passHidden.setText(pass.getText());
            passHidden.setManaged(true);
            passHidden.setVisible(true);
            pass.setManaged(false);
            pass.setVisible(false);
            tick.setVisible(false); // Ocultar tick
            notick.setVisible(true); // Mostrar notick cuando se muestra el campo de contraseña
            passwordVisible = true;
        }
        synchronizePasswords();
    }

    private void synchronizePasswords() {
        // Sincronizar las contraseñas
        if (passwordVisible) {
            passHidden.setText(pass.getText());
        } else {
            pass.setText(passHidden.getText());
        }
    }

}
