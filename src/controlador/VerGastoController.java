package controlador;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Acount;
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
    public void initialize(URL url, ResourceBundle rb)  {
        // Iniciar La TableView con los gastos desde la base de datos
        idGasto.setCellValueFactory(new PropertyValueFactory<>("id"));
        nombreGasto.setCellValueFactory(new PropertyValueFactory<>("name"));
        descripcionGasto.setCellValueFactory(new PropertyValueFactory<>("description"));
        categoriaGasto.setCellValueFactory(new PropertyValueFactory<>("category"));
        costeGasto.setCellValueFactory(new PropertyValueFactory<>("cost"));
        unidadesGasto.setCellValueFactory(new PropertyValueFactory<>("units"));
        facturaGasto.setCellValueFactory(new PropertyValueFactory<>("scanImage"));

        // Obtener los datos de la base de datos
        Acount account;
        try {
            account = Acount.getInstance();
            List<Charge> gastos = account.getUserCharges();
            // Agregar los datos a la TableView
            Gastos.getItems().addAll(gastos);
        } catch (AcountDAOException ex) {
            Logger.getLogger(VerGastoController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(VerGastoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        

        
    }

    // Para eliminar un gasto de la base de datos
    @FXML
    private void EliminarGasto(ActionEvent event) throws AcountDAOException, IOException {
        // Obtener el gasto seleccionado en la TableView
        Charge gastoSeleccionado = Gastos.getSelectionModel().getSelectedItem();
        
        // Verificar si se seleccionó un gasto
        if (gastoSeleccionado != null) {
            // Mostrar un diálogo de confirmación
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Eliminar Gasto");
            alert.setHeaderText("¿Estás seguro de que deseas eliminar este gasto?");
            alert.setContentText("Esta acción no se puede deshacer.");

            // Obtener la respuesta del usuario
            Optional<ButtonType> result = alert.showAndWait();

            // Si el usuario confirma la eliminación
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Eliminar el gasto de la base de datos
                boolean eliminado = Acount.getInstance().removeCharge(gastoSeleccionado);

                // Si se eliminó correctamente
                if (eliminado) {
                    // Obtener la lista de gastos actualizada
                    ObservableList<Charge> listaGastos = Gastos.getItems();
                    
                    // Eliminar el gasto de la TableView
                    listaGastos.remove(gastoSeleccionado);
                    
                    // Mostrar un mensaje de éxito
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Éxito");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("El gasto se ha eliminado correctamente.");
                    successAlert.showAndWait();
                } else {
                    // Mostrar un mensaje de error si no se pudo eliminar el gasto
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("No se pudo eliminar el gasto.");
                    errorAlert.showAndWait();
                }
            }
        } else {
            // Mostrar un mensaje si no se seleccionó ningún gasto
            Alert noSelectionAlert = new Alert(Alert.AlertType.WARNING);
            noSelectionAlert.setTitle("Advertencia");
            noSelectionAlert.setHeaderText(null);
            noSelectionAlert.setContentText("Por favor, selecciona un gasto para eliminar.");
            noSelectionAlert.showAndWait();
        }
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
    public TableView<Charge> getTableViewGastos() {
        return Gastos;
    }
}
