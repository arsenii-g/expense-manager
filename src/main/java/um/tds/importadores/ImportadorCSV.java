package um.tds.importadores;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import um.tds.modelo.Categoria;
import um.tds.modelo.Gasto;
import um.tds.servicios.ServicioCategoria;

public class ImportadorCSV implements ServicioImportador {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public List<Gasto> importar(String ruta) {
        List<Gasto> gastosImportados = new ArrayList<>();

        try (Stream<String> lineas = Files.lines(Paths.get(ruta))) {
            lineas.skip(1)
                  .forEach(linea -> {
                parsearGasto(linea).ifPresent(gastosImportados::add);
            });
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }

        return gastosImportados;
    }

    private Optional<Gasto> parsearGasto(String linea) {
        try {

            String[] partes = linea.split(";");
            if (partes.length < 4) return Optional.empty();

            LocalDate fecha = LocalDate.parse(partes[0].trim(), FORMATTER);
            String descripcion = partes[1].trim();
            double cantidad = Double.parseDouble(partes[2].trim().replace(",", "."));
            String nombreCat = partes[3].trim();

            Categoria categoria = obtenerOGenerarCategoria(nombreCat);

            Gasto gasto = new Gasto(fecha, cantidad, categoria, descripcion);

            if (partes.length >= 5 && !partes[4].trim().isEmpty()) {
                gasto.setNombreCuenta(partes[4].trim());
            }

            if (partes.length >= 6 && !partes[5].trim().isEmpty()) {
                gasto.setPagador(partes[5].trim());
            }

            return Optional.of(gasto);

        } catch (Exception e) {
            System.err.println("Error procesando línea [" + linea + "]: " + e.getMessage());
            return Optional.empty();
        }
    }

    private Categoria obtenerOGenerarCategoria(String nombre) {
        return ServicioCategoria.getInstancia().obtenerCategorias().stream()
                .filter(c -> c.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElseGet(() -> {
                    ServicioCategoria.getInstancia().crearCategoria(nombre, "Importada");
                    return ServicioCategoria.getInstancia().obtenerCategorias().stream()
                            .filter(c -> c.getNombre().equalsIgnoreCase(nombre))
                            .findFirst()
                            .get();
                });
    }
}