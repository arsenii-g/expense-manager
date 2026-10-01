package um.tds.modelo.estrategias;

import java.util.List;
import um.tds.modelo.Alerta;
import um.tds.modelo.Gasto;

public interface EstrategiaAlerta {
    boolean comprobarLimites(List<Gasto> gastos, Alerta alerta);
}