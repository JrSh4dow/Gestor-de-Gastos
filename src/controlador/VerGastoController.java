package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.AcountDAOException;
import utils.Utils;

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
    @FXML
    private Button añadirGasto;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void TerminarSesion(ActionEvent event) throws IOException, AcountDAOException {
        Boolean ok = Utils.AcabarSesion();
        if (ok) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Inicio.fxml"));
            Parent userRoot = loader.load();
            Stage inicioStage = new Stage();
            inicioStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/imagenes/logo-sin.png")));
            inicioStage.setTitle("Expense Tracker");
            inicioStage.setScene(new Scene(userRoot));
            inicioStage.show();
            Stage stage = (Stage) Inicio.getScene().getWindow();
            stage.close();
        }

    }

    @FXML
    private void IrInicio(ActionEvent event) {
    }

    @FXML
    private void VerPerfil(ActionEvent event) {
    }

    @FXML
    private void ModificarGasto(ActionEvent event) {
    }

    @FXML
    private void EliminarGasto(ActionEvent event) {
    }

    @FXML
    private void AñadirGasto(ActionEvent event) {
    }

}
