package filtros;
import java.util.List;
import um.tds.modelo.Gasto;

public class FiltroMes implements IFiltro {
    private List<Integer> meses;
    public FiltroMes(List<Integer> meses) { this.meses = meses; }
    @Override
    public boolean cumple(Gasto gasto) {
        if (meses == null || meses.isEmpty()) return true;
        return meses.contains(gasto.getFecha().getMonthValue());
    }
}