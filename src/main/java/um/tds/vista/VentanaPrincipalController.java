package um.tds.vista;

import javafx.fxml.FXML;

import javafx.beans.property.SimpleStringProperty;
import um.tds.modelo.CuentaCompartida;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import um.tds.controladores.ControladorGasto;
import um.tds.controladores.ControladorJefe;
import um.tds.controladores.GastoObserver;
import um.tds.modelo.Gasto;
import um.tds.modelo.Categoria;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import filtros.FiltroAnd;
import filtros.FiltroCategoria;
import filtros.FiltroMes;
import filtros.FiltroRangoFechas;

public class VentanaPrincipalController implements GastoObserver {

    @FXML private TableView<Gasto> tablaGastos;
    @FXML private TableColumn<Gasto, LocalDate> colFecha;
    @FXML private TableColumn<Gasto, String> colDesc;
    @FXML private TableColumn<Gasto, String> colCat;
    @FXML private TableColumn<Gasto, Double> colCant;
    @FXML private TableColumn<Gasto, String> colCuenta;
    @FXML private TableColumn<Gasto, String> colPagador;

    @FXML private MenuButton menuFiltroMeses;

    @FXML private ComboBox<Categoria> comboFiltroCategoria;
    @FXML private ListView<String> listaNotificaciones;
    @FXML private Label lblStatus;

    @FXML private ListView<Categoria> listaCategoriasPrincipal;
    @FXML private TabPane tabPanePrincipal;

    @FXML private DatePicker dpFiltroInicio;
    @FXML private DatePicker dpFiltroFin;

    private ControladorGasto controladorGasto = new ControladorGasto();

