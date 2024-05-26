package controlador;

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
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
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

    private BooleanProperty validPassword;
    private BooleanProperty validNick;
    private BooleanProperty validRpass;
    private BooleanProperty validEmail;
    private BooleanProperty validName;
    private BooleanProperty validSurname;
    @FXML
    private Tooltip m;
    @FXML
    private Tooltip a;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        validNick = new SimpleBooleanProperty();
        validPassword = new SimpleBooleanProperty();
        validEmail = new SimpleBooleanProperty();
        validName = new SimpleBooleanProperty();
        validSurname = new SimpleBooleanProperty();
        validRpass = new SimpleBooleanProperty();
        validPassword.setValue(Boolean.FALSE);
        validNick.setValue(Boolean.FALSE);
        validEmail.setValue(Boolean.FALSE);
        validName.setValue(Boolean.FALSE);
        validSurname.setValue(Boolean.FALSE);
        validRpass.setValue(Boolean.FALSE);
        String image = "/avatars/default.png";
        avatar.setImage(new Image(image));

        try {
            m.setShowDelay(Duration.ZERO);
            a.setShowDelay(Duration.ZERO);
            acount = Acount.getInstance();

            // Añadir un ChangeListener a los campos TextField
            NickName.focusedProperty()
                    .addListener(
                            (ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                                if (!newValue) { // focus lost.
                                    m.hide();
                                    String t = NickName.getText();
                                    if (!User.checkNickName(t)) {
                                        Utils.error(NickName);
                                    } else if (acount.existsLogin(NickName.getText())) {
                                        Utils.mostrarError("Nickname ya existe, elije otro");
                                        NickName.clear();
                                        NickName.requestFocus();

                                    } else {
                                        Utils.correct(NickName);
                                        validNick.setValue(Boolean.TRUE);
                                    }
                                } else {
                                    Point2D p = NickName.localToScreen(NickName.getLayoutBounds().getMaxX(),
                                            NickName.getLayoutBounds().getMaxY()); // Posición del TextField
                                    m.show(NickName, p.getX(), p.getY());
                                }
                            });
        } catch (AcountDAOException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Inicialmente, el botón Aceptar está deshabilitado
        Pass.textProperty()
                .addListener((ob, olv, newv) -> {
                    if (newv.length() >= 8) {
                        a.hide();
                        if (!User.checkPassword(newv)) {
                            Utils.error(Pass);
                        } else {
                            Utils.correct(Pass);
                            validPassword.setValue(Boolean.TRUE);
                        }
                    } else {
                        Point2D p = Pass.localToScreen(Pass.getLayoutBounds().getMaxX(),
                                Pass.getLayoutBounds().getMaxY()); // Posición del TextField
                        a.show(Pass, p.getX(), p.getY());
                        validPassword.setValue(Boolean.FALSE);
                    }
                });
        Rpass.textProperty()
                .addListener((ob, olv, newv) -> {
                    if (newv.length() >= 8) {
                        if (Pass.getText().compareTo(newv) != 0) {
                            Utils.error(Rpass);
                        } else {
                            Utils.correct(Rpass);
                            validRpass.setValue(Boolean.TRUE);
                        }
                    } else {
                        Utils.error(Rpass);
                        validRpass.setValue(Boolean.FALSE);
                    }
                });
        Name.textProperty()
                .addListener((ob, olv, newv) -> {
                    if (newv.length() > 0) {
                        if (!Utils.checkNames(newv)) {
                            Utils.error(Name);
                        } else {
                            Utils.correct(Name);
                            validName.setValue(Boolean.TRUE);
                        }
                    } else {
                        Utils.error(Name);
                        validName.setValue(Boolean.FALSE);
                    }
                });
        SurName.textProperty()
                .addListener((ob, olv, newv) -> {
                    if (newv.length() > 0) {
                        if (!Utils.checkNames(newv)) {
                            Utils.error(SurName);
                        } else {
                            Utils.correct(SurName);
                            validSurname.setValue(Boolean.TRUE);
                        }
                    } else {
                        Utils.error(SurName);
                        validSurname.setValue(Boolean.FALSE);
                    }
                });
        Email.focusedProperty()
                .addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    if (!newValue) { // focus lost.
                        String t = Email.getText();
                        if (!User.checkEmail(t)) {
                            Utils.error(Email);
                        } else {
                            Utils.correct(Email);
                            validEmail.setValue(Boolean.TRUE);
                        }
                    }
                });
        Registrar.disableProperty().bind(validNick.not().or(validPassword.not()
                .or(validName.not().or(validSurname.not().or(validEmail.not().or(validRpass.not()))))));

        Rpass.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                reg();
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
        reg();
    }

    @FXML
    private void SetImage(ActionEvent event) {
        Window n = ((Node) event.getSource()).getScene().getWindow();
        Avatar = Utils.ElegirImagen(n);
        if (Avatar != null) {
            avatar.setImage(new Image(Avatar));
        }
    }

    private void reg() {
        Image img;
        if (Avatar == null) {
            img = new Image("/avatars/default.png");
        } else {
            img = new Image(Avatar);
        }

        // registrar el nuevo miembro
        LocalDate date = LocalDate.now();
        Boolean ok;
        try {
            ok = acount.registerUser(Name.getText(), SurName.getText(), Email.getText(), NickName.getText(),
                    Pass.getText(), img, date);
            if (ok) {
                Utils.mostrarInfo("Se ha registrado correctamente");
                Stage currentStage = (Stage) Registrar.getScene().getWindow();
                currentStage.close();
                CargaVistas.LOGIN();
            }
        } catch (AcountDAOException | IOException e) {
            e.printStackTrace();
        }

    }
}