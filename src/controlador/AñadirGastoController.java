package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.AcountDAOException;
import utils.Utils;

public class AñadirGastoController {

    @FXML
    private Button Inicio;
    @FXML
    private Button Perfil;
    @FXML
    private Button verGasto;
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
    private ChoiceBox<String> CategoriaGasto;
    @FXML
    private Button añadirCategoria;
    @FXML
    private Button cancelar;
    @FXML
    private TextArea DescriptionGasto;
    @FXML
    private Button añadirGasto;

    public void initialize(URL url, ResourceBundle rb) {
        NameGasto.requestFocus();
    }

    @FXML
    void AñadirCategoria(ActionEvent event) {

    }

    @FXML
    void IrInicio(ActionEvent event) {

    }

    @FXML
    private void SetImage(ActionEvent event) {
        String Avatar;
        Window n = ((Node) event.getSource()).getScene().getWindow();
        Avatar = Utils.ElegirImagen(n);
        if (Avatar != null) {
            Factura.setImage(new Image(Avatar));
        }
    }

    @FXML
    void VerGasto(ActionEvent event) {

    }

    @FXML
    void VerPerfil(ActionEvent event) {

    }

    @FXML
    private void Cancelar(ActionEvent event) {
        Stage a = (Stage) cancelar.getScene().getWindow();
        a.close();
    }

    @FXML
    private void TerminarSesion(ActionEvent event) throws AcountDAOException, IOException {
        Boolean ok = Utils.AcabarSesion();
        if (ok) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Inicio.fxml"));
            Parent userRoot = loader.load();
            Stage inicioStage = new Stage();
            inicioStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/imagenes/logo-sin.png")));
            inicioStage.setTitle("Expense Tracker");
            inicioStage.setScene(new Scene(userRoot));
            inicioStage.show();
            Stage stage = (Stage) cancelar.getScene().getWindow();
            stage.close();
        }

    }

    @FXML
    private void AñadirGasto(ActionEvent event) {
    }

}
