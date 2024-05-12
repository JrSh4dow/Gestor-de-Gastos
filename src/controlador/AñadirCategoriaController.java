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
import model.User;
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

    /**
     * Initializes the controller class.
     */
    public void initialize(URL url, ResourceBundle rb) {
        // Algo
        NameCategoria.requestFocus();
    }

    @FXML
    private void Cancelar(ActionEvent event) {
        Stage a = (Stage) cancelar.getScene().getWindow();
        a.close();
    }

    // Añadir la categoria a la base de datos
    @FXML
    private void AñadirCategoria(ActionEvent event) throws AcountDAOException, IOException{
        // Obtener instancia de Acount
        Acount account = Acount.getInstance();
        
        // Verificar si hay un usuario logueado
        User user = account.getLoggedUser();
        if (user != null) {
            // Obtener los valores de la nueva categoría desde la interfaz de usuario
            String nombreCategoria = NameCategoria.getText();
            String descripcionCategoria = DescriptionCategoria.getText();
            
            // Registrar la categoría
            boolean registrado = account.registerCategory(nombreCategoria, descripcionCategoria);
            
            // Verificar si se registró correctamente
            if (registrado) {
                // Actualizar la lista de categorías en la interfaz de usuario o realizar otras acciones necesarias
                
            } else {
                // Mostrar un mensaje de error o realizar otras acciones necesarias
                Utils.mostrarError("No se pudo añadir la categoría.");
            }
        } else {
            // Mostrar un mensaje indicando que no hay usuario logueado
            Utils.mostrarError("Debe iniciar sesión para añadir una categoría.");
        }
    }

}
