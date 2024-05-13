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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;
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
    private Button GuardarCambios;
    private Charge act;

    public void initGasto(Charge c) {
        this.act = c;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            llenarChoiceBoxConCategorias();
        } catch (AcountDAOException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Aqui hay que inicializar con los campos del gasto seleccionado
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

    @FXML
    private void Cancelar(ActionEvent event) {
        Stage a = (Stage) CancelarCambios.getScene().getWindow();
        a.close();
    }

    @FXML
    private void SetImage(ActionEvent event) {
        Window n = ((Node) event.getSource()).getScene().getWindow();
        String facturaPath = utils.Utils.ElegirImagen(n);
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

        // Verificar si se han ingresado todos los datos obligatorios
        if (nombreGasto.isEmpty() || costeGasto <= 0 || unidadesGasto <= 0 || categoriaSeleccionada == null
                || fechaGasto == null) {
            Utils.mostrarAlerta("Por favor, complete todos los campos obligatorios.");
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
        /*
         * // Verificar si se encontró la categoría
         * if (categoria == null) {
         * Utils.mostrarError("La categoría seleccionada no es válida.");
         * return;
         * }
         */

        // Registrar el gasto en la base de datos
        boolean modificado = account.registerCharge(nombreGasto, descripcionGasto, costeGasto, unidadesGasto,
                imagenFactura, fechaGasto, categoria);

        // Verificar si el gasto se registró correctamente
        if (modificado) {
            Utils.mostrarInfo("El gasto se ha modificado correctamente.");
            Stage mainStage2 = (Stage) GuardarCambios.getScene().getWindow();
            mainStage2.close();
        } else {
            Utils.mostrarError("No se pudo modificar el gasto.");
        }
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
