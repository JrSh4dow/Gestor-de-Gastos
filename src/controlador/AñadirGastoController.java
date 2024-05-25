package controlador;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.converter.DoubleStringConverter;
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

    public void initialize(URL url, ResourceBundle rb) {
        NameGasto.requestFocus();
        applyDoubleFilter(CosteGasto);
        applyFilter(UnidadeGasto);
        try {
            llenarChoiceBoxConCategorias();
        } catch (AcountDAOException | IOException e) {
            e.printStackTrace();
        }
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
    if (nombreGasto.isEmpty() || categoriaSeleccionada == null || fechaGasto == null || costoText.isEmpty() || unidadesText.isEmpty()) {
        Utils.mostrarAlerta("Por favor, complete todos los campos obligatorios.");
        return;
    }

    double costeGasto;
    int unidadesGasto;
    try {
        // Reemplazar comas con puntos para manejar decimales correctamente
        costoText = costoText.replace(',', '.');
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
        boolean registrado = account.registerCharge(nombreGasto, descripcionGasto, costeGasto, unidadesGasto, imagenFactura, fechaGasto, categoria);

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
    public static void applyDoubleFilter(TextField textField) {
    // Crear un formateador de texto que solo acepte números decimales
    TextFormatter<Double> textFormatter = new TextFormatter<>(new DoubleStringConverter(), null, c -> {
        // Permitir solo dígitos, comas, y puntos
        if (c.getControlNewText().matches("\\d*|\\d+([.,]\\d*)?")) {
            return c;
        }
        return null;
    });

    // Añadir un listener para reemplazar comas con puntos
    textField.textProperty().addListener((observable, oldValue, newValue) -> {
        if (newValue.contains(",")) {
            textField.setText(newValue.replace(',', '.'));
        }
    });

    // Aplicar el formateador de texto al campo de texto
    textField.setTextFormatter(textFormatter);
    }




}
