package controlador;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
    private Button inicio;

    User logged;
    @FXML
    private Button Inicio;
    @FXML
    private Button Cancelar;
    @FXML
    private Button guardarCambios;

    public void initialize(URL url, ResourceBundle rb) throws AcountDAOException, IOException {
        logged = Acount.getInstance().getLoggedUser();
        Name.setText(logged.getName());
        SurName.setText(logged.getSurname());
        Email.setText(logged.getEmail());
        Pass.setText(logged.getPassword());
        NickName.setText(logged.getNickName());
        avatar.setImage(logged.getImage());
    }

    @FXML
    private void TerminarSesion(ActionEvent event) throws IOException, AcountDAOException {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Diálogo de confirmación");
        alert.setHeaderText("Cabecera");
        alert.setContentText("¿Seguro que quieres cerrar sesión?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Boolean ok = Acount.getInstance().logOutUser();
            if (ok) {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Inicio.fxml"));
                Parent userRoot = loader.load();
                Stage inicioStage = new Stage();
                inicioStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/imagenes/logo-sin.png")));
                inicioStage.setTitle("Expense Tracker");
                inicioStage.setScene(new Scene(userRoot));
                inicioStage.show();
                Stage stage = (Stage) inicio.getScene().getWindow();
                stage.close();
            }
        }
    }

    @FXML
    private void IrInicio(ActionEvent event) throws IOException {
        Stage stage = (Stage) inicio.getScene().getWindow();
        stage.close();
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("../vista/Main.fxml"));
        Parent root = miCargador.load();
        Stage mainStage = new Stage();
        mainStage.setScene(new Scene(root));
        mainStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/imagenes/logo-sin.png")));
        mainStage.setTitle("Principal");
        mainStage.setResizable(false);
        mainStage.initModality(Modality.APPLICATION_MODAL);
        mainStage.show();
        Stage mainStage2 = (Stage) inicio.getScene().getWindow();
        mainStage2.close();
    }

    @FXML
    private void GuardarCambios(ActionEvent event) throws AcountDAOException, IOException {
        if (Utils.validarDatos(Name) && Utils.validarDatos(SurName) && Utils.validarNickname(NickName)
                && Utils.checkEditPass(Pass)
                && Utils.checkEditEmail(Email)) {
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("Completa correctamente los campos de registro");
            alert.showAndWait();
        }
    }

    @FXML
    private void SetImage(ActionEvent event) {
        try {

            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg"));

            // Mostrar el diálogo de selección de archivos
            File file;
            file = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
            if (file != null) {
                // Convertir la ruta del archivo a una URL y cargar la imagen
                Avatar = file.toURI().toString();
                avatar.setImage(new Image(Avatar));
            }
        } catch (Exception e) {
            System.out.println("Se produjo un error al seleccionar la imagen: " + e.getMessage());
        }
    }

    @FXML
    private void CancelarCambios(ActionEvent event) {
    }

}