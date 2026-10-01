package um.tds.repositorios;

import um.tds.modelo.Gasto;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class AdaptadorGastoJSON implements RepositorioGasto{
	private static AdaptadorGastoJSON unicaInstancia;
	private static final String RUTA_FICHERO="gastos.json";

	private AdaptadorGastoJSON() {}

	public static AdaptadorGastoJSON getInstancia() {
		if(unicaInstancia==null) {
			unicaInstancia=new AdaptadorGastoJSON();
		}
		return unicaInstancia;
	}

	@Override
	public void guardar(Gasto gasto) {
        List<Gasto> gastos = obtenerTodos();
        gastos.add(gasto);
        escribirEnFichero(gastos);
    }

    @Override
    public void actualizar(Gasto gasto) {
        List<Gasto> gastos = obtenerTodos();
        for (int i = 0; i < gastos.size(); i++) {
            if (gastos.get(i).getId().equals(gasto.getId())) {
                gastos.set(i, gasto);
                break;
            }
        }
        escribirEnFichero(gastos);
    }

    @Override
    public void eliminar(Gasto gasto) {
        List<Gasto> gastos = obtenerTodos();

        gastos.removeIf(g -> g.getId().equals(gasto.getId()));
        escribirEnFichero(gastos);
    }

    @Override
    public Gasto obtenerPorId(String id) {
        List<Gasto> gastos = obtenerTodos();
        return gastos.stream()
                     .filter(g -> g.getId().equals(id))
                     .findFirst()
                     .orElse(null);
    }

    @Override
    public List<Gasto> obtenerTodos() {
        File fichero = new File(RUTA_FICHERO);
        if (!fichero.exists()) {
            return new ArrayList<>();
        }

        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());

        try {
            return mapper.readValue(fichero, new TypeReference<List<Gasto>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void escribirEnFichero(List<Gasto> gastos) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        try {

            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(RUTA_FICHERO), gastos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

