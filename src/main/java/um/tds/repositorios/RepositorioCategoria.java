package um.tds.repositorios;

import java.util.List;
import java.util.Optional;
import um.tds.modelo.Categoria;

public interface RepositorioCategoria {

    void guardar(Categoria c);
    void eliminar(String id);
    List<Categoria> encontrar();
    Optional<Categoria> encontrarPorId(String id);
    Optional<Categoria> encontrarPorNombre(String nombre);
}