    @FXML
    public void initialize() {

        ControladorJefe.getInstancia().registrarObservador(this);

        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colCat.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCategoria().getNombre()));
        colCant.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        colCuenta.setCellValueFactory(cellData -> {

            Gasto gasto = cellData.getValue();
            String id = gasto.getIdCuentaCompartida();
            String nombreCSV = gasto.getNombreCuenta();

            if (id != null) {
                CuentaCompartida cuenta = ControladorJefe.getInstancia().obtenerCuentas().stream()
                        .filter(c -> c.getId().equals(id))
                        .findFirst()
                        .orElse(null);
                return new SimpleStringProperty(cuenta != null ? cuenta.getNombre() : "Cuenta no encontrada");
            }

            if (nombreCSV != null && !nombreCSV.isEmpty()) {
                return new SimpleStringProperty("Pendiente: " + nombreCSV);
            }

            return new SimpleStringProperty("Personal");
        });

        colPagador.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getPagador() == null ? "Yo" : c.getValue().getPagador()
        ));

        String[] nombresMeses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

        menuFiltroMeses.getItems().clear();
        for (int i = 0; i < 12; i++) {
            CheckMenuItem item = new CheckMenuItem(nombresMeses[i]);
            item.setOnAction(e -> filtrarGastos());
            menuFiltroMeses.getItems().add(item);
        }

        comboFiltroCategoria.setButtonCell(new ListCell<Categoria>() {
            @Override
            protected void updateItem(Categoria item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Todas");
                } else {
                    setText(item.getNombre());
                }
            }
        });

        actualizarGastos();

        comboFiltroCategoria.setOnAction(e -> filtrarGastos());

        if (dpFiltroInicio != null) {
            dpFiltroInicio.valueProperty().addListener((obs, oldVal, newVal) -> filtrarGastos());
        }
        if (dpFiltroFin != null) {
            dpFiltroFin.valueProperty().addListener((obs, oldVal, newVal) -> filtrarGastos());
        }

        configurarListaCategorias();

        if (tabPanePrincipal != null) {
            tabPanePrincipal.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
                if (newTab != null && "Notificaciones".equals(newTab.getText())) {
                    cargarNotificaciones();
                }
            });
        }
    }

    @Override
    public void actualizarGastos() {
        javafx.application.Platform.runLater(() -> {
            actualizarTabla();
            actualizarListaCategorias();
            actualizarFiltroCategorias();
            cargarNotificaciones();
        });
    }

    private void actualizarTabla() {
        tablaGastos.getItems().setAll(controladorGasto.obtenerGastos(null, null, null));
    }

    private void actualizarListaCategorias() {
        listaCategoriasPrincipal.getItems().setAll(ControladorJefe.getInstancia().obtenerCategorias());
    }

    private void actualizarFiltroCategorias() {
        Categoria seleccionada = comboFiltroCategoria.getValue();
        List<Categoria> categorias = ControladorJefe.getInstancia().obtenerCategorias();
        comboFiltroCategoria.getItems().setAll(categorias);
        if (seleccionada != null && categorias.contains(seleccionada)) {
            comboFiltroCategoria.setValue(seleccionada);
        }
    }

    private void cargarNotificaciones() {
        var listaReal = um.tds.servicios.ServicioNotificacion.getInstancia().mostrarNotificaciones();
        listaNotificaciones.getItems().clear();
        for (var notif : listaReal) {
            listaNotificaciones.getItems().add(notif.toString());
        }
    }

    private void configurarListaCategorias() {
        listaCategoriasPrincipal.setCellFactory(lv -> new ListCell<Categoria>() {
            @Override
            protected void updateItem(Categoria item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : item.getNombre());
            }
        });
    }

    @FXML
    private void handleNuevoGasto() {
        abrirVentana("/um/tds/vista/GastoFormView.fxml", "Registrar Gasto");
    }

    @FXML
    private void handleAbrirImportador() {
        abrirVentana("/um/tds/vista/ImportarGastosView.fxml", "Importar Gastos");
    }

    @FXML
    private void handleNuevaCategoria() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nueva Categoría");
        dialog.setHeaderText("Crear una nueva categoría de gasto");
        dialog.setContentText("Nombre:");
        dialog.showAndWait().ifPresent(nombre -> {
            if (!nombre.trim().isEmpty()) {
                ControladorJefe.getInstancia().registrarCategoria(nombre, nombre);

            }
        });
    }

    @FXML
    private void handleEliminarCategoria() {
        Categoria seleccionada = listaCategoriasPrincipal.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarError("Selecciona una categoría.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Eliminar " + seleccionada.getNombre() + "?");
        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            ControladorJefe.getInstancia().eliminarCategoria(seleccionada);
        }
    }

    @FXML
    private void handleEliminarGasto() {
        Gasto seleccionado = tablaGastos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Borrar gasto?");
        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            ControladorJefe.getInstancia().eliminarGasto(seleccionado);
        }
    }

    @FXML
    private void handleEditarGasto() {
        Gasto seleccionado = tablaGastos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/um/tds/vista/GastoFormView.fxml"));
            Parent root = loader.load();
            GastoFormController controller = loader.getController();
            controller.setGastoEditar(seleccionado);

            Stage stage = new Stage();
            stage.setTitle("Editar Gasto");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void abrirVentana(String fxmlPath, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("Error al abrir ventana.");
        }
    }

    @FXML
    private void handleLimpiarFiltros() {

        comboFiltroCategoria.setValue(null);

        dpFiltroInicio.setValue(null);
        dpFiltroFin.setValue(null);

        for (MenuItem item : menuFiltroMeses.getItems()) {
            if (item instanceof CheckMenuItem) {
                ((CheckMenuItem) item).setSelected(false);
            }
        }

        filtrarGastos();
    }

    @FXML

    private void filtrarGastos() {

        FiltroAnd filtroGlobal = new FiltroAnd();

        if (comboFiltroCategoria.getValue() != null) {
            filtroGlobal.añadirFiltro(new FiltroCategoria(comboFiltroCategoria.getValue()));
        }

        if (dpFiltroInicio.getValue() != null || dpFiltroFin.getValue() != null) {
            filtroGlobal.añadirFiltro(new FiltroRangoFechas(dpFiltroInicio.getValue(), dpFiltroFin.getValue()));
        }

        List<Integer> mesesSeleccionados = new java.util.ArrayList<>();
        for (int i = 0; i < menuFiltroMeses.getItems().size(); i++) {
            CheckMenuItem item = (CheckMenuItem) menuFiltroMeses.getItems().get(i);
            if (item.isSelected()) {
                mesesSeleccionados.add(i + 1);
            }
        }
        if (!mesesSeleccionados.isEmpty()) {
            filtroGlobal.añadirFiltro(new FiltroMes(mesesSeleccionados));
        }

        List<Gasto> todos = ControladorJefe.getInstancia().obtenerGastos(null, null, null);

        List<Gasto> filtrados = todos.stream()
                .filter(g -> filtroGlobal.cumple(g))
                .collect(java.util.stream.Collectors.toList());

        tablaGastos.getItems().setAll(filtrados);
        lblStatus.setText("Filtros aplicados. Gastos encontrados: " + filtrados.size());
    }

    @FXML private void handleLimpiarNotificaciones() { listaNotificaciones.getItems().clear(); }
    @FXML private void handleSalir() { System.exit(0); }
    private void mostrarError(String msg) { new Alert(Alert.AlertType.ERROR, msg).showAndWait(); }

}