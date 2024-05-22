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
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import model.Acount;
import model.AcountDAOException;
import model.User;
import utils.*;

public class PerfilController implements Initializable {

    @FXML
    private TextField Name;
    @FXML
    private TextField SurName;
    @FXML
    private TextField Email;
    @FXML
    private TextField NickName;
    @FXML
    private TextField Pass;
    @FXML
    private ImageView avatar;
    private String Avatar;
    @FXML
    private Button Imagen;
    @FXML
    private BorderPane Caja;
    User logged;
    @FXML
    private Button Inicio;
    @FXML
    private Button Cancelar;
    @FXML
    private Button guardarCambios;
    @FXML
    Text data;
    private BooleanProperty validPassword;
    private BooleanProperty validEmail;
    private BooleanProperty validName;
    private BooleanProperty validSurname;
    @FXML
    private Tooltip c;
    @FXML
    private Tooltip a;

    public void initialize(URL url, ResourceBundle rb) {
        c.setShowDelay(Duration.ZERO);a.setShowDelay(Duration.ZERO);
        validPassword = new SimpleBooleanProperty(true);
        validEmail = new SimpleBooleanProperty(true);
        validName = new SimpleBooleanProperty(true);
        validSurname = new SimpleBooleanProperty(true);

        establecer();

        Pass.focusedProperty()
                .addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    if (!newValue && !Pass.getText().isEmpty()) { // focus lost.
                        String t = Pass.getText();
                        if (!User.checkPassword(t)) {
                            Utils.error(Pass);
                            validPassword.setValue(false);
                        } else {
                            Utils.correct(Pass);
                            validPassword.setValue(true);
                        }
                    }
                });

        Name.focusedProperty()
                .addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    if (!newValue) { // focus lost.
                        String t = Name.getText();
                        if (!Utils.checkNames(t)) {
                            Utils.error(Name);
                            validName.setValue(false);
                        } else {
                            Utils.correct(Name);
                            validName.setValue(true);
                        }
                    }
                });
        SurName.focusedProperty()
                .addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    if (!newValue) { // focus lost.
                        String t = SurName.getText();
                        if (!Utils.checkNames(t)) {
                            Utils.error(SurName);
                            validSurname.setValue(false);
                        } else {
                            Utils.correct(SurName);
                            validSurname.setValue(true);
                        }
                    }
                });
        Email.focusedProperty()
                .addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    if (!newValue) { // focus lost.
                        String t = Email.getText();
                        if (!User.checkEmail(t)) {
                            Utils.error(Email);
                            validEmail.setValue(false);
                        } else {
                            Utils.correct(Email);
                            validEmail.setValue(true);
                        }
                    }
                });
        guardarCambios.disableProperty().bind((validPassword.not()
                .or(validName.not().or(validSurname.not().or(validEmail.not())))));
    }

    public void establecer() {
        try {
            logged = Acount.getInstance().getLoggedUser();
        } catch (AcountDAOException | IOException e) {
            e.printStackTrace();
        }
        Name.setText(logged.getName());
        SurName.setText(logged.getSurname());
        Email.setText(logged.getEmail());
        Pass.setText(logged.getPassword());
        NickName.setText(logged.getNickName());
        avatar.setImage(logged.getImage());
        data.setText(logged.getRegisterDate().toString());
    }

    @FXML
    private void TerminarSesion(ActionEvent event) throws AcountDAOException, IOException {
        Boolean ok = Utils.AcabarSesion();
        if (ok) {
            Node sourceNode = Caja.getScene().getRoot();
            Scene scene = sourceNode.getScene();
            Stage stage = (Stage) scene.getWindow();
            stage.close();
            CargaVistas.INICIO();
        }

    }

    @FXML
    private void IrInicio(ActionEvent event) throws IOException, AcountDAOException {
        Stage mainStage2 = (Stage) Inicio.getScene().getWindow();
        mainStage2.close();
        CargaVistas.MAIN();
    }

    @FXML
    private void GuardarCambios(ActionEvent event) throws AcountDAOException, IOException {

        logged.setName(Name.getText());
        logged.setSurname(SurName.getText());
        logged.setImage(avatar.getImage());
        logged.setEmail(Email.getText());
        logged.setPassword(Pass.getText());

        Utils.mostrarInfo("Se han guardado los cambios correctamente");
    }

    @FXML
    private void SetImage(ActionEvent event) {
        Window n = ((Node) event.getSource()).getScene().getWindow();
        Avatar = Utils.ElegirImagen(n);
        if (Avatar != null) {
            avatar.setImage(new Image(Avatar));
        }
    }

    @FXML
    private void CancelarCambios(ActionEvent event) {
        establecer();
    }

}