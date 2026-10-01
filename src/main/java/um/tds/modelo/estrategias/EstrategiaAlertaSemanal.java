package um.tds.modelo.estrategias;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import um.tds.modelo.Alerta;
import um.tds.modelo.Gasto;

public class EstrategiaAlertaSemanal implements EstrategiaAlerta {

    @Override
    public boolean comprobarLimites(List<Gasto> gastos, Alerta alerta) {
        LocalDate hoy = LocalDate.now();

        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int semanaActual = hoy.get(weekFields.weekOfWeekBasedYear());
        int anioActual = hoy.get(weekFields.weekBasedYear());

        double totalGastado = gastos.stream()
            .filter(g -> {
                int semanaGasto = g.getFecha().get(weekFields.weekOfWeekBasedYear());
                int anioGasto = g.getFecha().get(weekFields.weekBasedYear());
                return semanaGasto == semanaActual && anioGasto == anioActual;
            })
            .mapToDouble(Gasto::getCantidad)
            .sum();

        return totalGastado > alerta.getLimite();
    }
}