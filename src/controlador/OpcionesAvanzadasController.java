package controlador;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.util.Callback;
import javafx.util.Duration;
import model.Acount;
import model.AcountDAOException;
import model.Category;
import model.Charge;

/**
 * FXML Controller class
 *
 * @author MEG
 */
public class OpcionesAvanzadasController implements Initializable {

    @FXML
    private Label TotalMesActual;
    @FXML
    private TableView<Category> TablaMesActual;
    @FXML
    private DatePicker FechaElegida;
    @FXML
    private Tooltip Tip;
    @FXML
    private Label TotalMesElegido;
    @FXML
    private TableView<Category> TablaMesElegido;
    @FXML
    private BarChart<Integer, Double> Grafica;
    @FXML
    private TableColumn<Category, String> CategoriaMesActual;
    @FXML
    private TableColumn<Category, String> CategoriaMesElegido;
    @FXML
    private TableColumn<Category, Double> ColumnaTotalMesActual;
    @FXML
    private TableColumn<Category, Double> ColumnaTotalMesElegido;
    static List<Charge> charges;
    List<Category> categories;
    LocalDate actual;
    LocalDate elegida;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        actual = LocalDate.now();
        FechaElegida.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                elegida = FechaElegida.getValue();
                TotalMesElegido.setText("Total: " + TotalGastado(elegida) + " €");
                ColumnaTotalMesElegido
                        .setCellValueFactory(
                                cellData -> new SimpleDoubleProperty(Totales(cellData.getValue(), elegida)).asObject());
                TablaMesElegido.getItems().clear();
                TablaMesElegido.getItems().addAll(categories);
            }
        });
        LocalDate minDate = LocalDate.of(2022, 1, 1);
        LocalDate maxDate = LocalDate.now();
        // formatear las fechas
        FechaElegida.setDayCellFactory(new Callback<DatePicker, DateCell>() {
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
        try {
            charges = Acount.getInstance().getUserCharges();
            categories = Acount.getInstance().getUserCategories();
        } catch (AcountDAOException | IOException e) {
            e.printStackTrace();
        }
        CategoriaMesActual.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        CategoriaMesElegido.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        ColumnaTotalMesActual
                .setCellValueFactory(
                        cellData -> new SimpleDoubleProperty(Totales(cellData.getValue(), actual)).asObject());
        Tip.setShowDelay(Duration.ZERO);
        TablaMesActual.getItems().addAll(categories);
        TablaMesElegido.getItems().addAll(categories);

        TotalMesActual.setText("Total: " + TotalGastado(actual) + " €");
    }

    public static double Totales(Category cat, LocalDate m) {
        double total = 0.0;
        int targetMonth = m.getMonthValue();
        int targetYear = m.getYear();

        for (Charge charge : charges) {
            if (charge.getCategory().getName().equals(cat.getName())) {
                int chargeMonth = charge.getDate().getMonthValue();
                int chargeYear = charge.getDate().getYear();
                if (chargeMonth == targetMonth && chargeYear == targetYear) {
                    total += charge.getCost();
                }
            }
        }
        return total;
    }

    public static double TotalGastado(LocalDate m) {
        double total = 0.0;
        int targetMonth = m.getMonthValue();
        int targetYear = m.getYear();

        for (Charge charge : charges) {
            int chargeMonth = charge.getDate().getMonthValue();
            int chargeYear = charge.getDate().getYear();
            if (chargeMonth == targetMonth && chargeYear == targetYear) {
                total += charge.getCost();
            }
        }
        return total;
    }

}
