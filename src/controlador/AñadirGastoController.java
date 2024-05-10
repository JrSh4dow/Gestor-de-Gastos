package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Acount;
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
    @FXML
    private BorderPane Caja;

    public void initialize(URL url, ResourceBundle rb) {
        NameGasto.requestFocus();
    }

    @FXML
    void AñadirCategoria(ActionEvent event) throws IOException {
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("../vista/AñadirCategoria.fxml"));
        Parent root = miCargador.load();
        Stage mainStage = new Stage();
        mainStage.setScene(new Scene(root));
        mainStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/imagenes/logo-sin.png")));
        mainStage.setTitle("Inicio");
        mainStage.setResizable(false);
        mainStage.initModality(Modality.APPLICATION_MODAL);
        mainStage.showAndWait();
    }

    @FXML
    void IrInicio(ActionEvent event) throws IOException {
        Stage mainStage2 = (Stage) Inicio.getScene().getWindow();
        mainStage2.close();
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("../vista/Main.fxml"));
        Parent root = miCargador.load();
        Stage mainStage = new Stage();
        mainStage.setScene(new Scene(root));
        mainStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/imagenes/logo-sin.png")));
        mainStage.setTitle("Inicio");
        mainStage.setResizable(false);
        mainStage.initModality(Modality.APPLICATION_MODAL);
        mainStage.show();

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
    void VerGasto(ActionEvent event) throws IOException {
        Stage mainStage2 = (Stage) verGasto.getScene().getWindow();
        mainStage2.close();
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("../vista/VerGastos.fxml"));
        Parent root = miCargador.load();
        Stage mainStage = new Stage();
        mainStage.setScene(new Scene(root));
        mainStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/imagenes/logo-sin.png")));
        mainStage.setTitle("GASTOS");
        mainStage.setResizable(false);
        mainStage.initModality(Modality.APPLICATION_MODAL);
        mainStage.show();
    }

    @FXML
    void VerPerfil(ActionEvent event) throws IOException {
        Stage mainStage2 = (Stage) Perfil.getScene().getWindow();
        mainStage2.close();
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("../vista/VerPerfil.fxml"));
        Parent root = miCargador.load();
        Stage mainStage = new Stage();
        mainStage.setScene(new Scene(root));
        mainStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/imagenes/logo-sin.png")));
        mainStage.setTitle("PERFIL");
        mainStage.setResizable(false);
        mainStage.initModality(Modality.APPLICATION_MODAL);
        mainStage.show();
    }

    @FXML
    private void Cancelar(ActionEvent event) {
        NameGasto.clear();
        CosteGasto.clear();
        UnidadeGasto.clear();
        DescriptionGasto.clear();
        CategoriaGasto.setSelectionModel(null);
        Factura.setImage(null);
        FechaGasto.setValue(null);

    }

    @FXML
    private void TerminarSesion(ActionEvent event) throws AcountDAOException, IOException {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Diálogo de confirmación");
        alert.setHeaderText("Cabecera");
        alert.setContentText("¿Seguro que quieres cerrar sesión?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Boolean ok = Acount.getInstance().logOutUser();
            if (ok) {
                Node sourceNode = Caja.getScene().getRoot();
                Scene scene = sourceNode.getScene();
                Stage stage = (Stage) scene.getWindow();
                stage.close();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Inicio.fxml"));
                Parent userRoot = loader.load();
                Stage inicioStage = new Stage();
                inicioStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/imagenes/logo-sin.png")));
                inicioStage.setTitle("Expense Tracker");
                inicioStage.setScene(new Scene(userRoot));
                inicioStage.show();
            }
        }
    }

    @FXML
    private void AñadirGasto(ActionEvent event) throws IOException {
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("../vista/AñadirGasto.fxml"));
        Parent root = miCargador.load();
        Stage mainStage = new Stage();
        mainStage.setScene(new Scene(root));
        mainStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/imagenes/logo-sin.png")));
        mainStage.setTitle("AÑADIR GASTO");
        mainStage.setResizable(false);
        mainStage.initModality(Modality.APPLICATION_MODAL);
        mainStage.show();
        Stage mainStage2 = (Stage) añadirGasto.getScene().getWindow();
        mainStage2.close();
    }

}
