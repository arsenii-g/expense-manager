package filtros;
import java.util.*;
import um.tds.modelo.Gasto;

public class FiltroAnd implements IFiltro {
    private List<IFiltro> filtros = new ArrayList<>();

    public void añadirFiltro(IFiltro f) { if (f != null) filtros.add(f); }

    @Override
    public boolean cumple(Gasto gasto) {

        return filtros.stream().allMatch(f -> f.cumple(gasto));
    }
}