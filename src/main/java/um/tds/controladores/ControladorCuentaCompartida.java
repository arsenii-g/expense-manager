package um.tds.controladores;

import java.util.List;

import java.util.Map;

import um.tds.modelo.CuentaCompartida;
import um.tds.modelo.PersonaCuenta;
import um.tds.servicios.ServicioCuentaCompartida;

public class ControladorCuentaCompartida {
    private ServicioCuentaCompartida servicioCuentaCompartida;

    public ControladorCuentaCompartida() {
       this.servicioCuentaCompartida=ServicioCuentaCompartida.getInstancia();
    }

    public void crearCuentaCompartida(String nombre, List<String> participantes) {
        servicioCuentaCompartida.crearCuentaCompartida(nombre, participantes);
    }

    public void crearCuentaCompartida(String nombre, Map<String, Double> participantes) {
        servicioCuentaCompartida.crearCuentaCompartida(nombre, participantes);
    }

    public List<PersonaCuenta> mostrarSaldosPendientes(String idCuenta) {
        return servicioCuentaCompartida.mostrarSaldosPendientes(idCuenta);
    }

    public List<CuentaCompartida> obtenerCuentasCompartidas() {
        return servicioCuentaCompartida.mostrarCuentasCompartidas();
    }

	public void recalcularSaldos(CuentaCompartida cuenta) {
		servicioCuentaCompartida.recalcularSaldos(cuenta);
	}
}
