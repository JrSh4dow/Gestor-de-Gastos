package controlador;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.util.Duration;
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
    private BooleanProperty validName;
    private BooleanProperty validDescripcion;
    @FXML
    private Tooltip c;
    @FXML
    private Tooltip m;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        c.setShowDelay(Duration.ZERO);m.setShowDelay(Duration.ZERO);
        NameCategoria.requestFocus();

        validDescripcion = new SimpleBooleanProperty();
        validName = new SimpleBooleanProperty();

        validName.setValue(Boolean.TRUE);
        validDescripcion.setValue(Boolean.TRUE);

        DescriptionCategoria.focusedProperty()
                .addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    if (!newValue) { // focus lost.

                        if (DescriptionCategoria.getText().isEmpty()) {
                            Utils.error(DescriptionCategoria);
                            validDescripcion.setValue(Boolean.FALSE);
                        } else {
                            Utils.correct(DescriptionCategoria);
                            validDescripcion.setValue(Boolean.TRUE);
                        }
                    }
                });
        NameCategoria.focusedProperty()
                .addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    if (!newValue) { // focus lost.

                        if (NameCategoria.getText().isEmpty()) {
                            Utils.error(NameCategoria);
                            validName.setValue(Boolean.FALSE);
                        } else {
                            Utils.correct(NameCategoria);
                            validDescripcion.setValue(Boolean.TRUE);
                        }
                    }
                });
        añadirCategoria.disableProperty().bind(validName.not().or(validDescripcion.not()));
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
        }
        Stage mainStage2 = (Stage) añadirCategoria.getScene().getWindow();
        mainStage2.close();
    }

}
