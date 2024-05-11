package controlador;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Acount;
import model.AcountDAOException;
import model.User;
import utils.*;

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
    private String Avatar;
    @FXML
    private Button botonVolver;
    @FXML
    private TextField Rpass;
    @FXML
    private TextField Pass;
    @FXML
    private Button Registrar;
    @FXML
    private ImageView avatar;
    @FXML
    private Button Imagen;
    Acount acount;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String image = "/avatars/default.png";
        avatar.setImage(new Image(image));
        Acount acount;

        try {
            acount = Acount.getInstance();

            // Añadir un ChangeListener a los campos TextField
            NickName.focusedProperty()
                    .addListener(
                            (ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                                if (!newValue) { // focus lost.
                                    String t = NickName.getText();
                                    if (!User.checkNickName(t)) {
                                        Utils.error(NickName);
                                        NickName.requestFocus();
                                    } else if (acount.existsLogin(NickName.getText())) {
                                        Utils.mostrarError("Nickname ya existe, elije otro");
                                        NickName.clear();
                                        NickName.requestFocus();

                                    } else {
                                        Utils.correct(NickName);
                                    }
                                }
                            });
        } catch (AcountDAOException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Inicialmente, el botón Aceptar está deshabilitado
        Registrar.setDisable(true);
        Pass.focusedProperty()
                .addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    if (!newValue && !Pass.getText().isEmpty()) { // focus lost.
                        String t = Pass.getText();
                        if (!User.checkPassword(t)) {
                            Utils.error(Pass);
                            Utils.mostrarError("Introduce una contraseña valida");
                            Pass.requestFocus();
                        } else {
                            Utils.correct(Pass);
                        }
                    }
                });
        Rpass.focusedProperty()
                .addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    if (!newValue && !Rpass.getText().isEmpty()) { // focus lost.
                        String t = Rpass.getText();
                        if (Pass.getText().compareTo(t) != 0) {
                            Utils.error(Rpass);
                            Rpass.requestFocus();
                        } else {
                            Utils.correct(Rpass);
                            Registrar.setDisable(false);
                        }
                    }
                });
        Name.focusedProperty()
                .addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    if (!newValue) { // focus lost.
                        String t = Name.getText();
                        if (!Utils.checkNames(t)) {
                            Utils.error(Name);
                            Name.requestFocus();
                        } else {
                            Utils.correct(Name);
                        }
                    }
                });
        SurName.focusedProperty()
                .addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    if (!newValue) { // focus lost.
                        String t = SurName.getText();
                        if (!Utils.checkNames(t)) {
                            Utils.error(SurName);
                            SurName.requestFocus();
                        } else {
                            Utils.correct(SurName);
                        }
                    }
                });
        Email.focusedProperty()
                .addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    if (!newValue) { // focus lost.
                        String t = Email.getText();
                        if (!User.checkEmail(t)) {
                            Utils.error(Email);
                            Email.requestFocus();
                        } else {
                            Utils.correct(Email);
                        }
                    }
                });

    }

    @FXML
    private void volverClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) botonVolver.getScene().getWindow();
        stage.close();
        CargaVistas.INICIO();
    }

    @FXML
    private void registrarClicked(ActionEvent event) throws IOException, AcountDAOException {

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
            Utils.mostrarInfo("Se ha registrado correctamente");
            Stage currentStage = (Stage) Registrar.getScene().getWindow();
            currentStage.close();
            CargaVistas.LOGIN();
        }
    }

    @FXML
    private void SetImage(ActionEvent event) {
        Window n = ((Node) event.getSource()).getScene().getWindow();
        Avatar = Utils.ElegirImagen(n);
        if (Avatar != null) {
            avatar.setImage(new Image(Avatar));
        }
    }
}