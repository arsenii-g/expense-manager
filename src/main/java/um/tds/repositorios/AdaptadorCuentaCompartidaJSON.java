package um.tds.repositorios;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import um.tds.modelo.CuentaCompartida;

public class AdaptadorCuentaCompartidaJSON implements RepositorioCuentaCompartida {

    private static AdaptadorCuentaCompartidaJSON unicaInstancia;
    private static final String RUTA_FICHERO = "cuentas_compartidas.json";

    private AdaptadorCuentaCompartidaJSON() {}

    public static AdaptadorCuentaCompartidaJSON getInstance() {
        if (unicaInstancia == null) unicaInstancia = new AdaptadorCuentaCompartidaJSON();
        return unicaInstancia;
    }

    @Override
    public void guardar(CuentaCompartida cuenta) {
        List<CuentaCompartida> cuentas = obtenerTodas();
        cuentas.removeIf(c -> c.getId().equals(cuenta.getId()));
        cuentas.add(cuenta);
        escribirFichero(cuentas);
    }

    @Override
    public void eliminar(CuentaCompartida cuenta) {
        List<CuentaCompartida> cuentas = obtenerTodas();
        cuentas.removeIf(c -> c.getId().equals(cuenta.getId()));
        escribirFichero(cuentas);
    }

    @Override
    public List<CuentaCompartida> obtenerTodas() {
        File fichero = new File(RUTA_FICHERO);
        if (!fichero.exists()) return new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            return mapper.readValue(fichero, new TypeReference<List<CuentaCompartida>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<CuentaCompartida> obtenerPorId(String id) {
        return obtenerTodas().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    @Override
    public void actualizar(CuentaCompartida cuenta) {
        guardar(cuenta);
    }

    private void escribirFichero(List<CuentaCompartida> cuentas) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(RUTA_FICHERO), cuentas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

