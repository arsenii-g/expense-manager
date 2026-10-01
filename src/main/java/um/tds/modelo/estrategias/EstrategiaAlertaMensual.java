package um.tds.modelo.estrategias;

import java.time.LocalDate;
import java.util.List;
import um.tds.modelo.Alerta;
import um.tds.modelo.Gasto;

public class EstrategiaAlertaMensual implements EstrategiaAlerta {

    @Override
    public boolean comprobarLimites(List<Gasto> gastos, Alerta alerta) {
        LocalDate hoy = LocalDate.now();

        double totalGastado = gastos.stream()
            .filter(g -> g.getFecha().getMonth() == hoy.getMonth() &&
                         g.getFecha().getYear() == hoy.getYear())
            .mapToDouble(Gasto::getCantidad)
            .sum();

        return totalGastado > alerta.getLimite();
    }
}