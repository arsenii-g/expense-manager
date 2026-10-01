package um.tds.importadores;

public class ImportadorFactoria {

    public static ServicioImportador crearImportador(String formato) {
        return switch (formato.toLowerCase()) {
            case "csv" -> new ImportadorCSV();
            case "json" -> new ImportadorJSON();
            default -> throw new IllegalArgumentException(
                "Formato de importación no soportado: " + formato
            );
        };
    }
}