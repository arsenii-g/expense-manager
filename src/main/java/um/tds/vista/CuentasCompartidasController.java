package um.tds.vista;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import um.tds.controladores.ControladorJefe;
import um.tds.modelo.CuentaCompartida;
import um.tds.modelo.PersonaCuenta;

import java.io.IOException;
import java.util.stream.Collectors;

public class CuentasCompartidasController {

    @FXML private TableView<CuentaCompartida> tablaCuentas;
    @FXML private TableColumn<CuentaCompartida, String> colNombre;
    @FXML private TableColumn<CuentaCompartida, String> colParticipantes;
    @FXML private Button btnNuevaCuenta;
    @FXML private Button btnVerDetalle;

    @FXML
    public void initialize() {

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        colParticipantes.setCellValueFactory(cellData -> {
            String nombres = cellData.getValue().getMiembros().stream()
                    .map(PersonaCuenta::getNombre)
                    .collect(Collectors.joining(", "));
            return new javafx.beans.property.SimpleStringProperty(nombres);
        });

        actualizarTabla();

        btnNuevaCuenta.setOnAction(e -> mostrarDialogoNuevaCuenta());
        btnVerDetalle.setOnAction(e -> verDetalleCuenta());
    }

    private void actualizarTabla() {
        tablaCuentas.getItems().setAll(ControladorJefe.getInstancia().obtenerCuentas());
    }

    private void verDetalleCuenta() {
        CuentaCompartida seleccionada = tablaCuentas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CuentaCompartidaDetalleView.fxml"));
            Parent root = loader.load();

            CuentaCompartidaDetalleController controller = loader.getController();
            controller.setCuenta(seleccionada);

            Stage stage = new Stage();
            stage.setTitle("Detalle de Cuenta: " + seleccionada.getNombre());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarDialogoNuevaCuenta() {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nueva Cuenta Compartida");
        dialog.setHeaderText("Crear cuenta (Distribución equitativa)");
        dialog.setContentText("Nombre de la cuenta:");

        dialog.showAndWait().ifPresent(nombre -> {
             TextInputDialog partsDialog = new TextInputDialog();
             partsDialog.setTitle("Participantes");
             partsDialog.setHeaderText("Introduce los nombres separados por comas");
             partsDialog.setContentText("Nombres:");

             partsDialog.showAndWait().ifPresent(nombresStr -> {
            	    var listaNombres = java.util.Arrays.stream(nombresStr.split(","))
            	                                       .map(String::trim)
            	                                       .filter(s -> !s.isEmpty())
            	                                       .collect(Collectors.toList());

            	    ControladorJefe.getInstancia().crearCuentaCompartida(nombre, listaNombres);
            	    actualizarTabla();
            	});
        });
    }
}