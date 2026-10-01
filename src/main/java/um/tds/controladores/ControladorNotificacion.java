package um.tds.controladores;

import java.util.List;

import um.tds.modelo.Notificacion;
import um.tds.servicios.ServicioNotificacion;

public class ControladorNotificacion {

    private final ServicioNotificacion servicioNotificaciones;

    public ControladorNotificacion() {
        this.servicioNotificaciones=ServicioNotificacion.getInstancia();
    }

    public List<Notificacion> mostrarNotificaciones() {
        return servicioNotificaciones.mostrarNotificaciones();
    }

}