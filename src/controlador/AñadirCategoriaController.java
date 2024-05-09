package controlador;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AñadirCategoriaController {

    @FXML
    private TextArea DescriptionCategoria;
    @FXML
    private TextField NameCategoria;
    @FXML
    private Button cancelar;
    @FXML
    private Button añadirCategoria;

    /**
     * Initializes the controller class.
     */
    public void initialize(URL url, ResourceBundle rb) {
        // Algo
        NameCategoria.requestFocus();
    }

    @FXML
    private void Cancelar(ActionEvent event) {
        NameCategoria.clear();
        DescriptionCategoria.clear();
        Stage a = (Stage) cancelar.getScene().getWindow();
        a.close();
    }

    @FXML
    private void AñadirCategoria(ActionEvent event) {
    }

}
