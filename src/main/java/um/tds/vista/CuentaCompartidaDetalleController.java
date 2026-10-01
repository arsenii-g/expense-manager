package um.tds.vista;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import um.tds.controladores.ControladorJefe;
import um.tds.modelo.CuentaCompartida;
import um.tds.modelo.Gasto;
import um.tds.modelo.PersonaCuenta;

public class CuentaCompartidaDetalleController {

    @FXML private Label lblNombreCuenta;
    @FXML private TableView<PersonaCuenta> tablaSaldos;
    @FXML private TableColumn<PersonaCuenta, String> colPersona;
    @FXML private TableColumn<PersonaCuenta, Double> colPorcentaje;
    @FXML private TableColumn<PersonaCuenta, Double> colSaldo;

    @FXML private TableView<Gasto> tablaGastosCuenta;
    @FXML private TableColumn<Gasto, String> colFecha;
    @FXML private TableColumn<Gasto, String> colDescripcion;
    @FXML private TableColumn<Gasto, Double> colCantidad;
    @FXML private TableColumn<Gasto, String> colPagador;

    @FXML private Button btnCalcularSaldos;

    private CuentaCompartida cuenta;

    public void setCuenta(CuentaCompartida cuenta) {
        this.cuenta = cuenta;
        lblNombreCuenta.setText(cuenta.getNombre());
        cargarDatos();
    }

    @FXML
    public void initialize() {

        colPersona.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombre()));
        colPorcentaje.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().getPorcentaje()));
        colSaldo.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().getSaldo()));

        colSaldo.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(String.format("%.2f €", item));
                    if (item < 0) setStyle("-fx-text-fill: red;");
                    else if (item > 0) setStyle("-fx-text-fill: green;");
                    else setStyle("-fx-text-fill: black;");
                }
            }
        });

        colFecha.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getFecha().toString()));
        colDescripcion.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDescripcion()));
        colCantidad.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().getCantidad()));

        colPagador.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getPagador()));

        btnCalcularSaldos.setOnAction(e -> ejecutarRecalculo());
    }

    private void cargarDatos() {
        if (cuenta != null) {
            tablaSaldos.getItems().setAll(cuenta.getMiembros());
            tablaGastosCuenta.getItems().setAll(cuenta.getGastos());
        }
    }

    private void ejecutarRecalculo() {
        if (cuenta != null) {

            ControladorJefe.getInstancia().recalcularSaldosCuenta(cuenta);

            cargarDatos();

        }
    }
}