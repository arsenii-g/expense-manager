package um.tds.servicios;

import java.util.List;
import java.util.Optional;

import um.tds.modelo.Categoria;
import um.tds.repositorios.Factoria;
import um.tds.repositorios.RepositorioCategoria;

public class ServicioCategoria {

    private static ServicioCategoria instancia;
    private RepositorioCategoria repositorio;

    private ServicioCategoria() {
        try {
            this.repositorio = Factoria.getInstancia().getRepositorioCategoria();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized ServicioCategoria getInstancia() {
        if (instancia == null) {
            instancia = new ServicioCategoria();
        }
        return instancia;
    }

    public void crearCategoria(String nombre, String descripcion) {
        Optional<Categoria> existente = repositorio.encontrarPorNombre(nombre);
        if (existente.isEmpty()) {
            Categoria categoria = new Categoria(nombre, descripcion);
            repositorio.guardar(categoria);
        } else {
            throw new IllegalArgumentException("Ya existe una categoría con el nombre: " + nombre);
        }
    }

    public void modificarCategoria(String id, String nombre, String descripcion) {
        Optional<Categoria> optionalCategoria = repositorio.encontrarPorId(id);
        if (optionalCategoria.isPresent()) {
            Categoria categoria = optionalCategoria.get();
            categoria.setNombre(nombre);
            categoria.setDescripcion(descripcion);
            repositorio.guardar(categoria);
        } else {
            throw new IllegalArgumentException("No se encontró la categoría con id: " + id);
        }
    }

    public void borrarCategoria(String id) {
        repositorio.eliminar(id);
    }

    public List<Categoria> obtenerCategorias() {
        return repositorio.encontrar();
    }

    public Categoria obtenerCategoriaPorId(String id) {
        return repositorio.encontrarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la categoría con id: " + id));
    }
}
