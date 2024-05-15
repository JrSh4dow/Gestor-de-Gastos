package controlador;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
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
import model.Category;
import model.Charge;
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
    @FXML
    private Button eliminarCategoria;

    public void initialize(URL url, ResourceBundle rb) throws AcountDAOException, IOException {
        NameGasto.requestFocus();
        
        // Llenar el ChoiceBox con las categorías del usuario
        llenarChoiceBoxConCategorias();
    }

    // añadir el gasto a la base de datos
    @FXML
    private void AñadirGasto(ActionEvent event) throws IOException, AcountDAOException {
        // Obtener los valores del formulario
        String nombreGasto = NameGasto.getText();
        String descripcionGasto = DescriptionGasto.getText();
        double costeGasto = Double.parseDouble(CosteGasto.getText());
        int unidadesGasto = Integer.parseInt(UnidadeGasto.getText());
        String categoriaSeleccionada = CategoriaGasto.getValue();
        LocalDate fechaGasto = FechaGasto.getValue();
        Image imagenFactura = Factura.getImage();

        // Verificar si se han ingresado todos los datos obligatorios
        if (nombreGasto.isEmpty() || costeGasto <= 0 || unidadesGasto <= 0 || categoriaSeleccionada == null || fechaGasto == null) {
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

        // Verificar si se encontró la categoría
        if (categoria == null) {
            Utils.mostrarError("La categoría seleccionada no es válida.");
            return;
        }

        // Registrar el gasto en la base de datos
        boolean registrado = account.registerCharge(nombreGasto, descripcionGasto,costeGasto, unidadesGasto,  imagenFactura ,fechaGasto,categoria);

        // Verificar si el gasto se registró correctamente
        if (registrado) {
            Utils.mostrarAlerta("El gasto se ha registrado correctamente.");
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
    void AñadirCategoria(ActionEvent event) throws IOException, AcountDAOException {
        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("../vista/AñadirCategoria.fxml"));
        Parent root = miCargador.load();
        Stage mainStage = new Stage();
        mainStage.setScene(new Scene(root));
        mainStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/imagenes/logo-sin.png")));
        mainStage.setTitle("Inicio");
        mainStage.setResizable(false);
        mainStage.initModality(Modality.APPLICATION_MODAL);
        mainStage.showAndWait();
        
        // Actualizar el ChoiceBox después de añadir una categoría
        llenarChoiceBoxConCategorias();
    }

    @FXML
    void IrInicio(ActionEvent event) throws IOException, AcountDAOException {
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
    
    private void llenarChoiceBoxConCategorias() throws AcountDAOException, IOException {
        Acount account = Acount.getInstance();
        List<Category> categorias = account.getUserCategories();
        
        if (categorias != null) {
            // Limpiar el ChoiceBox
            CategoriaGasto.getItems().clear();
            
            // Llenar el ChoiceBox con los nombres de las categorías
            for (Category categoria : categorias) {
                // Excluir la descripción de las categorías
                if (!categoria.getName().equalsIgnoreCase("descripción")) {
                    CategoriaGasto.getItems().add(categoria.getName());
                }
            }
        }
    }

    @FXML
    private void EliminarCategoria(ActionEvent event) throws AcountDAOException, IOException {
        // Obtener la categoría seleccionada
    String nombreCategoriaSeleccionada = CategoriaGasto.getValue();
    
    // Verificar si se ha seleccionado una categoría
    if (nombreCategoriaSeleccionada != null) {
        // Obtener la instancia de la cuenta
        Acount account = Acount.getInstance();
        
        // Obtener la categoría seleccionada
        Category categoriaSeleccionada = null;
        List<Category> categorias = account.getUserCategories();
        for (Category categoria : categorias) {
            if (categoria.getName().equals(nombreCategoriaSeleccionada)) {
                categoriaSeleccionada = categoria;
                break;
            }
        }
        
        // Verificar si se encontró la categoría seleccionada
        if (categoriaSeleccionada != null) {
            // Mostrar un diálogo de confirmación
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmar eliminación");
            alert.setHeaderText("Eliminar categoría");
            alert.setContentText("¿Seguro que quieres eliminar la categoría seleccionada?");
            
            // Obtener la respuesta del usuario
            Optional<ButtonType> result = alert.showAndWait();
            
            // Verificar si el usuario confirmó la eliminación
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Eliminar la categoría de la cuenta
                boolean eliminada = account.removeCategory(categoriaSeleccionada);
                
                // Verificar si la categoría se eliminó correctamente
                if (eliminada) {
                    // Mostrar un mensaje de éxito
                    Utils.mostrarAlerta("La categoría ha sido eliminada correctamente.");
                    
                    // Actualizar el ChoiceBox después de eliminar la categoría
                    llenarChoiceBoxConCategorias();
                } else {
                    // Mostrar un mensaje de error
                    Utils.mostrarError("No se pudo eliminar la categoría.");
                }
            }
        } else {
            // Mostrar un mensaje de error si no se encontró la categoría seleccionada
            Utils.mostrarError("No se encontró la categoría seleccionada.");
        }
    } else {
        // Mostrar un mensaje de advertencia si no se ha seleccionado una categoría
        Utils.mostrarAlerta("Por favor, selecciona una categoría para eliminar.");
    }
    }

}
