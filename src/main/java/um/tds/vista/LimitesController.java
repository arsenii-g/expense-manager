package um.tds.vista;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import um.tds.controladores.ControladorJefe;
import um.tds.modelo.Alerta;
import um.tds.modelo.Categoria;
import um.tds.modelo.TipoAlerta;

public class LimitesController {

    @FXML private TextField txtLimite;
    @FXML private ComboBox<Categoria> comboCategoria;
    @FXML private ComboBox<TipoAlerta> comboPeriodo;
    @FXML private Button btnCrearLimite;
    @FXML private TableView<Alerta> tablaLimites;

    @FXML private TableColumn<Alerta, String> colCategoria;
    @FXML private TableColumn<Alerta, String> colPeriodo;
    @FXML private TableColumn<Alerta, Double> colLimite;
    @FXML private TableColumn<Alerta, String> colActivo;

    @FXML
    public void initialize() {
        comboPeriodo.getItems().setAll(TipoAlerta.values());
        comboCategoria.getItems().setAll(ControladorJefe.getInstancia().obtenerCategorias());

        comboCategoria.setConverter(new javafx.util.StringConverter<>() {
            @Override public String toString(Categoria c) { return c != null ? c.getNombre() : ""; }
            @Override public Categoria fromString(String s) { return null; }
        });

        colCategoria.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCategoria().getNombre()));
        colPeriodo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTipoEstrategia().toString()));
        colLimite.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("limite"));
        colActivo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().isSuperado() ? "SUPERADO" : "OK"));

        actualizarTabla();

        btnCrearLimite.setOnAction(e -> {
            try {
                double limite = Double.parseDouble(txtLimite.getText());
                Categoria cat = comboCategoria.getValue();
                TipoAlerta tipo = comboPeriodo.getValue();

                if (cat != null && tipo != null) {
                    ControladorJefe.getInstancia().crearAlerta(limite, cat, tipo);
                    actualizarTabla();
                    txtLimite.clear();
                }
            } catch (NumberFormatException ex) {
            }
        });
    }

    private void actualizarTabla() {
        tablaLimites.getItems().setAll(ControladorJefe.getInstancia().obtenerAlertas());
    }
}