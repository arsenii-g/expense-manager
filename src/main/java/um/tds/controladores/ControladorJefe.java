package um.tds.controladores;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import um.tds.modelo.Alerta;
import um.tds.modelo.Categoria;
import um.tds.modelo.CuentaCompartida;
import um.tds.modelo.Gasto;
import um.tds.modelo.Notificacion;
import um.tds.modelo.TipoAlerta;
import um.tds.repositorios.AdaptadorCategoriaJSON;
import um.tds.repositorios.AdaptadorGastoJSON;

public class ControladorJefe {

    private static ControladorJefe unicaInstancia;

    private ControladorCategoria controladorCategoria;
    private ControladorGasto controladorGasto;
    private ControladorAlerta controladorAlerta;
    private ControladorCuentaCompartida controladorCuenta;
    private ControladorNotificacion controladorNotificacion;
    private List<GastoObserver> observadores = new ArrayList<>();

    private ControladorJefe() {
        this.controladorCategoria = new ControladorCategoria();
        this.controladorGasto = new ControladorGasto();
        this.controladorAlerta = new ControladorAlerta();
        this.controladorCuenta = new ControladorCuentaCompartida();
        this.controladorNotificacion = new ControladorNotificacion();
    }

    public static synchronized ControladorJefe getInstancia() {
        if (unicaInstancia == null) {
            unicaInstancia = new ControladorJefe();
        }
        return unicaInstancia;
    }

    public void registrarObservador(GastoObserver observador) {
        observadores.add(observador);
    }

    public void notificarCambio() {
        for (GastoObserver obs : observadores) {
            obs.actualizarGastos();
        }
    }

    public void registrarCategoria(String nombre, String descripcion) {
        controladorCategoria.crearCategoria(nombre, descripcion);
        notificarCambio();
    }

    public List<Categoria> obtenerCategorias() {
        return controladorCategoria.obtenerCategorias();
    }

    public void eliminarCategoria(Categoria c) {
        AdaptadorCategoriaJSON.getInstance().eliminar(c.getId());
        notificarCambio();
    }

    public void registrarGasto(LocalDate fecha, double cantidad, Categoria cat, String des) {
        controladorGasto.crearGasto(fecha, cantidad, cat, des);
        notificarCambio();
    }

    public List<Gasto> obtenerGastos(Categoria cat, LocalDate ini, LocalDate fin) {
        return controladorGasto.obtenerGastos(cat, ini, fin);
    }

    public void eliminarGasto(Gasto g) {
        AdaptadorGastoJSON.getInstancia().eliminar(g);
        notificarCambio();
    }

    public void actualizarGasto(Gasto gasto) {
        controladorGasto.actualizarGasto(gasto);
        notificarCambio();
    }

    public void crearCuentaCompartida(String nombre, List<String> participantes) {
        controladorCuenta.crearCuentaCompartida(nombre, participantes);
        notificarCambio();
    }
    public void importarGastos(String formato, String ruta) {
        controladorGasto.importarGastos(formato, ruta);

        notificarCambio();
    }

    public List<CuentaCompartida> obtenerCuentas() {
        return controladorCuenta.obtenerCuentasCompartidas();
    }

    public void crearAlerta(double limite, Categoria cat, TipoAlerta tipo) {
        controladorAlerta.crearAlerta(limite, cat, tipo);
        notificarCambio();
    }

    public List<Alerta> obtenerAlertas() {
        return controladorAlerta.mostrarAlertas();
    }

    public List<Notificacion> obtenerNotificaciones() {
        return controladorNotificacion.mostrarNotificaciones();
    }

    public void recalcularSaldosCuenta(CuentaCompartida cuenta) {
        controladorCuenta.recalcularSaldos(cuenta);
        notificarCambio();
    }

    public void crearGastoCompartido(LocalDate fecha, double cantidad, Categoria cat, String desc,
            CuentaCompartida cuenta, String nombrePagador) {
	controladorGasto.crearGastoCompartido(fecha, cantidad, cat, desc, cuenta, nombrePagador);

	notificarCambio();
	}
}