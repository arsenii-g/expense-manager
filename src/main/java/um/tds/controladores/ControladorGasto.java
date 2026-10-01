package um.tds.controladores;

import java.time.LocalDate;

import java.util.List;

import um.tds.modelo.Categoria;
import um.tds.modelo.CuentaCompartida;
import um.tds.modelo.Gasto;
import um.tds.servicios.ServicioGasto;

public class ControladorGasto {
    private final ServicioGasto servicioGasto;

    public ControladorGasto() {
       this.servicioGasto=ServicioGasto.getInstancia();
    }

    public void crearGasto(LocalDate fecha, double cantidad, Categoria categoria, String d) {
        servicioGasto.crearGasto(fecha, cantidad, categoria, d);
    }

    public void crearGastoCompartido(LocalDate fecha, double cantidad, Categoria cat, String desc,
            CuentaCompartida cuenta, String nombrePagador) {
	Gasto nuevoGasto = new Gasto(fecha, cantidad, cat, desc);

	nuevoGasto.setIdCuentaCompartida(cuenta.getId());
	nuevoGasto.setPagador(nombrePagador);

	servicioGasto.crearGasto(nuevoGasto);
	}

    public List<Gasto> obtenerGastos(Categoria categoria, LocalDate inicio, LocalDate fin) {
        return servicioGasto.obtenerGastos(categoria, inicio, fin);
    }
    public void actualizarGasto(Gasto gasto) {
        servicioGasto.actualizarGasto(gasto);
    }

	public void importarGastos(String formato, String ruta) {
		servicioGasto.importarGastos(formato, ruta);

	}

}
