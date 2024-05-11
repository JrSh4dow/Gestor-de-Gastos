package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import utils.CargaVistas;

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
        Stage currentStage = (Stage) Log.getScene().getWindow();
        currentStage.close();
        CargaVistas.LOGIN();
    }

    @FXML
    private void signup(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) Sign.getScene().getWindow();
        currentStage.close();
        CargaVistas.REGISTRO();
    }

}
