package controlador;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.AcountDAOException;
import model.Category;
import model.Charge;
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
    private TableView<Charge> Gastos;
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
    @FXML
    private BorderPane Caja;
    @FXML
    private Button imprimirGastos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Iniciar La TableView con los gastos desde la base de datos
    }

    // Para eliminar un gasto de la base de datos
    @FXML
    private void EliminarGasto(ActionEvent event) {
    }

    @FXML
    private void TerminarSesion(ActionEvent event) throws IOException, AcountDAOException {
        Boolean ok = Utils.AcabarSesion();
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

    @FXML
    private void IrInicio(ActionEvent event) throws IOException, AcountDAOException {
        Stage mainStage2 = (Stage) Inicio.getScene().getWindow();
        mainStage2.close();
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("../vista/Main.fxml"));
        Parent root = miCargador.load();
        Stage mainStage = new Stage();
        MainController r = miCargador.getController();
        r.Pie();
        r.main();
        mainStage.setScene(new Scene(root));
        mainStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/imagenes/logo-sin.png")));
        mainStage.setTitle("INICIO");
        mainStage.setResizable(false);
        mainStage.initModality(Modality.APPLICATION_MODAL);
        mainStage.show();
    }

    @FXML
    private void VerPerfil(ActionEvent event) throws IOException {
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
    private void ModificarGasto(ActionEvent event) throws IOException {
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("../vista/ModificarGastos.fxml"));
        Parent root = miCargador.load();
        Stage mainStage = new Stage();
        mainStage.setScene(new Scene(root));
        mainStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/imagenes/logo-sin.png")));
        mainStage.setTitle("MODIFICAR GASTO");
        mainStage.setResizable(false);
        mainStage.initModality(Modality.APPLICATION_MODAL);
        mainStage.showAndWait();
    }

    @FXML
    private void AñadirGasto(ActionEvent event) throws IOException {
        Stage mainStage2 = (Stage) añadirGasto.getScene().getWindow();
        mainStage2.close();
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("../vista/AñadirGasto.fxml"));
        Parent root = miCargador.load();
        Stage mainStage = new Stage();
        mainStage.setScene(new Scene(root));
        mainStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/imagenes/logo-sin.png")));
        mainStage.setTitle("AÑADIR GASTO");
        mainStage.setResizable(false);
        mainStage.initModality(Modality.APPLICATION_MODAL);
        mainStage.show();
    }

    // para imprimir los gastos como un documento pdf
    @FXML
    void ImprimirGastos(ActionEvent event) {
        PrinterJob printerJob = PrinterJob.createPrinterJob();

        if (printerJob != null && printerJob.showPrintDialog(imprimirGastos.getScene().getWindow())) {
            Node imprimir = contenidoPDF();
            if (printerJob.printPage(imprimir))
                printerJob.endJob();
        }
    }

    @SuppressWarnings("unchecked")
    private Node contenidoPDF() {
        // Crear una tabla temporal para copiar el contenido de la TableView
        TableView<Charge> tablaTemporal = new TableView<>();
        TableColumn<Charge, Category> columnaNombre = new TableColumn<>("Nombre");
        TableColumn<Charge, String> columnaDescripcion = new TableColumn<>("Descripción");
        TableColumn<Charge, Category> columnaCategoria = new TableColumn<>("Categoría");
        TableColumn<Charge, LocalDate> columnaFecha = new TableColumn<>("Fecha");
        TableColumn<Charge, Double> columnaCoste = new TableColumn<>("Coste");
        TableColumn<Charge, Integer> columnaUnidades = new TableColumn<>("Unidades");

        // Asignar las propiedades de las entidades a las columnas
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        columnaCoste.setCellValueFactory(new PropertyValueFactory<>("Coste"));
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("concepto"));
        columnaUnidades.setCellValueFactory(new PropertyValueFactory<>("unidades"));

        // Agregar las columnas a la tabla
        tablaTemporal.getColumns().addAll(columnaNombre, columnaDescripcion, columnaCategoria, columnaFecha,
                columnaCoste, columnaUnidades);

        // Copiar los datos de la TableView original a la tabla temporal
        tablaTemporal.setItems(Gastos.getItems());

        return tablaTemporal;
    }
}
