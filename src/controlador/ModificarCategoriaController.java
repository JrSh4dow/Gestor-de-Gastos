package controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Category;
import utils.Utils;

/**
 * FXML Controller class
 *
 * @author MEG
 */
public class ModificarCategoriaController implements Initializable {

    @FXML
    private TextField NameCategoria;
    @FXML
    private Button cancelar;
    @FXML
    private Button añadirCategoria;
    @FXML
    private TextArea DescriptionCategoria;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    private Category act;

    public void init(Category cat) {
        act = cat;
        NameCategoria.setText(cat.getName());
        DescriptionCategoria.setText(cat.getDescription());
    }

    @FXML
    private void Cancelar(ActionEvent event) {
        Stage s = (Stage) cancelar.getScene().getWindow();
        s.close();
    }

    @FXML
    private void AñadirCategoria(ActionEvent event) {
        // Obtener los valores de la nueva categoría desde la interfaz de usuario
        String nombreCategoria = NameCategoria.getText();
        String descripcionCategoria = DescriptionCategoria.getText();
        if (!nombreCategoria.isEmpty() && !descripcionCategoria.isEmpty()) {
            act.setName(nombreCategoria);
            act.setDescription(descripcionCategoria);
            Utils.mostrarInfo("Categoria modificada con éxito");
        } else {
            Utils.mostrarAlerta("Por favor rellena los campos obligatorios");
        }
        Stage mainStage2 = (Stage) añadirCategoria.getScene().getWindow();
        mainStage2.close();
    }

}
