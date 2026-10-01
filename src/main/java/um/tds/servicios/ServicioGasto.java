package um.tds.servicios;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import um.tds.importadores.ImportadorFactoria;
import um.tds.importadores.ServicioImportador;
import um.tds.modelo.Categoria;
import um.tds.modelo.CuentaCompartida;
import um.tds.modelo.Gasto;
import um.tds.repositorios.Factoria;
import um.tds.repositorios.RepositorioGasto;

public class ServicioGasto {

    private static ServicioGasto instancia;

    private RepositorioGasto repositorioGasto;
    private ServicioCuentaCompartida servicioCuentaCompartida;
    private ServicioAlerta servicioAlerta;

    private ServicioGasto() {
        try {

            this.repositorioGasto = Factoria.getInstancia().getRepositorioGasto();

            this.setServicioCuentaCompartida(ServicioCuentaCompartida.getInstancia());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized ServicioGasto getInstancia() {
        if (instancia == null) {
            instancia = new ServicioGasto();
        }
        return instancia;
    }

    public void setServicioAlerta(ServicioAlerta servicioAlerta) {
        this.servicioAlerta = servicioAlerta;
    }

    public void crearGasto(LocalDate fecha, double cantidad, Categoria categoria) {
        Gasto gasto = new Gasto(fecha, cantidad, categoria);
        crearGasto(gasto);
    }

    public void crearGasto(LocalDate fecha, double cantidad, Categoria categoria, String d) {
        Gasto gasto = new Gasto(fecha, cantidad, categoria, d);
        crearGasto(gasto);
    }

    public void crearGasto(
            LocalDate fecha,
            double cantidad,
            Categoria categoria,
            String descripcion,
            CuentaCompartida cuenta,
            String nombrePagador
    ) {
        Gasto gasto = new Gasto(fecha, cantidad, categoria, descripcion);
        gasto.setIdCuentaCompartida(cuenta.getId());
        gasto.setPagador(nombrePagador);

        crearGasto(gasto);
    }

    public void crearGasto(Gasto gasto) {
        repositorioGasto.guardar(gasto);

        if (gasto.getIdCuentaCompartida() != null) {
            ServicioCuentaCompartida.getInstancia().registrarGasto(gasto);
        }

        if(servicioAlerta != null) {
            servicioAlerta.verificarAlertas();
        }
    }

    public void importarGastos(String formato, String ruta) {

        ServicioImportador importador = ImportadorFactoria.crearImportador(formato);

        List<Gasto> gastos = importador.importar(ruta);

        for (Gasto gasto : gastos) {

            if (gasto.getNombreCuenta() != null) {

                Optional<CuentaCompartida> cuentaExistente = ServicioCuentaCompartida.getInstancia()
                        .buscarCuentaPorNombre(gasto.getNombreCuenta());

                if (cuentaExistente.isPresent()) {

                    gasto.setIdCuentaCompartida(cuentaExistente.get().getId());
                }
            }

            this.crearGasto(gasto);
        }
    }

    public void actualizarGasto(Gasto gasto) {

        repositorioGasto.actualizar(gasto);

        if(servicioAlerta != null) {
            servicioAlerta.verificarAlertas();
        }
    }

    public void eliminarGasto(Gasto gasto) {
        repositorioGasto.eliminar(gasto);

        if(servicioAlerta != null) {
            servicioAlerta.verificarAlertas();
        }
    }

    public List<Gasto> obtenerGastos(Categoria categoria, LocalDate inicio, LocalDate fin) {
        List<Gasto> todos = repositorioGasto.obtenerTodos();

        return todos.stream()

            .filter(g -> categoria == null || g.getCategoria().getNombre().equals(categoria.getNombre()))
            .filter(g -> inicio == null || !g.getFecha().isBefore(inicio))
            .filter(g -> fin == null || !g.getFecha().isAfter(fin))
            .collect(Collectors.toList());
    }

	public ServicioCuentaCompartida getServicioCuentaCompartida() {
		return servicioCuentaCompartida;
	}

	public void setServicioCuentaCompartida(ServicioCuentaCompartida servicioCuentaCompartida) {
		this.servicioCuentaCompartida = servicioCuentaCompartida;
	}
}
