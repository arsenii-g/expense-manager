package um.tds.servicios;

import java.util.List;
import java.util.UUID;
import java.time.LocalDate;

import um.tds.modelo.Notificacion;
import um.tds.modelo.Alerta;
import um.tds.repositorios.Factoria;
import um.tds.repositorios.RepositorioNotificacion;

public class ServicioNotificacion {

    private static ServicioNotificacion instancia;

    private  RepositorioNotificacion repositorio;

    private ServicioNotificacion() {
        try {

            this.repositorio = Factoria.getInstancia().getRepositorioNotificacion();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized ServicioNotificacion getInstancia() {
        if (instancia == null) {
            instancia = new ServicioNotificacion();
        }
        return instancia;
    }

    public List<Notificacion> mostrarNotificaciones() {
        return repositorio.obtenerTodas();
    }

    public void crearNotificacion(Alerta alerta) {
        if (alerta == null) return;

        Notificacion notificacion = new Notificacion(
            UUID.randomUUID().toString(),
            LocalDate.now(),
            "Alerta superada: " + alerta.getCategoria().getNombre()
                + " - límite: " + alerta.getLimite(),
            false,
            alerta.getId()
        );

        repositorio.guardar(notificacion);
    }
}
