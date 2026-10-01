package um.tds.importadores;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import um.tds.modelo.Categoria;
import um.tds.modelo.Gasto;
import um.tds.servicios.ServicioCategoria;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ImportadorJSON implements ServicioImportador {

    private final ObjectMapper mapper;

    public ImportadorJSON() {
        this.mapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(formatter));
        this.mapper.registerModule(javaTimeModule);
    }

    @Override
    public List<Gasto> importar(String ruta) {
        List<Gasto> gastosImportados = new ArrayList<>();

        try {
            List<Map<String, Object>> listaDatos = mapper.readValue(
                new File(ruta),
                new TypeReference<List<Map<String, Object>>>() {}
            );

            for (Map<String, Object> datos : listaDatos) {
                convertirGasto(datos).ifPresent(gastosImportados::add);
            }

        } catch (IOException e) {
            System.err.println("Error al procesar el archivo JSON: " + e.getMessage());
        }

        return gastosImportados;
    }

    private Optional<Gasto> convertirGasto(Map<String, Object> datos) {
        try {
            LocalDate fecha = LocalDate.parse(datos.get("fecha").toString(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String descripcion = (String) datos.getOrDefault("descripcion", "");
            double cantidad = Double.parseDouble(datos.get("cantidad").toString());
            String nombreCat = (String) datos.get("categoria");

            Categoria categoria = obtenerOGenerarCategoria(nombreCat);

            Gasto gasto = new Gasto(fecha, cantidad, categoria, descripcion);

            if (datos.containsKey("nombreCuenta") && datos.get("nombreCuenta") != null) {
                gasto.setNombreCuenta(datos.get("nombreCuenta").toString().trim());
            }

            if (datos.containsKey("pagador") && datos.get("pagador") != null) {
                gasto.setPagador(datos.get("pagador").toString().trim());
            }

            return Optional.of(gasto);
        } catch (Exception e) {
            System.err.println("Error en un objeto del JSON: " + e.getMessage());
            return Optional.empty();
        }
    }

    private Categoria obtenerOGenerarCategoria(String nombre) {
        return ServicioCategoria.getInstancia().obtenerCategorias().stream()
                .filter(c -> c.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElseGet(() -> {
                    ServicioCategoria.getInstancia().crearCategoria(nombre, "Importada de JSON");
                    return ServicioCategoria.getInstancia().obtenerCategorias().stream()
                            .filter(c -> c.getNombre().equalsIgnoreCase(nombre))
                            .findFirst()
                            .orElseThrow();
                });
    }
}