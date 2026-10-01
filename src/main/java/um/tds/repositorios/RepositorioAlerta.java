package um.tds.repositorios;

import java.util.List;
import java.util.Optional;
import um.tds.modelo.Alerta;

public interface RepositorioAlerta  {
    void guardar(Alerta alerta);
    void eliminar(Alerta alerta);
    List<Alerta> obtenerTodas();
    Optional<Alerta> obtenerPorId(String id);

}