package controlador;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Acount;
import model.AcountDAOException;
import model.Category;
import model.Charge;
import utils.CargaVistas;
import utils.Utils;

public class MainController implements Initializable {

    @FXML
    private Button Perfil;
    @FXML
    private Button verGasto;
    @FXML
    private Button añadirGasto;
    @FXML
    private BorderPane Caja;
    @FXML
    private PieChart Pie;
    @FXML
    private Label TotalGastos;
    @FXML
    private ChoiceBox<String> fecha;
    @FXML
    private Button AplicarFiltro;
    @FXML
    private Button opcionesAvanzadas;

    public void initialize(URL url, ResourceBundle rb) {
        AplicarFiltro.setDisable(true);
        List<Category> categories;
        List<Charge> charges;
        ObservableList<PieChart.Data> pieChartData;
        try {
            categories = Acount.getInstance().getUserCategories();
            charges = Acount.getInstance().getUserCharges();
            pieChartData = FXCollections.observableArrayList();

            for (Category category : categories) {
                Double total = Total(category, charges);
                if (total != null && total > 0) {
                    pieChartData.add(new PieChart.Data(category.getName(), total));
                }
            }
            // Crea el gráfico de pastel
            Pie.setData(pieChartData);
            Pie.setTitle("Distribución de gastos:");
        } catch (AcountDAOException | IOException e) {
            e.printStackTrace();
        }

        // Inicializa la ChoiceBox
        ObservableList<String> options = FXCollections.observableArrayList("Último mes", "Últimos 2 meses",
                "Últimos 3 meses", "Últimos 6 meses");
        fecha.setItems(options);
        // Agregar listener al ChoiceBox
        fecha.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                AplicarFiltro.setDisable(false);
            }
        });
        AplicarFiltro.setOnAction(event -> {
            try {
                filtrarGastos();
            } catch (AcountDAOException | IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    void AñadirGasto(ActionEvent event) throws IOException {
        Stage stage = (Stage) añadirGasto.getScene().getWindow();
        stage.close();
        CargaVistas.AÑADIRGASTO();
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
    void VerGasto(ActionEvent event) throws IOException {
        Stage stage = (Stage) verGasto.getScene().getWindow();
        stage.close();
        CargaVistas.GASTOS();
    }

    @FXML
    void VerPerfil(ActionEvent event) throws IOException {
        Stage stage = (Stage) Perfil.getScene().getWindow();
        stage.close();
        CargaVistas.PERFIL();
    }

    private Double Total(Category category, List<Charge> charges) {
        Double total = 0.0;
        if (!charges.isEmpty()) {
            for (Charge charge : charges) {
                if (charge.getCategory().equals(category)) {
                    total += charge.getCost() * charge.getUnits();
                }
            }
        }
        return total;
    }

    private void filtrarGastos() throws AcountDAOException, IOException {
        List<Charge> charges = Acount.getInstance().getUserCharges();
        String opcionSeleccionada = fecha.getValue();
        final LocalDate fechaLimite; // Declara la variable como final

        // Calcula la fecha límite según la opción seleccionada
        switch (opcionSeleccionada) {
            case "Último mes":
                fechaLimite = LocalDate.now().minus(1, ChronoUnit.MONTHS);
                break;
            case "Últimos 2 meses":
                fechaLimite = LocalDate.now().minus(2, ChronoUnit.MONTHS);
                break;
            case "Últimos 3 meses":
                fechaLimite = LocalDate.now().minus(3, ChronoUnit.MONTHS);
                break;
            case "Últimos 6 meses":
                fechaLimite = LocalDate.now().minus(6, ChronoUnit.MONTHS);
                break;
            default:
                // Opción no válida
                return;
        }

        // Filtra y suma los gastos dentro del rango de fechas
        double totalGastos = charges.stream()
                .filter(charge -> charge.getDate().isAfter(fechaLimite))
                .mapToDouble(Charge::getCost)
                .sum();

        // Muestra el resultado en TotalGastos
        TotalGastos.setText(String.format("Total de gastos: %.2f", totalGastos));
    }

    @FXML
    private void Vercategorias(ActionEvent event) throws IOException {
        FXMLLoader miCargador = new FXMLLoader(CargaVistas.class.getResource("../vista/VerCategorias.fxml"));
        Parent root = miCargador.load();
        Stage mainStage = new Stage();
        mainStage.setScene(new Scene(root));
        mainStage.getIcons().add(new Image(CargaVistas.class.getResourceAsStream("/imagenes/logo-sin.png")));
        mainStage.setTitle("VER CATEGORIAS");
        mainStage.setResizable(false);
        mainStage.initModality(Modality.APPLICATION_MODAL);
        mainStage.showAndWait();
    }

    @FXML
    private void OpcionesAvanzadas(ActionEvent event) throws IOException {
        FXMLLoader miCargador = new FXMLLoader(CargaVistas.class.getResource("../vista/OpcionesAvanzadas.fxml"));
        Parent root = miCargador.load();
        Stage mainStage = new Stage();
        mainStage.setScene(new Scene(root));
        mainStage.getIcons().add(new Image(CargaVistas.class.getResourceAsStream("/imagenes/logo-sin.png")));
        mainStage.setTitle("OPCIONES AVANZADAS");
        mainStage.setResizable(false);
        mainStage.initModality(Modality.APPLICATION_MODAL);
        mainStage.showAndWait();
    }

}
