package um.tds.controladores;

import java.util.List;
import um.tds.modelo.Categoria;
import um.tds.servicios.ServicioCategoria;

public class ControladorCategoria {

    private ServicioCategoria servicioCategoria;

    public ControladorCategoria() {
        this.servicioCategoria = ServicioCategoria.getInstancia();
    }

    public void crearCategoria(String nombre, String descripcion) {
        servicioCategoria.crearCategoria(nombre, descripcion);
    }

    public void modificarCategoria(String id, String nombre, String descripcion) {
        servicioCategoria.modificarCategoria(id, nombre, descripcion);
    }

    public void borrarCategoria(String id) {
        servicioCategoria.borrarCategoria(id);
    }

    public List<Categoria> obtenerCategorias() {
        return servicioCategoria.obtenerCategorias();
    }

    public Categoria obtenerCategoriaPorId(String id) {
        return servicioCategoria.obtenerCategoriaPorId(id);
    }
}
