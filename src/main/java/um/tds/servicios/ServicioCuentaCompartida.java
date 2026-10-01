package um.tds.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import um.tds.modelo.CuentaCompartida;
import um.tds.modelo.Gasto;
import um.tds.modelo.PersonaCuenta;
import um.tds.repositorios.Factoria;
import um.tds.repositorios.RepositorioCuentaCompartida;

public class ServicioCuentaCompartida {

    private static ServicioCuentaCompartida instancia;
    private RepositorioCuentaCompartida repositorio;

    private ServicioCuentaCompartida() {
        try {
            this.repositorio = Factoria.getInstancia().getRepositorioCuentaCompartida();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized ServicioCuentaCompartida getInstancia() {
        if (instancia == null) {
            instancia = new ServicioCuentaCompartida();
        }
        return instancia;
    }

    public void crearCuentaCompartida(String nombre, List<String> nombresMiembros) {
        List<PersonaCuenta> miembros = new ArrayList<>();
        for (String nombreMiembro : nombresMiembros) {
            miembros.add(new PersonaCuenta(nombreMiembro, 100.0 / nombresMiembros.size()));
        }

        CuentaCompartida cuenta = new CuentaCompartida(nombre, miembros, false);
        repositorio.guardar(cuenta);
        procesarHuerfanos(cuenta);
    }

    public void crearCuentaCompartida(String nombre, Map<String, Double> miembrosConPorcentaje) {
        List<PersonaCuenta> miembros = new ArrayList<>();
        for (var entry : miembrosConPorcentaje.entrySet()) {
            miembros.add(new PersonaCuenta(entry.getKey(), entry.getValue()));
        }

        CuentaCompartida cuenta = new CuentaCompartida(nombre, miembros, true);
        repositorio.guardar(cuenta);
        procesarHuerfanos(cuenta);
    }

    private void procesarHuerfanos(CuentaCompartida cuenta) {
        List<Gasto> huerfanos = ServicioGasto.getInstancia().obtenerGastos(null, null, null).stream()
                .filter(g -> g.getIdCuentaCompartida() == null)
                .filter(g -> cuenta.getNombre().equalsIgnoreCase(g.getNombreCuenta()))
                .collect(Collectors.toList());

        for (Gasto g : huerfanos) {
            vincularGastoACuenta(g, cuenta);
        }
    }

    public void vincularGastoACuentaPorNombre(Gasto gasto, String nombreCuenta) {
        repositorio.obtenerTodas().stream()
                .filter(c -> c.getNombre().equalsIgnoreCase(nombreCuenta))
                .findFirst()
                .ifPresent(cuenta -> vincularGastoACuenta(gasto, cuenta));
    }

    private void vincularGastoACuenta(Gasto gasto, CuentaCompartida cuenta) {
        gasto.setIdCuentaCompartida(cuenta.getId());
        ServicioGasto.getInstancia().actualizarGasto(gasto);
        this.registrarGasto(gasto);
    }

    public void registrarGasto(Gasto gasto) {
        CuentaCompartida cuenta = repositorio.obtenerPorId(gasto.getIdCuentaCompartida())
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));

        PersonaCuenta pagador = buscarMiembro(cuenta, gasto.getPagador());
        cuenta.calcularSaldos(gasto.getCantidad(), pagador);
        cuenta.agregarGasto(gasto);
        repositorio.actualizar(cuenta);
    }

    public Optional<CuentaCompartida> buscarCuentaPorNombre(String nombre) {
        return repositorio.obtenerTodas().stream()
                .filter(c -> c.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
    }

    public List<CuentaCompartida> mostrarCuentasCompartidas() {
        return repositorio.obtenerTodas();
    }

    public List<PersonaCuenta> mostrarSaldosPendientes(String idCuenta) {
        CuentaCompartida cuenta = repositorio.obtenerPorId(idCuenta)
                .orElseThrow(() -> new IllegalArgumentException("No existe una cuenta con id: " + idCuenta));
        return cuenta.getMiembros();
    }

    private PersonaCuenta buscarMiembro(CuentaCompartida cuenta, String nombrePagador) {
        return cuenta.getMiembros().stream()
                .filter(m -> m.getNombre().equalsIgnoreCase(nombrePagador))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("La persona " + nombrePagador + " no pertenece a la cuenta"));
    }

    public void recalcularSaldos(CuentaCompartida cuenta) {
        if (cuenta == null) return;

        for (PersonaCuenta p : cuenta.getMiembros()) {
            p.setSaldo(0.0);
        }

        List<Gasto> gastosDeCuenta = ServicioGasto.getInstancia()
                .obtenerGastos(null, null, null).stream()
                .filter(g -> cuenta.getId().equals(g.getIdCuentaCompartida()))
                .collect(Collectors.toList());

        cuenta.setGastos(gastosDeCuenta);

        for (Gasto g : gastosDeCuenta) {
            PersonaCuenta pagador = cuenta.getMiembros().stream()
                    .filter(m -> m.getNombre().equalsIgnoreCase(g.getPagador()))
                    .findFirst()
                    .orElse(null);

            if (pagador != null) {
                cuenta.calcularSaldos(g.getCantidad(), pagador);
            }
        }

        repositorio.actualizar(cuenta);
    }
}