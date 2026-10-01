package um.tds.vista;

import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import um.tds.controladores.ControladorJefe;
import um.tds.modelo.Categoria;
import um.tds.modelo.Gasto;

public class VentanaCrearGastoController {

    @FXML private TextField txtDescripcion;
    @FXML private TextField txtCantidad;
    @FXML private DatePicker dateFecha;
    @FXML private ComboBox<Categoria> comboCategoria;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    private boolean guardado = false;

    public boolean isGuardado() {
        return guardado;
    }

    @FXML
    public void initialize() {

        dateFecha.setValue(LocalDate.now());

        var categorias = ControladorJefe.getInstancia().obtenerCategorias();
        comboCategoria.getItems().addAll(categorias);

        comboCategoria.setConverter(new StringConverter<Categoria>() {
            @Override
            public String toString(Categoria c) {
                return (c != null) ? c.getNombre() : "";
            }
            @Override
            public Categoria fromString(String string) {
                return null;
            }
        });

        btnCancelar.setOnAction(e -> cerrarVentana());
        btnGuardar.setOnAction(e -> guardarGasto());
    }

    private void guardarGasto() {
        String descripcion = txtDescripcion.getText();
        String cantStr = txtCantidad.getText();
        LocalDate fecha = dateFecha.getValue();
        Categoria cat = comboCategoria.getValue();

        if (cantStr == null || cantStr.trim().isEmpty() || fecha == null || cat == null) {
            mostrarAlerta("Faltan datos obligatorios (Cantidad, Fecha o Categoría).");
            return;
        }

        try {
            double cantidad = Double.parseDouble(cantStr.replace(",", "."));

            if (descripcion == null) descripcion = "";

            ControladorJefe.getInstancia().registrarGasto(fecha, cantidad, cat, descripcion);

            this.guardado = true;

            cerrarVentana();

        } catch (NumberFormatException e) {
            mostrarAlerta("La cantidad debe ser un número.");
        }
    }

    private void cerrarVentana() {

        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public void setGastoEditar(Gasto gasto) {
        if (gasto == null) return;

       txtDescripcion.setText(gasto.getDescripcion() != null ? gasto.getDescripcion() : "");

        txtCantidad.setText(String.valueOf(gasto.getCantidad()));
        dateFecha.setValue(gasto.getFecha());

        for (Categoria c : comboCategoria.getItems()) {
            if (c.getNombre().equals(gasto.getCategoria().getNombre())) {
                comboCategoria.setValue(c);
                break;
            }
        }

        btnGuardar.setText("Actualizar Gasto");
    }
}

