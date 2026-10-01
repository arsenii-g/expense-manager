package um.tds.vista;

import javafx.scene.control.Label;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import um.tds.controladores.ControladorJefe;
import um.tds.modelo.Gasto;

public class EstadisticasController {
	@FXML private PieChart grafico;
	@FXML private Label lbltotal;

	@FXML
	public void initialize() {
		cargarDatos();
	}

	public void cargarDatos() {
	    List<Gasto> todosLosGastos = ControladorJefe.getInstancia().obtenerGastos(null, null, null);

	    List<Gasto> misGastos = todosLosGastos.stream()
	            .filter(g -> g.getIdCuentaCompartida() == null || "Yo".equals(g.getPagador()))
	            .collect(Collectors.toList());

	    Map<String, Double> gastosPorCategoria = misGastos.stream()
	            .collect(Collectors.groupingBy(
	                g -> g.getCategoria().getNombre(),
	                Collectors.summingDouble(Gasto::getCantidad)
	            ));

	    ObservableList<PieChart.Data> datosGrafico = FXCollections.observableArrayList();
	    double totalGeneral = 0.0;

	    for (Map.Entry<String, Double> entry : gastosPorCategoria.entrySet()) {
	        if (entry.getValue() > 0) {
	            datosGrafico.add(new PieChart.Data(entry.getKey(), entry.getValue()));
	            totalGeneral += entry.getValue();
	        }
	    }

	    grafico.setData(datosGrafico);
	    grafico.setTitle("Mis Gastos por Categoría");

	    lbltotal.setText(String.format("Mi Gasto Total: %.2f €", totalGeneral));
	}
}

