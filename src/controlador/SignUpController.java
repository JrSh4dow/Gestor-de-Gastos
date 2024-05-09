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
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Acount;
import model.AcountDAOException;
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
        int same = Utils.checkEquals(Pass, Rpass);
        if (Utils.validarDatos(Name) && Utils.validarDatos(SurName) && Utils.validarNickname(NickName)
                && Utils.checkEditPass(Pass)
                && Utils.checkEditEmail(Email) && same == EQUALS) {
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
            }

        } else {
            if (acount.existsLogin(NickName.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Error");
                alert.setContentText("Nickname ya existe");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Error");
                alert.setContentText("Completa correctamente los campos de registro");
                alert.showAndWait();
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