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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.Duration;
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
public class ModificarGastoController implements Initializable {

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
    private Button CancelarCambios;
    @FXML
    private TextArea DescriptionGasto;
    @FXML
    private Button GuardarCambios;
    private Charge act;
    @FXML
    private TextField Idfield;

    private BooleanProperty validName;
    private BooleanProperty validDescripcion;
    private BooleanProperty validFecha;
    private BooleanProperty validCoste;
    private BooleanProperty validUnidade;
    private BooleanProperty validCategory;
    @FXML
    private Tooltip n;

    public void initGasto(Charge c) {
        this.act = c;
        // Aqui hay que inicializar con los campos del gasto seleccionado
        Idfield.setText(String.valueOf(act.getId()));
        NameGasto.setText(act.getName());
        DescriptionGasto.setText(act.getDescription());
        CosteGasto.setText(String.valueOf(act.getCost()));
        UnidadeGasto.setText(String.valueOf(act.getUnits()));
        // Establecer la categoría seleccionada en el ChoiceBox
        CategoriaGasto.getSelectionModel().select(act.getCategory().getName());
        // Establecer la fecha seleccionada en el DatePicker
        FechaGasto.setValue(act.getDate());
        Factura.setImage(act.getImageScan());

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        n.setShowDelay(Duration.ZERO);
        validCategory = new SimpleBooleanProperty();
        validCoste = new SimpleBooleanProperty();
        validDescripcion = new SimpleBooleanProperty();
        validName = new SimpleBooleanProperty();
        validFecha = new SimpleBooleanProperty();
        validUnidade = new SimpleBooleanProperty();
        validCategory.setValue(Boolean.TRUE);
        validCoste.setValue(Boolean.TRUE);
        validDescripcion.setValue(Boolean.TRUE);
        validFecha.setValue(Boolean.TRUE);
        validName.setValue(Boolean.TRUE);
        validUnidade.setValue(Boolean.TRUE);
        // Listener para NameGasto
        NameGasto.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                Utils.error(NameGasto);
                validName.setValue(Boolean.FALSE);
            } else {
                Utils.correct(NameGasto);
                validName.setValue(Boolean.TRUE);
            }
        });

        // Listener para DescriptionGasto
        DescriptionGasto.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                Utils.error(DescriptionGasto);
                validDescripcion.setValue(Boolean.FALSE);
            } else {
                Utils.correct(DescriptionGasto);
                validDescripcion.setValue(Boolean.TRUE);
            }
        });
        CosteGasto.focusedProperty()
                .addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    if (!newValue) { // focus lost.
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
                    if (!newValue) { // focus lost.

                        if (NameGasto.getText().isEmpty()) {
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
        GuardarCambios.disableProperty().bind(validCategory.not().or(validCoste.not()
                .or(validName.not().or(validDescripcion.not().or(validFecha.not().or(validUnidade.not()))))));

        try {
            llenarChoiceBoxConCategorias();
        } catch (AcountDAOException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void Cancelar(ActionEvent event) {
        Stage a = (Stage) CancelarCambios.getScene().getWindow();
        a.close();
    }

    @FXML
    private void SetImage(ActionEvent event) {
        Window n = ((Node) event.getSource()).getScene().getWindow();
        String facturaPath = Utils.ElegirImagen(n);
        if (facturaPath != null) {
            Factura.setImage(new Image(facturaPath));
        }
    }

    // Guardar las modificaciones del gasto
    @FXML
    private void Modificar(ActionEvent event) throws AcountDAOException, IOException {

        // Obtener los valores del formulario
        String nombreGasto = NameGasto.getText();
        String descripcionGasto = DescriptionGasto.getText();
        double costeGasto = Double.parseDouble(CosteGasto.getText());
        int unidadesGasto = Integer.parseInt(UnidadeGasto.getText());
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
        // modificar el gasto en la base de datos
        act.setCategory(categoria);
        act.setCost(costeGasto);
        act.setDate(fechaGasto);
        act.setDescription(descripcionGasto);
        act.setImageScan(imagenFactura);
        act.setName(nombreGasto);
        act.setUnits(unidadesGasto);
        // Verificar si el gasto se registró correctamente

        Utils.mostrarInfo("Se ha modificado el gasto  correctamente.");
        Stage mainStage2 = (Stage) GuardarCambios.getScene().getWindow();
        mainStage2.close();

    }

    private void llenarChoiceBoxConCategorias() throws AcountDAOException, IOException {
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
