package um.tds.repositorios;

import java.util.List;
import java.util.Optional;
import um.tds.modelo.CuentaCompartida;

public interface RepositorioCuentaCompartida{
    void guardar(CuentaCompartida cuenta);
    void eliminar(CuentaCompartida cuenta);
    void actualizar(CuentaCompartida cuenta);
    List<CuentaCompartida> obtenerTodas();
    Optional<CuentaCompartida> obtenerPorId(String id);
}