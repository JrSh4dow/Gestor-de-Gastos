package controlador;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.Duration;
import model.Acount;
import model.AcountDAOException;
import model.Category;
import utils.CargaVistas;
import utils.Utils;

public class AñadirGastoController implements Initializable {

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

    private BooleanProperty validName;
    private BooleanProperty validDescripcion;
    private BooleanProperty validFecha;
    private BooleanProperty validCoste;
    private BooleanProperty validUnidade;
    private BooleanProperty validCategory;
    @FXML
    private Tooltip c;
    @FXML
    private Tooltip a;
    private boolean disableValidators = false;
    @FXML
    private Tooltip n;

    public void initialize(URL url, ResourceBundle rb) {
        c.setShowDelay(Duration.ZERO);
        a.setShowDelay(Duration.ZERO);
        n.setShowDelay(Duration.ZERO);
        validCategory = new SimpleBooleanProperty();
        validCoste = new SimpleBooleanProperty();
        validDescripcion = new SimpleBooleanProperty();
        validName = new SimpleBooleanProperty();
        validFecha = new SimpleBooleanProperty();
        validUnidade = new SimpleBooleanProperty();
        validCategory.setValue(Boolean.FALSE);
        validCoste.setValue(Boolean.FALSE);
        validDescripcion.setValue(Boolean.FALSE);
        validFecha.setValue(Boolean.FALSE);
        validName.setValue(Boolean.FALSE);
        validUnidade.setValue(Boolean.FALSE);
        // Listener para NameGasto
        NameGasto.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!disableValidators) {
                if (newValue.isEmpty()) {
                    Utils.error(NameGasto);
                    validName.setValue(Boolean.FALSE);
                } else {
                    Utils.correct(NameGasto);
                    validName.setValue(Boolean.TRUE);
                }
            }
        });

        // Listener para DescriptionGasto
        DescriptionGasto.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!disableValidators) {
                if (newValue.isEmpty()) {
                    Utils.error(DescriptionGasto);
                    validDescripcion.setValue(Boolean.FALSE);
                } else {
                    Utils.correct(DescriptionGasto);
                    validDescripcion.setValue(Boolean.TRUE);
                }
            }
        });
        CosteGasto.focusedProperty()
                .addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    if (!newValue && !disableValidators) { // focus lost.
                        n.hide();
                        if (CosteGasto.getText().isEmpty()) {
                            Utils.error(CosteGasto);
                            validCoste.setValue(Boolean.FALSE);
                        } else {
                            Utils.correct(CosteGasto);
                            validCoste.setValue(Boolean.TRUE);
                        }
                    } else {
                        Point2D p = CosteGasto.localToScreen(CosteGasto.getLayoutBounds().getMaxX(),
                                CosteGasto.getLayoutBounds().getMaxY()); // Posición del TextField
                        n.show(CosteGasto, p.getX(), p.getY());
                    }
                });

        UnidadeGasto.focusedProperty()
                .addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    if (!newValue && !disableValidators) { // focus lost.

                        if (UnidadeGasto.getText().isEmpty()) {
                            Utils.error(UnidadeGasto);
                            validUnidade.setValue(Boolean.FALSE);
                        } else {
                            Utils.correct(UnidadeGasto);
                            validUnidade.setValue(Boolean.TRUE);
                        }
                    }
                });
        // Agregar listener al ChoiceBox
        CategoriaGasto.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                validCategory.setValue(Boolean.TRUE);
            }
        });

        // Agregar listener al DatePicker
        FechaGasto.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                validFecha.setValue(Boolean.TRUE);
            }
        });
        Utils.applyDoubleFilter(CosteGasto);
        Utils.applyFilter(UnidadeGasto);
        try {
            llenarChoiceBoxConCategorias();
        } catch (AcountDAOException | IOException e) {
            e.printStackTrace();
        }
        NameGasto.requestFocus();
        LocalDate minDate = LocalDate.of(2022, 1, 1);
        LocalDate maxDate = LocalDate.now();
        // formatear las fechas
        FechaGasto.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item.isBefore(minDate) || item.isAfter(maxDate)) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;"); // Color rosa para las fechas deshabilitadas
                        }
                    }
                };
            }
        });
        añadirGasto.disableProperty().bind(validCategory.not().or(validCoste.not()
                .or(validName.not().or(validDescripcion.not().or(validFecha.not().or(validUnidade.not()))))));
    }

    // añadir el gasto a la base de datos
    @FXML
    private void AñadirGasto(ActionEvent event) throws AcountDAOException, IOException {
        // Obtener los valores del formulario
        String nombreGasto = NameGasto.getText().trim();
        String descripcionGasto = DescriptionGasto.getText().trim();
        String costoText = CosteGasto.getText().trim();
        String unidadesText = UnidadeGasto.getText().trim();
        String categoriaSeleccionada = CategoriaGasto.getValue();
        LocalDate fechaGasto = FechaGasto.getValue();
        Image imagenFactura = Factura.getImage();

        // Verificar campos obligatorios y validar datos numéricos
        if (nombreGasto.isEmpty() || categoriaSeleccionada == null || fechaGasto == null || costoText.isEmpty()
                || unidadesText.isEmpty()) {
            Utils.mostrarAlerta("Por favor, complete todos los campos obligatorios.");
            return;
        }

        double costeGasto;
        int unidadesGasto;
        try {
            costeGasto = Double.parseDouble(costoText);
            unidadesGasto = Integer.parseInt(unidadesText);
            if (costeGasto <= 0 || unidadesGasto <= 0) {
                Utils.mostrarAlerta("El costo y las unidades deben ser mayores que cero.");
                return;
            }
        } catch (NumberFormatException e) {
            Utils.mostrarAlerta("Por favor, ingrese valores numéricos válidos para costo y unidades.");
            return;
        }

        // Obtener la categoría seleccionada por su nombre
        Acount account = Acount.getInstance();
        List<Category> categorias = account.getUserCategories();
        Category categoria = null;
        for (Category c : categorias) {
            if (c.getName().equals(categoriaSeleccionada)) {
                categoria = c;
                break;
            }
        }

        // Registrar el gasto en la base de datos
        try {
            boolean registrado = account.registerCharge(nombreGasto, descripcionGasto, costeGasto, unidadesGasto,
                    imagenFactura, fechaGasto, categoria);

            // Verificar si el gasto se registró correctamente
            if (registrado) {
                Utils.mostrarInfo("El gasto se ha registrado correctamente.");
                // Temporarily disable validators while clearing fields
                disableValidators = true;

                NameGasto.clear();
                CosteGasto.clear();
                UnidadeGasto.clear();
                DescriptionGasto.clear();
                CategoriaGasto.getSelectionModel().clearSelection();
                Factura.setImage(null);
                FechaGasto.setValue(null);

                // Re-enable validators after clearing fields
                disableValidators = false;
            } else {
                Utils.mostrarError("No se pudo registrar el gasto.");
            }
        } catch (AcountDAOException e) {
            Utils.mostrarError("Error al registrar el gasto: " + nombreGasto);
        }
    }

    @FXML
    void AñadirCategoria(ActionEvent event) throws IOException {
        Boolean ok = CargaVistas.CATEGORIA();
        if (ok) {
            try {
                llenarChoiceBoxConCategorias();
            } catch (AcountDAOException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void IrInicio(ActionEvent event) throws IOException, AcountDAOException {
        Stage mainStage2 = (Stage) Inicio.getScene().getWindow();
        mainStage2.close();
        CargaVistas.MAIN();

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
        CargaVistas.GASTOS();
    }

    @FXML
    void VerPerfil(ActionEvent event) throws IOException {
        Stage mainStage2 = (Stage) Perfil.getScene().getWindow();
        mainStage2.close();
        CargaVistas.PERFIL();
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

    protected void llenarChoiceBoxConCategorias() throws AcountDAOException, IOException {
        Acount account = Acount.getInstance();
        List<Category> categorias = account.getUserCategories();

        if (categorias != null) {
            // Limpiar el ChoiceBox
            CategoriaGasto.getItems().clear();

            // Llenar el ChoiceBox con los nombres de las categorías
            for (Category categoria : categorias) {
                CategoriaGasto.getItems().add(categoria.getName());
            }
        }
    }

}
