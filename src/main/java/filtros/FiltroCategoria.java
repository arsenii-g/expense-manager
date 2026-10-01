package filtros;
import um.tds.modelo.Categoria;
import um.tds.modelo.Gasto;

public class FiltroCategoria implements IFiltro {
    private Categoria categoria;
    public FiltroCategoria(Categoria c) { this.categoria = c; }
    @Override
    public boolean cumple(Gasto gasto) {
        return categoria == null || gasto.getCategoria().equals(categoria);
    }
}