package filtros;
import java.time.LocalDate;
import um.tds.modelo.Gasto;

public class FiltroRangoFechas implements IFiltro {
    private LocalDate inicio, fin;
    public FiltroRangoFechas(LocalDate i, LocalDate f) { this.inicio = i; this.fin = f; }

    @Override
    public boolean cumple(Gasto gasto) {
        LocalDate fecha = gasto.getFecha();
        boolean despues = (inicio == null) || !fecha.isBefore(inicio);
        boolean antes = (fin == null) || !fecha.isAfter(fin);
        return despues && antes;
    }
}