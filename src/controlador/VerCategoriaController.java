package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;
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
    @FXML
    private Tooltip a;
    @FXML
    private Tooltip m;
    @FXML
    private Tooltip c;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        a.setShowDelay(Duration.ZERO);
        c.setShowDelay(Duration.ZERO);
        m.setShowDelay(Duration.ZERO);
        Modificar.disableProperty().bind(
                Bindings.equal(-1,
                        lista.getSelectionModel().selectedIndexProperty()));
        Eliminar.disableProperty().bind(
                Bindings.equal(-1,
                        lista.getSelectionModel().selectedIndexProperty()));
        Añadir.disableProperty().bind(
                Bindings.equal(-1,
                        lista.getSelectionModel().selectedIndexProperty()).not());
        lista.setCellFactory(c -> new CatListCell());
        try {
            List<Category> cat = Acount.getInstance().getUserCategories();
            lista.getItems().addAll(cat);
        } catch (AcountDAOException ex) {
            Logger.getLogger(VerCategoriaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(VerCategoriaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        lista.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Crear un Timeline para deseleccionar después de 2 segundos
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
                    lista.getSelectionModel().clearSelection();
                }));
                timeline.setCycleCount(1);
                timeline.play();
            }
        });
    }

    @FXML
    private void añadir(ActionEvent event) throws IOException, AcountDAOException {
        Boolean ok = CargaVistas.CATEGORIA();
        if (ok) {
            lista.getItems().clear();
            List<Category> cat = Acount.getInstance().getUserCategories();
            lista.getItems().addAll(cat);
        }
    }

    @FXML
    private void modificar(ActionEvent event) throws IOException, AcountDAOException {
        Category act = lista.getSelectionModel().getSelectedItem();
        CargaVistas.CATEGORIAM(act);
        lista.getItems().clear();
        List<Category> cat = Acount.getInstance().getUserCategories();
        lista.getItems().addAll(cat);
        lista.getSelectionModel().clearSelection();
        lista.refresh();

    }

    @FXML
    private void eliminar(ActionEvent event) throws AcountDAOException, IOException {
        Category act = lista.getSelectionModel().getSelectedItem();
        // Mostrar un diálogo de confirmación
        String css = this.getClass().getResource("/estilos/Alert.css").toExternalForm();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar Categoria");
        alert.setHeaderText("¿Estás seguro de que deseas eliminar la categoria : '" + act.getName() + "' ?");
        alert.setContentText("Esta acción no se puede deshacer.");
        alert.getDialogPane().getStylesheets().add(css);
        alert.getDialogPane().getStyleClass().add("custom-alert");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/imagenes/logo.jpeg"));
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Boolean ok = Acount.getInstance().removeCategory(act);
            if (ok) {
                Utils.mostrarInfo("Categoria eliminada con éxito");
                lista.getItems().clear();
                List<Category> cat = Acount.getInstance().getUserCategories();
                lista.getItems().addAll(cat);
            }
        }
        lista.getSelectionModel().clearSelection();
        lista.refresh();
    }

    class CatListCell extends ListCell<Category> {

        @Override
        protected void updateItem(Category t, boolean bln) {
            super.updateItem(t, bln);
            if (t == null || bln)
                setText(null);
            else
                setText(t.getName() + " : " + t.getDescription());

        }

    }

}
