package controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class AñadirGastoController {

    @FXML
    private ChoiceBox<?> CategoriaGasto;
    @FXML
    private TextField CosteGasto;
    @FXML
    private TextField DescriptionGasto;
    @FXML
    private ImageView Factura;
    @FXML
    private DatePicker FechaGasto;
    @FXML
    private Button Imagen;
    @FXML
    private Button Inicio;
    @FXML
    private TextField NameGasto;
    @FXML
    private Button Perfil;
    @FXML
    private TextField UnidadeGasto;
    @FXML
    private Button añadirCategoria;
    @FXML
    private Button verGasto;
    @FXML
    private Button añadir;

    public void initialize(URL url, ResourceBundle rb) {

    }

    // Esto es para añadir el gasto a la base de datos.
    @FXML
    void Anadir(ActionEvent event) {

    }

    void AñadirCategoria(ActionEvent event) {

    }

    @FXML
    void IrInicio(ActionEvent event) {

    }

    @FXML
    void SetImage(ActionEvent event) {

    }

    @FXML
    void TerminarSesion(ActionEvent event) {

    }

    @FXML
    void VerGasto(ActionEvent event) {

    }

    @FXML
    void VerPerfil(ActionEvent event) {

    }

}
