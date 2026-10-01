package um.tds.vista;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import um.tds.controladores.ControladorGasto;
import um.tds.controladores.ControladorJefe;
import um.tds.modelo.Categoria;
import um.tds.modelo.CuentaCompartida;
import um.tds.modelo.Gasto;
import um.tds.modelo.PersonaCuenta;
import java.time.LocalDate;

public class GastoFormController {

    @FXML private DatePicker dpFecha;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtCantidad;
    @FXML private ComboBox<Categoria> comboCategoria;
    @FXML private ComboBox<CuentaCompartida> comboCuenta;
    @FXML private ComboBox<PersonaCuenta> comboPagador;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;
	@FXML private ControladorGasto controladorGasto = new ControladorGasto();
	private Gasto gastoEnEdicion;

    @FXML
    public void initialize() {
        dpFecha.setValue(LocalDate.now());

        comboCategoria.getItems().setAll(ControladorJefe.getInstancia().obtenerCategorias());
        comboCategoria.setConverter(new StringConverter<>() {
            @Override public String toString(Categoria c) { return c != null ? c.getNombre() : ""; }
            @Override public Categoria fromString(String s) { return null; }
        });

        comboCuenta.getItems().setAll(ControladorJefe.getInstancia().obtenerCuentas());
        comboCuenta.setConverter(new StringConverter<>() {
            @Override public String toString(CuentaCompartida c) { return c != null ? c.getNombre() : ""; }
            @Override public CuentaCompartida fromString(String s) { return null; }
        });

        comboCuenta.setOnAction(e -> {
            CuentaCompartida seleccionada = comboCuenta.getValue();
            if (seleccionada != null) {
                comboPagador.getItems().setAll(seleccionada.getMiembros());
                comboPagador.setDisable(false);
            } else {
                comboPagador.getItems().clear();
                comboPagador.setDisable(true);
            }
        });

        comboPagador.setConverter(new StringConverter<>() {
            @Override public String toString(PersonaCuenta p) { return p != null ? p.getNombre() : ""; }
            @Override public PersonaCuenta fromString(String s) { return null; }
        });

        comboPagador.setDisable(true);

        btnGuardar.setOnAction(e -> guardar());
        btnCancelar.setOnAction(e -> ((Stage) btnCancelar.getScene().getWindow()).close());
    }

    private void guardar() {
        try {
            LocalDate fecha = dpFecha.getValue();
            String desc = txtDescripcion.getText();
            Categoria cat = comboCategoria.getValue();

            if (txtCantidad.getText().isEmpty() || cat == null || fecha == null) {
                mostrarError("Por favor, rellena todos los campos obligatorios.");
                return;
            }

            double cantidad = Double.parseDouble(txtCantidad.getText());

            if (gastoEnEdicion == null) {
                CuentaCompartida cuenta = comboCuenta.getValue();
                PersonaCuenta pagador = comboPagador.getValue();

                if (cuenta != null && pagador != null) {
                    ControladorJefe.getInstancia().crearGastoCompartido(
                        fecha, cantidad, cat, desc, cuenta, pagador.getNombre()
                    );
                } else {
                    ControladorJefe.getInstancia().registrarGasto(fecha, cantidad, cat, desc);
                }
            } else {
                gastoEnEdicion.setFecha(fecha);
                gastoEnEdicion.setDescripcion(desc);
                gastoEnEdicion.setCantidad(cantidad);
                gastoEnEdicion.setCategoria(cat);

                ControladorJefe.getInstancia().actualizarGasto(gastoEnEdicion);
            }

            ((Stage) btnGuardar.getScene().getWindow()).close();

        } catch (NumberFormatException e) {
            mostrarError("La cantidad debe ser un número válido (ej: 10.5).");
        }
    }

    private void mostrarError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.showAndWait();
    }

    public void setGastoEditar(Gasto gasto) {
        this.gastoEnEdicion = gasto;

        txtDescripcion.setText(gasto.getDescripcion());
        txtCantidad.setText(String.valueOf(gasto.getCantidad()));
        dpFecha.setValue(gasto.getFecha());
        comboCategoria.setValue(gasto.getCategoria());

        if (gasto.getIdCuentaCompartida() != null) {
             comboCuenta.setDisable(true);
        }

        btnGuardar.setText("Actualizar Gasto");
    }

}