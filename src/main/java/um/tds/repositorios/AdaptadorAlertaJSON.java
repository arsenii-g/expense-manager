package um.tds.repositorios;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import um.tds.modelo.Alerta;

public class AdaptadorAlertaJSON implements RepositorioAlerta {

    private static AdaptadorAlertaJSON unicaInstancia;
    private static final String RUTA_FICHERO = "alertas.json";

    private AdaptadorAlertaJSON() {}

    public static AdaptadorAlertaJSON getInstance() {
        if (unicaInstancia == null) unicaInstancia = new AdaptadorAlertaJSON();
        return unicaInstancia;
    }

    @Override
    public void guardar(Alerta alerta) {
        List<Alerta> alertas = obtenerTodas();
        alertas.removeIf(a -> a.getId().equals(alerta.getId()));
        alertas.add(alerta);
        escribirFichero(alertas);
    }

    @Override
    public void eliminar(Alerta alerta) {
        List<Alerta> alertas = obtenerTodas();
        alertas.removeIf(a -> a.getId().equals(alerta.getId()));
        escribirFichero(alertas);
    }

    @Override
    public List<Alerta> obtenerTodas() {
        File fichero = new File(RUTA_FICHERO);
        if (!fichero.exists()) return new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            return mapper.readValue(fichero, new TypeReference<List<Alerta>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<Alerta> obtenerPorId(String id) {
        return obtenerTodas().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
    }

    private void escribirFichero(List<Alerta> alertas) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(RUTA_FICHERO), alertas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}