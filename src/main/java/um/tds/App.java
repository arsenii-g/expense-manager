package um.tds;

import javafx.application.Application;
import um.tds.vista.InterfazConsola;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        try {

        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/um/tds/vista/VentanaPrincipal.fxml"));
        	Parent root = loader.load();

            Scene scene = new Scene(root, 1000, 700);

            stage.setTitle("Gestión de Gastos");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al cargar el archivo FXML: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
    	if (args.length > 0 && args[0].equalsIgnoreCase("-console")) {

            try {
            	new InterfazConsola().iniciar();
            } catch(Exception e) {
            	e.printStackTrace();
            }
            System.exit(0);
        } else {

            launch(args);
        }

    }
}
