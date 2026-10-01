package um.tds.repositorios;

import java.util.List;

import um.tds.modelo.Gasto;

public interface RepositorioGasto{
	void guardar(Gasto g);
	void actualizar(Gasto g);
	void eliminar(Gasto g);
	Gasto obtenerPorId(String id);
	List<Gasto> obtenerTodos();
}

