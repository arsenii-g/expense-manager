package um.tds.servicios;

import java.util.List;
import java.util.ArrayList;

import um.tds.modelo.Alerta;
import um.tds.modelo.Categoria;
import um.tds.modelo.TipoAlerta;
import um.tds.modelo.Gasto;
import um.tds.repositorios.Factoria;
import um.tds.repositorios.RepositorioAlerta;

public class ServicioAlerta {

    private static ServicioAlerta instancia;

    private  RepositorioAlerta repositorioAlerta;
    private  ServicioGasto servicioGastos;
    private  ServicioNotificacion servicioNotificaciones;

    private ServicioAlerta() {
        try {

            this.repositorioAlerta = Factoria.getInstancia().getRepositorioAlerta();

            this.servicioNotificaciones = ServicioNotificacion.getInstancia();
            this.servicioGastos = ServicioGasto.getInstancia();

            if (this.servicioGastos != null) {
                this.servicioGastos.setServicioAlerta(this);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized ServicioAlerta getInstancia() {
        if (instancia == null) {
            instancia = new ServicioAlerta();
        }
        return instancia;
    }

    public void crearAlerta(double limite, Categoria categoria, TipoAlerta tipoAlerta) {
        Alerta alerta = new Alerta(limite, categoria, tipoAlerta);
        repositorioAlerta.guardar(alerta);
    }

    public void eliminarAlerta(String id) {
        repositorioAlerta.obtenerPorId(id).ifPresent(repositorioAlerta::eliminar);
    }

    public Alerta obtenerPorId(String id) {
        return repositorioAlerta.obtenerPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la alerta con id: " + id));
    }

    public List<Alerta> obtenerAlertas() {
        return new ArrayList<>(repositorioAlerta.obtenerTodas());
    }

    public void verificarAlertas() {
        List<Alerta> todas = obtenerAlertas();

        for (Alerta alerta : todas) {

            if (!alerta.isActivo()) {
                continue;
            }
            List<Gasto> todosLosGastos = servicioGastos.obtenerGastos(alerta.getCategoria(), null, null);

            List<Gasto> gastosPersonales = todosLosGastos.stream()
                    .filter(g -> g.getIdCuentaCompartida() == null)
                    .collect(java.util.stream.Collectors.toList());

            boolean superado = alerta.getEstrategia().comprobarLimites(gastosPersonales, alerta);

            if (superado) {
                alerta.setSuperado(true);
                servicioNotificaciones.crearNotificacion(alerta);
            } else {
                alerta.setSuperado(false);
            }
        }
    }
}

