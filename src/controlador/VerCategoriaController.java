package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import model.Acount;
import model.AcountDAOException;
import model.Category;
import utils.CargaVistas;
import utils.Utils;

/**
 * FXML Controller class
 *
 * @author MEG
 */
public class VerCategoriaController implements Initializable {

    @FXML
    private ListView<Category> lista;
    @FXML
    private Button Añadir;
    @FXML
    private Button Modificar;
    @FXML
    private Button Eliminar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Eliminar.setDisable(true); Modificar.setDisable(true);Añadir.setDisable(false);
        lista.setCellFactory(c-> new CatListCell());
        lista.focusedProperty().addListener((a, b, c) -> {
            if (lista.isFocused()) {
                Añadir.setDisable(true);
            }
        });
        try {
            List<Category> cat=Acount.getInstance().getUserCategories();
            lista.getItems().addAll(cat);
        } catch (AcountDAOException ex) {
            Logger.getLogger(VerCategoriaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(VerCategoriaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        lista.getSelectionModel().selectedIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            if (newIndex.intValue() != -1) {
                // Habilitar botones cuando se selecciona una fila
                Eliminar.setDisable(false); Modificar.setDisable(false);Añadir.setDisable(true);
            } else {
                // Deshabilitar botones cuando no hay ninguna fila seleccionada
                Eliminar.setDisable(true); Modificar.setDisable(true);Añadir.setDisable(false);
            }
        });
    }    

    @FXML
    private void añadir(ActionEvent event) throws IOException, AcountDAOException {
        Boolean ok = CargaVistas.CATEGORIA();
        if (ok) {
            lista.getItems().clear();
            List<Category> cat=Acount.getInstance().getUserCategories();
            lista.getItems().addAll(cat);
        }
    }

    @FXML
    private void modificar(ActionEvent event) {
        Category act = lista.getSelectionModel().getSelectedItem();
        FXMLLoader miCargador = new FXMLLoader(CargaVistas.class.getResource("../vista/AñadirCategoria.fxml"));
        AñadirCategoriaController controller = miCargador.getController(); controller.init(act);
        lista.refresh();
    }

    @FXML
    private void eliminar(ActionEvent event) throws AcountDAOException, IOException {
        Category act = lista.getSelectionModel().getSelectedItem();
        Boolean ok = Acount.getInstance().removeCategory(act);
        if (ok) {
            Utils.mostrarInfo("Categoria eliminada con éxito");
        }
        lista.refresh();
    }
    class CatListCell extends ListCell<Category> {

    @Override
    protected void updateItem(Category t, boolean bln) {
        super.updateItem(t, bln); 
        if (t==null || bln) setText(null); 
        else setText(t.getName() + " : " + t.getDescription());

    }
    
}
    
}
