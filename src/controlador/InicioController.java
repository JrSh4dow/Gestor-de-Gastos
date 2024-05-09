package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author MEG
 */
public class InicioController implements Initializable {

    @FXML
    private Text titulo;
    @FXML
    private Text lema;
    @FXML
    private Button Log;
    @FXML
    private Button Sign;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Algo
    }

    @FXML
    private void login(ActionEvent event) throws IOException {
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("/vista/LogIn.fxml"));
        Parent root = miCargador.load();
        Stage currentStage = (Stage) Log.getScene().getWindow();
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

    @FXML
    private void signup(ActionEvent event) throws IOException {
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("/vista/SignUp.fxml"));
        Parent root = miCargador.load();
        Stage currentStage = (Stage) Sign.getScene().getWindow();
        currentStage.close();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/imagenes/logo-sin.png")));
        stage.setTitle("REGISTRAR-SE");
        // la ventana se muestra modal
        stage.show();
    }

}
