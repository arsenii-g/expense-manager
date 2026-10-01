package um.tds.repositorios;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import um.tds.modelo.Notificacion;

public class AdaptadorNotificacionJSON implements RepositorioNotificacion {

    private static AdaptadorNotificacionJSON unicaInstancia;
    private static final String RUTA_FICHERO = "notificaciones.json";

    private AdaptadorNotificacionJSON() {}

    public static AdaptadorNotificacionJSON getInstance() {
        if (unicaInstancia == null) unicaInstancia = new AdaptadorNotificacionJSON();
        return unicaInstancia;
    }

    @Override
    public void guardar(Notificacion notif) {
        List<Notificacion> lista = obtenerTodas();
        lista.removeIf(n -> n.getId().equals(notif.getId()));
        lista.add(notif);
        escribirFichero(lista);
    }

    @Override
    public void eliminar(Notificacion notif) {
        List<Notificacion> lista = obtenerTodas();
        lista.removeIf(n -> n.getId().equals(notif.getId()));
        escribirFichero(lista);
    }

    @Override
    public List<Notificacion> obtenerTodas() {
        File fichero = new File(RUTA_FICHERO);
        if (!fichero.exists()) return new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            return mapper.readValue(fichero, new TypeReference<List<Notificacion>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();

        }
    }

    @Override
    public Optional<Notificacion> obtenerPorId(String id) {
        return obtenerTodas().stream()
                .filter(n -> n.getId().equals(id))
                .findFirst();
    }

    private void escribirFichero(List<Notificacion> lista) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(RUTA_FICHERO), lista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}