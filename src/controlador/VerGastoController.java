package controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;

/**
 * FXML Controller class
 *
 * @author MEG
 */
public class VerGastoController implements Initializable {

    @FXML
    private Button Inicio;
    @FXML
    private Button Perfil;
    @FXML
    private Button verGasto;
    @FXML
    private TableColumn<?, ?> idGasto;
    @FXML
    private TableColumn<?, ?> nombreGasto;
    @FXML
    private TableColumn<?, ?> descripcionGasto;
    @FXML
    private TableColumn<?, ?> categoriaGasto;
    @FXML
    private TableColumn<?, ?> costeGasto;
    @FXML
    private TableColumn<?, ?> unidadesGasto;
    @FXML
    private TableColumn<?, ?> facturaGasto;
    @FXML
    private Button modificarGasto;
    @FXML
    private Button eliminarGasto;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void TerminarSesion(ActionEvent event) {
    }

    @FXML
    private void IrInicio(ActionEvent event) {
    }

    @FXML
    private void VerPerfil(ActionEvent event) {
    }

    @FXML
    private void VerGasto(ActionEvent event) {
    }

    @FXML
    private void ModificarGasto(ActionEvent event) {
    }

    @FXML
    private void EliminarGasto(ActionEvent event) {
    }
    
}
