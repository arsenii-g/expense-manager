package um.tds.importadores;

import java.util.List;

import um.tds.modelo.Gasto;

public interface ServicioImportador {
	List<Gasto> importar(String ruta);
}

