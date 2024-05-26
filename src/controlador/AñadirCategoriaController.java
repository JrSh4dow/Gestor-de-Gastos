package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Acount;
import model.AcountDAOException;
import utils.Utils;

public class AñadirCategoriaController implements Initializable {

    @FXML
    private TextArea DescriptionCategoria;
    @FXML
    private TextField NameCategoria;
    @FXML
    private Button cancelar;
    @FXML
    private Button añadirCategoria;
    private Boolean ok;
    private BooleanProperty validName;
    private BooleanProperty validDescripcion;
    @FXML
    private Tooltip c;
    @FXML
    private Tooltip a;

    /**
     * Initializes the controller class.
     */
    public void initialize(URL url, ResourceBundle rb) {
        c.setShowDelay(Duration.ZERO);
        a.setShowDelay(Duration.ZERO);

        validDescripcion = new SimpleBooleanProperty(false);
        validName = new SimpleBooleanProperty(false);

        // Listener para DescriptionCategoria
        DescriptionCategoria.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() == 0) {
                Utils.error(DescriptionCategoria);
                validDescripcion.setValue(Boolean.FALSE);
            } else {
                Utils.correct(DescriptionCategoria);
                validDescripcion.setValue(Boolean.TRUE);
            }
        });

        // Listener para NameCategoria
        NameCategoria.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() == 0) {
                Utils.error(NameCategoria);
                validName.setValue(Boolean.FALSE);
            } else {
                Utils.correct(NameCategoria);
                validName.setValue(Boolean.TRUE);
            }
        });
        añadirCategoria.disableProperty().bind(validName.not().or(validDescripcion.not()));

        // Algo
        ok = false;
        DescriptionCategoria.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                reg();
            }
        });
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
    private void AñadirCategoria(ActionEvent event) {
        reg();
    }

    private void reg() {
        // Obtener instancia de Acount
        Acount account;
        try {
            account = Acount.getInstance();
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
            }
        } catch (AcountDAOException e) {
            Utils.mostrarAlerta("No se ha podido añadir la categoria porque ya existe");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
