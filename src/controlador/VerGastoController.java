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
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Acount;
import model.AcountDAOException;
import model.Category;
import model.Charge;
import utils.CargaVistas;
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
    private TableColumn<Charge, Integer> idGasto;
    @FXML
    private TableColumn<Charge, String> nombreGasto;
    @FXML
    private TableColumn<Charge, String> descripcionGasto;
    @FXML
    private TableColumn<Charge, Category> categoriaGasto;
    @FXML
    private TableColumn<Charge, Double> costeGasto;
    @FXML
    private TableColumn<Charge, Integer> unidadesGasto;
    @FXML
    private TableColumn<Charge, Image> facturaGasto;
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
    @FXML
    private TableColumn<Charge, LocalDate> FechaGasto;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Iniciar La TableView con los gastos desde la base de datos
        categoriaGasto.setCellFactory(column -> {
            return new TableCell<Charge, Category>() {
                @Override
                protected void updateItem(Category category, boolean empty) {
                    super.updateItem(category, empty);

                    if (empty || category == null) {
                        setText(null);
                    } else {
                        setText(category.getName());
                    }
                }
            };
        });
        FechaGasto.setCellFactory(column -> new TableCell<Charge, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(date.toString());
                }
            }
        });
        facturaGasto.setCellFactory(column -> new TableCell<Charge, Image>() {
            @Override
            protected void updateItem(Image image, boolean empty) {
                super.updateItem(image, empty);
                if (empty || image == null) {
                    setGraphic(null);
                } else {
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(100);
                    imageView.setFitHeight(60);
                    setGraphic(imageView);
                }
            }
        });
        idGasto.setCellValueFactory(new PropertyValueFactory<>("id"));
        FechaGasto.setCellValueFactory(new PropertyValueFactory<>("fecha"));
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
        Gastos.refresh();
    }

    // Para eliminar un gasto de la base de datos
    @SuppressWarnings("unused")
    @FXML
    private void EliminarGasto(ActionEvent event) throws AcountDAOException, IOException {
        // Obtener el gasto seleccionado en la TableView
        Charge gastoSeleccionado = Gastos.getSelectionModel().getSelectedItem();
        Category act = gastoSeleccionado.getCategory();
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
                    if (!Utils.exist(act)) {
                        Acount.getInstance().removeCategory(act);
                    }
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
        Gastos.refresh();
    }

    @FXML
    private void TerminarSesion(ActionEvent event) throws IOException, AcountDAOException {
        Boolean ok = Utils.AcabarSesion();
        if (ok) {
            Node sourceNode = Caja.getScene().getRoot();
            Scene scene = sourceNode.getScene();
            Stage stage = (Stage) scene.getWindow();
            stage.close();
            CargaVistas.INICIO();
        }
    }

    @FXML
    private void IrInicio(ActionEvent event) throws IOException, AcountDAOException {
        Stage mainStage2 = (Stage) Inicio.getScene().getWindow();
        mainStage2.close();
        CargaVistas.MAIN();
    }

    @FXML
    private void VerPerfil(ActionEvent event) throws IOException {
        Stage mainStage2 = (Stage) Perfil.getScene().getWindow();
        mainStage2.close();
        CargaVistas.PERFIL();
    }

    @FXML
    private void ModificarGasto(ActionEvent event) throws IOException {
        // Obtener el gasto seleccionado en la TableView
        Charge gastoSeleccionado = Gastos.getSelectionModel().getSelectedItem();

        // Verificar si se seleccionó un gasto
        if (gastoSeleccionado != null) {
            CargaVistas.MODIFICARGASTO(gastoSeleccionado);
        } else {
            // Mostrar un mensaje si no se seleccionó ningún gasto
            Alert noSelectionAlert = new Alert(Alert.AlertType.WARNING);
            noSelectionAlert.setTitle("Advertencia");
            noSelectionAlert.setHeaderText(null);
            noSelectionAlert.setContentText("Por favor, selecciona un gasto para modificar.");
            noSelectionAlert.showAndWait();
        }
    }

    @FXML
    private void AñadirGasto(ActionEvent event) throws IOException {
        Stage mainStage2 = (Stage) añadirGasto.getScene().getWindow();
        mainStage2.close();
        CargaVistas.AÑADIRGASTO();
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
