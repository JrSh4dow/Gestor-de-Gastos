package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Acount;
import model.AcountDAOException;
import model.User;
import utils.*;

public class PerfilController {

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Inicio.fxml"));
            Parent userRoot = loader.load();
            Stage inicioStage = new Stage();
            inicioStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/imagenes/logo-sin.png")));
            inicioStage.setTitle("Expense Tracker");
            inicioStage.setScene(new Scene(userRoot));
            inicioStage.show();
        }

    }

    @FXML
    private void IrInicio(ActionEvent event) throws IOException {
        Stage mainStage2 = (Stage) Inicio.getScene().getWindow();
        mainStage2.close();
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("../vista/Main.fxml"));
        Parent root = miCargador.load();
        Stage mainStage = new Stage();
        mainStage.setScene(new Scene(root));
        mainStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/imagenes/logo-sin.png")));
        mainStage.setTitle("INICIO");
        mainStage.setResizable(false);
        mainStage.initModality(Modality.APPLICATION_MODAL);
        mainStage.show();
    }

    @FXML
    private void GuardarCambios(ActionEvent event) throws AcountDAOException, IOException {
        if (Utils.validarDatos(Name.getText()) && Utils.validarDatos(SurName.getText())
                && User.checkPassword(Pass.getText())
                && User.checkEmail(Email.getText())) {
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

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Información");
            alert.setContentText("Se han gurdado los cambios correctamente");
            alert.showAndWait();

        } else {
            if (!User.checkPassword(Pass.getText()) && !(Pass == null || Pass.getText().isEmpty())) {
                Utils.mostrarError("Introduce una contraseña valida");
            } else {
                Utils.mostrarError("Completa correctamente los campos");
            }
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

    @FXML
    private void CancelarCambios(ActionEvent event) {
        establecer();
    }

}