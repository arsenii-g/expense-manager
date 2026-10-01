package um.tds.repositorios;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import um.tds.modelo.Categoria;

public class AdaptadorCategoriaJSON implements RepositorioCategoria {

    private static AdaptadorCategoriaJSON unicaInstancia;
    private static final String RUTA_FICHERO = "categorias.json";

    private AdaptadorCategoriaJSON() { }

    public static AdaptadorCategoriaJSON getInstance() {
        if (unicaInstancia == null)
            unicaInstancia = new AdaptadorCategoriaJSON();
        return unicaInstancia;
    }

    @Override
    public void guardar(Categoria c) {
        List<Categoria> categorias = encontrar();

        categorias.removeIf(cat -> cat.getId().equals(c.getId()));
        categorias.add(c);
        escribirFichero(categorias);
    }

    @Override
    public void eliminar(String id) {
        List<Categoria> categorias = encontrar();
        boolean borrado = categorias.removeIf(c -> c.getId().equals(id));
        if (borrado) {
            escribirFichero(categorias);
        }
    }

    @Override
    public List<Categoria> encontrar() {
        File fichero = new File(RUTA_FICHERO);
        if (!fichero.exists()) return new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(fichero, new TypeReference<List<Categoria>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<Categoria> encontrarPorId(String id) {
        return encontrar().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Categoria> encontrarPorNombre(String nombre) {
        return encontrar().stream()
                .filter(c -> c.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
    }

    private void escribirFichero(List<Categoria> categorias) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(RUTA_FICHERO), categorias);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
