package controlador;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PageLayout;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.Printer.MarginType;
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
import javafx.scene.transform.Scale;
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
        eliminarGasto.setDisable(true);
        modificarGasto.setDisable(true);
        Gastos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Habilitar botones cuando se selecciona una fila
                eliminarGasto.setDisable(false);
                modificarGasto.setDisable(false);
            } else {
                // Deshabilitar botones cuando no hay ninguna fila seleccionada
                eliminarGasto.setDisable(true);
                modificarGasto.setDisable(true);
            }
        });
        // Iniciar La TableView con los gastos desde la base de datos
        categoriaGasto.setCellFactory(column -> {
            return new TableCell<Charge, Category>() {
                @Override
                protected void updateItem(Category category, boolean empty) {
                    super.updateItem(category, empty);

                    if (empty || category == null) {
                        setText(null);
                    } else {
                        setWrapText(true);
                        setText(category.getName());
                    }
                }
            };
        });

        facturaGasto.setCellFactory(column -> new TableCell<Charge, Image>() {

            @Override
            protected void updateItem(Image scanImage, boolean empty) {
                super.updateItem(scanImage, empty);
                if (empty || scanImage == null) {
                    setGraphic(null);
                } else {
                    ImageView imageView = new ImageView(scanImage);
                    imageView.setFitWidth(80);
                    imageView.setFitHeight(40);
                    setGraphic(imageView);
                }
            }
        });
        descripcionGasto.setCellFactory(column -> {
            return new TableCell<Charge, String>() {
                @Override
                protected void updateItem(String description, boolean empty) {
                    super.updateItem(description, empty);

                    if (empty || description == null) {
                        setText(null);
                    } else {
                        // Permitir el ajuste del texto
                        setWrapText(true);
                        setText(description);
                    }
                }
            };
        });

        nombreGasto.setCellFactory(column -> {
            return new TableCell<Charge, String>() {
                @Override
                protected void updateItem(String name, boolean empty) {
                    super.updateItem(name, empty);

                    if (empty || name == null) {
                        setText(null);
                    } else {
                        // Permitir el ajuste del texto
                        setWrapText(true);
                        setText(name);
                    }
                }
            };
        });
        idGasto.setCellValueFactory(new PropertyValueFactory<>("id"));
        FechaGasto.setCellValueFactory(new PropertyValueFactory<>("date"));
        nombreGasto.setCellValueFactory(new PropertyValueFactory<>("name"));
        descripcionGasto.setCellValueFactory(new PropertyValueFactory<>("description"));
        categoriaGasto.setCellValueFactory(new PropertyValueFactory<>("category"));
        costeGasto.setCellValueFactory(new PropertyValueFactory<>("cost"));
        unidadesGasto.setCellValueFactory(new PropertyValueFactory<>("units"));
        facturaGasto.setCellValueFactory(cellData -> {

            Charge charge = cellData.getValue();
            if (charge != null) {
                return new SimpleObjectProperty<>(charge.getImageScan());
            } else {
                return new SimpleObjectProperty<>(null);
            }
        });
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
            String css = this.getClass().getResource("/estilos/Alert.css").toExternalForm();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Eliminar Gasto");
            alert.setHeaderText("¿Estás seguro de que deseas eliminar el gasto : '" + gastoSeleccionado.getName() + "' ?");
            alert.setContentText("Esta acción no se puede deshacer.");
            alert.getDialogPane().getStylesheets().add(css);
            alert.getDialogPane().getStyleClass().add("custom-alert");
            // Establecer una imagen como icono para la ventana de la alerta
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/imagenes/logo.jpeg")); // Reemplaza 'ruta/a/tu/imagen.png' con la ruta de tu imagen
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
                    successAlert.getDialogPane().getStylesheets().add(css);
                    successAlert.getDialogPane().getStyleClass().add("custom-alert");
                    stage = (Stage) successAlert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("/imagenes/logo.jpeg"));
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
                    errorAlert.getDialogPane().getStylesheets().add(css);
                    errorAlert.getDialogPane().getStyleClass().add("custom-alert");
                    errorAlert.showAndWait();
                    stage = (Stage) errorAlert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("/imagenes/logo.jpeg"));
                }
            }
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
    private void ModificarGasto(ActionEvent event) throws IOException, AcountDAOException {
        // Obtener el gasto seleccionado en la TableView
        Charge gastoSeleccionado = Gastos.getSelectionModel().getSelectedItem();

        // Verificar si se seleccionó un gasto
        if (gastoSeleccionado != null) {
            CargaVistas.MODIFICARGASTO(gastoSeleccionado);
            Gastos.getItems().clear();
            Gastos.refresh();
            List<Charge> gastos = Acount.getInstance().getUserCharges();
            // Agregar los datos a la TableView
            Gastos.getItems().addAll(gastos);

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
        if (printerJob != null) {

            Printer printer = Printer.getDefaultPrinter();
            PageLayout pageLayout = printer.createPageLayout(Paper.A4, javafx.print.PageOrientation.PORTRAIT,
                    MarginType.HARDWARE_MINIMUM);

            Node node = Gastos;
            node.getTransforms().add(new Scale(0.6, 0.6));

            // Imprimir el nodo en el archivo PDF
            boolean printed = printerJob.printPage(pageLayout, node);
            if (printed) {
                printerJob.endJob();
            }

            // Restaurar la escala original después de imprimir
            node.getTransforms().clear();
        }

    }

}
