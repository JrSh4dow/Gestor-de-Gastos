package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Acount;
import model.AcountDAOException;
import model.Category;
import utils.Utils;

public class AñadirCategoriaController {

    @FXML
    private TextArea DescriptionCategoria;
    @FXML
    private TextField NameCategoria;
    @FXML
    private Button cancelar;
    @FXML
    private Button añadirCategoria;
    private Boolean ok;
    @SuppressWarnings("unused")
    private Category act;

    /**
     * Initializes the controller class.
     */
    public void initialize(URL url, ResourceBundle rb) {
        // Algo
        NameCategoria.requestFocus();
        ok = false;
    }

    public void init(Category cat) {
        act = cat;
        NameCategoria.setText(cat.getName());
        DescriptionCategoria.setText(cat.getDescription());
    }

    public Boolean getOk() {
        return ok;
    }

    @FXML
    private void Cancelar(ActionEvent event) {
        Stage a = (Stage) cancelar.getScene().getWindow();
        a.close();
    }

    // Añadir la categoria a la base de datos
    @FXML
    private void AñadirCategoria(ActionEvent event) throws AcountDAOException, IOException {
        // Obtener instancia de Acount
        Acount account = Acount.getInstance();

        // Obtener los valores de la nueva categoría desde la interfaz de usuario
        String nombreCategoria = NameCategoria.getText();
        String descripcionCategoria = DescriptionCategoria.getText();
        if (!nombreCategoria.isEmpty() && !descripcionCategoria.isEmpty()) {
            // Registrar la categoría
            boolean registrado = account.registerCategory(nombreCategoria, descripcionCategoria);

            // Verificar si se registró correctamente
            if (registrado) {
                Utils.mostrarInfo("Se ha añadido la categoria");
                Stage mainStage2 = (Stage) añadirCategoria.getScene().getWindow();
                mainStage2.close();
                ok = true;

            } else {
                Utils.mostrarAlerta("No se ha podido añadir la categoria porque ya existe");
            }
        } else {
            Utils.mostrarAlerta("Por favor rellena los campos obligatorios");
        }

    }

}
