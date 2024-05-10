package controlador;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
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

    // When to strings are equal, compareTo returns zero
    private final int EQUALS = 0;
    @FXML
    private ImageView avatar;
    @FXML
    private Button Imagen;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String image = "/avatars/default.png";
        avatar.setImage(new Image(image));
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

    @FXML
    private void registrarClicked(ActionEvent event) throws IOException, AcountDAOException {
        Acount acount = Acount.getInstance();
        int same = Pass.getText().compareTo(Rpass.getText());
        if (Utils.validarDatos(Name.getText()) && Utils.validarDatos(SurName.getText())
                && User.checkNickName(NickName.getText())
                && User.checkPassword(Pass.getText())
                && User.checkEmail(Email.getText()) && same == EQUALS) {
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
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setTitle("Información");
                alert.setContentText("Se ha registrado correctamente");
                alert.showAndWait();
                FXMLLoader miCargador = new FXMLLoader(getClass().getResource("/vista/LogIn.fxml"));
                Parent root = miCargador.load();
                Stage currentStage = (Stage) Registrar.getScene().getWindow();
                currentStage.close();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("INICIO DE SESIÓN");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/imagenes/logo-sin.png")));
                // la ventana se muestra modal
                stage.show();
            }

        } else {
            if (acount.existsLogin(NickName.getText())) {
                Utils.mostrarError("Nickname ya existe, elije otro");
            } else if ((Pass == null && Pass.getText().isEmpty()) || (Rpass == null || Rpass.getText().isEmpty())) {
                Utils.mostrarError("Completa correctamente los campos de registro");
            } else {
                Utils.mostrarError("Introduce una contraseña valida");
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
}