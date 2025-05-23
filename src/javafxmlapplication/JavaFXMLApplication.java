package javafxmlapplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class JavaFXMLApplication extends Application {

    private static Scene scene;

    public static void setRoot(Parent root) {
        scene.setRoot(root);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // ======================================================================
        // 1- creación del grafo de escena a partir del fichero FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Inicio.fxml"));
        Parent root = loader.load();
        // ======================================================================
        // 2- creación de la escena con el nodo raiz del grafo de escena
        Scene scene = new Scene(root);
        String css = this.getClass().getResource("/estilos/inicio.css").toExternalForm();
        scene.getStylesheets().add(css);
        // ======================================================================
        // 3- asiganación de la escena al Stage que recibe el metodo
        // - configuracion del stage
        // - se muestra el stage de manera no modal mediante el metodo show()
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/imagenes/logo-sin.png")));
        stage.setTitle("Expense Tracker");
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);

    }

}
