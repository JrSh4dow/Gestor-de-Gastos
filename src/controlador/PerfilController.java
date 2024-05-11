package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
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

    public void initialize(URL url, ResourceBundle rb) {
        establecer();
        // Inicialmente, el botón Aceptar está deshabilitado
        guardarCambios.setDisable(true);
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
                            guardarCambios.setDisable(false);
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

        Image img;
        if (Avatar == null) {
            img = new Image("/avatars/default.png");
        } else {
            img = logged.getImage();
        }

        logged.setName(Name.getText());
        logged.setSurname(SurName.getText());
        logged.setImage(img);
        logged.setEmail(Email.getText());
        logged.setPassword(Pass.getText());

        Utils.mostrarInfo("Se han gurdado los cambios correctamente");
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