package um.tds.repositorios;

import java.util.List;
import java.util.Optional;
import um.tds.modelo.Notificacion;

public interface RepositorioNotificacion  {
    void guardar(Notificacion notificacion);
    void eliminar(Notificacion notificacion);
    List<Notificacion> obtenerTodas();
    Optional<Notificacion> obtenerPorId(String id);
}