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
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;
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

    public void initialize(URL url, ResourceBundle rb) {
        c.setShowDelay(Duration.ZERO);
        a.setShowDelay(Duration.ZERO);
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
        NameGasto.focusedProperty()
                .addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    if (!newValue) { // focus lost.

                        if (NameGasto.getText().isEmpty()) {
                            Utils.error(NameGasto);
                        } else {
                            Utils.correct(NameGasto);
                            validName.setValue(Boolean.TRUE);
                        }
                    }
                });
        DescriptionGasto.focusedProperty()
                .addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    if (!newValue) { // focus lost.

                        if (DescriptionGasto.getText().isEmpty()) {
                            Utils.error(DescriptionGasto);
                        } else {
                            Utils.correct(DescriptionGasto);
                            validDescripcion.setValue(Boolean.TRUE);
                        }
                    }
                });
        CosteGasto.focusedProperty()
                .addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    if (!newValue) { // focus lost.

                        if (CosteGasto.getText().isEmpty() || !Utils.checkDigit(CosteGasto.getText())) {
                            Utils.error(CosteGasto);
                        } else {
                            Utils.correct(CosteGasto);
                            validCoste.setValue(Boolean.TRUE);
                        }
                    }
                });
        UnidadeGasto.focusedProperty()
                .addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    if (!newValue) { // focus lost.

                        if (NameGasto.getText().isEmpty() || !Utils.checkDigit(UnidadeGasto.getText())) {
                            Utils.error(UnidadeGasto);
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
        NameGasto.requestFocus();
        applyFilter(CosteGasto);
        applyFilter(UnidadeGasto);
        try {
            llenarChoiceBoxConCategorias();
        } catch (AcountDAOException | IOException e) {
            e.printStackTrace();
        }
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
        boolean registrado = account.registerCharge(nombreGasto, descripcionGasto, Double.parseDouble(costoText),
                Integer.parseInt(unidadesText),
                imagenFactura, fechaGasto, categoria);

        // Verificar si el gasto se registró correctamente
        if (registrado) {
            Utils.mostrarInfo("El gasto se ha registrado correctamente.");
            NameGasto.clear();
            CosteGasto.clear();
            UnidadeGasto.clear();
            DescriptionGasto.clear();
            CategoriaGasto.getSelectionModel().clearSelection();
            Factura.setImage(null);
            FechaGasto.setValue(null);
        } else {
            Utils.mostrarError("No se pudo registrar el gasto.");
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

    // Método para aplicar un filtro numérico a un campo de texto
    public static void applyFilter(TextField textField) {
        // Crear un formateador de texto que solo acepte números enteros
        TextFormatter<Integer> textFormatter = new TextFormatter<>(new IntegerStringConverter(), null,
                c -> {
                    if (c.getControlNewText().matches("\\d*")) { // Solo permite dígitos
                        return c;
                    } else {
                        return null; // Rechaza la entrada si no es un dígito
                    }
                });

        // Aplicar el formateador de texto al campo de texto
        textField.setTextFormatter(textFormatter);
    }

}
