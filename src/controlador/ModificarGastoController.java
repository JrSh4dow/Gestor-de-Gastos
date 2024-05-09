package controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author MEG
 */
public class ModificarGastoController implements Initializable {

    @FXML
    private TextField NameCategoria;
    @FXML
    private Button añadir;
    @FXML
    private TextArea DescriptionCategoria;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void Añadir(ActionEvent event) {
    }


    
}
