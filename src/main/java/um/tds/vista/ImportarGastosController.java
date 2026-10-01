package um.tds.vista;

import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.stage.FileChooser;
import um.tds.controladores.ControladorJefe;

import java.io.File;

public class ImportarGastosController {

    @FXML private ComboBox<String> comboFormato;
    @FXML private TextField txtRuta;
    @FXML private Button btnExaminar;
    @FXML private Button btnImportar;

    @FXML
    public void initialize() {
        comboFormato.getItems().addAll("CSV", "JSON");
        comboFormato.setValue("CSV");

        btnExaminar.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleccionar archivo de gastos");
            File file = fileChooser.showOpenDialog(btnExaminar.getScene().getWindow());
            if (file != null) {
                txtRuta.setText(file.getAbsolutePath());
            }
        });

        btnImportar.setOnAction(e -> {
            String ruta = txtRuta.getText();
            String formato = comboFormato.getValue();
            if (ruta == null || ruta.isEmpty()) return;

            try {

                ControladorJefe.getInstancia().importarGastos(formato, ruta);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Importación completada con éxito.");
                alert.showAndWait();

            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error al importar: " + ex.getMessage());
                alert.showAndWait();
                ex.printStackTrace();
            }
        });
    }
}