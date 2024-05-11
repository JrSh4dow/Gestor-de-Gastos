package controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * FXML Controller class
 *
 * @author MEG
 */
public class ModificarGastoController implements Initializable {

    @FXML
    private TextField NameGasto;
    @FXML
    private TextField CosteGasto;
    @FXML
    private TextField UnidadeGasto;
    @FXML
    private ImageView Factura;
    @FXML
    private Button Imagen;
    @FXML
    private DatePicker FechaGasto;
    @FXML
    private ChoiceBox<?> CategoriaGasto;
    @FXML
    private Button CancelarCambios;
    @FXML
    private Button GuardarCambios;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Aqui hay que inicializar con los campos del gasto seleccionado
    }

    @FXML
    private void Cancelar(ActionEvent event) {
        Stage a = (Stage) CancelarCambios.getScene().getWindow();
        a.close();
    }

    @FXML
    private void SetImage(ActionEvent event) {
        Window n = ((Node) event.getSource()).getScene().getWindow();
        String facturaPath = utils.Utils.ElegirImagen(n);
        if (facturaPath != null) {
            Factura.setImage(new Image(facturaPath));
        }
    }

    // Guardar las modificaciones del gasto
    @FXML
    private void Modificar(ActionEvent event) {
    }

}
