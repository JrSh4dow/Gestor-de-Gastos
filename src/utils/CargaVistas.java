package utils;

import java.io.IOException;

import controlador.AñadirCategoriaController;
import controlador.ModificarGastoController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Charge;

public class CargaVistas {
    public static void MAIN() throws IOException {
        FXMLLoader miCargador = new FXMLLoader(CargaVistas.class.getResource("../vista/Main.fxml"));
        Parent root = miCargador.load();
        Stage mainStage = new Stage();
        mainStage.setScene(new Scene(root));
        mainStage.getIcons().add(new Image(CargaVistas.class.getResourceAsStream("/imagenes/logo-sin.png")));
        mainStage.setTitle("Principal");
        mainStage.setResizable(true);
        mainStage.initModality(Modality.APPLICATION_MODAL);
        mainStage.show();
    }

    public static void INICIO() throws IOException {
        FXMLLoader loader = new FXMLLoader(CargaVistas.class.getResource("/vista/Inicio.fxml"));
        Parent userRoot = loader.load();
        Stage inicioStage = new Stage();
        inicioStage.getIcons().add(new Image(CargaVistas.class.getResourceAsStream("/imagenes/logo-sin.png")));
        inicioStage.setTitle("Expense Tracker");
        inicioStage.setScene(new Scene(userRoot));
        inicioStage.show();
    }

    public static void PERFIL() throws IOException {
        FXMLLoader miCargador = new FXMLLoader(CargaVistas.class.getResource("../vista/VerPerfil.fxml"));
        Parent root = miCargador.load();
        Stage mainStage = new Stage();
        mainStage.setScene(new Scene(root));
        mainStage.getIcons().add(new Image(CargaVistas.class.getResourceAsStream("/imagenes/logo-sin.png")));
        mainStage.setTitle("PERFIL");
        mainStage.setResizable(true);
        mainStage.initModality(Modality.APPLICATION_MODAL);
        mainStage.show();
    }

    public static void AÑADIRGASTO() throws IOException {
        FXMLLoader miCargador = new FXMLLoader(CargaVistas.class.getResource("../vista/AñadirGasto.fxml"));
        Parent root = miCargador.load();
        Stage mainStage = new Stage();
        mainStage.setScene(new Scene(root));
        mainStage.getIcons().add(new Image(CargaVistas.class.getResourceAsStream("/imagenes/logo-sin.png")));
        mainStage.setTitle("AÑADIR GASTO");
        mainStage.setResizable(true);
        mainStage.initModality(Modality.APPLICATION_MODAL);
        mainStage.show();
    }

    public static void GASTOS() throws IOException {
        FXMLLoader miCargador = new FXMLLoader(CargaVistas.class.getResource("../vista/VerGastos.fxml"));
        Parent root = miCargador.load();
        Stage mainStage = new Stage();
        mainStage.setScene(new Scene(root));
        mainStage.getIcons().add(new Image(CargaVistas.class.getResourceAsStream("/imagenes/logo-sin.png")));
        mainStage.setTitle("GASTOS");
        mainStage.setResizable(true);
        mainStage.initModality(Modality.APPLICATION_MODAL);
        mainStage.show();
    }

    public static Boolean CATEGORIA() throws IOException {
        FXMLLoader miCargador = new FXMLLoader(CargaVistas.class.getResource("../vista/AñadirCategoria.fxml"));
        Parent root = miCargador.load();
        Stage mainStage = new Stage();
        AñadirCategoriaController controller = miCargador.getController();
        mainStage.setScene(new Scene(root));
        mainStage.getIcons().add(new Image(CargaVistas.class.getResourceAsStream("/imagenes/logo-sin.png")));
        mainStage.setTitle("AÑDIR CATEGORIA");
        mainStage.setResizable(false);
        mainStage.initModality(Modality.APPLICATION_MODAL);
        mainStage.showAndWait();
        return controller.getOk();
    }

    public static Boolean MODIFICARGASTO(Charge c) throws IOException {
        FXMLLoader miCargador = new FXMLLoader(CargaVistas.class.getResource("../vista/ModificarGastos.fxml"));
        Parent root = miCargador.load();
        ModificarGastoController cont = miCargador.getController();
        cont.initGasto(c);
        Stage mainStage = new Stage();
        mainStage.setScene(new Scene(root));
        mainStage.getIcons().add(new Image(CargaVistas.class.getResourceAsStream("/imagenes/logo-sin.png")));
        mainStage.setTitle("MODIFICAR GASTO");
        mainStage.setResizable(false);
        mainStage.initModality(Modality.APPLICATION_MODAL);
        mainStage.showAndWait();
        return cont.getModificado();
    }

    public static void LOGIN() throws IOException {
        FXMLLoader miCargador = new FXMLLoader(CargaVistas.class.getResource("../vista/LogIn.fxml"));
        Parent root = miCargador.load();
        Stage mainStage = new Stage();
        mainStage.setScene(new Scene(root));
        mainStage.getIcons().add(new Image(CargaVistas.class.getResourceAsStream("/imagenes/logo-sin.png")));
        mainStage.setTitle("INICIO DE SESIÓN");
        mainStage.setResizable(false);
        mainStage.initModality(Modality.APPLICATION_MODAL);
        mainStage.show();
    }

    public static void REGISTRO() throws IOException {
        FXMLLoader miCargador = new FXMLLoader(CargaVistas.class.getResource("../vista/SignUp.fxml"));
        Parent root = miCargador.load();
        Stage mainStage = new Stage();
        mainStage.setScene(new Scene(root));
        mainStage.getIcons().add(new Image(CargaVistas.class.getResourceAsStream("/imagenes/logo-sin.png")));
        mainStage.setTitle("REGISTRAR-SE");
        mainStage.setResizable(false);
        mainStage.initModality(Modality.APPLICATION_MODAL);
        mainStage.show();
    }

}
