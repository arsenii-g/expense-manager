package um.tds.vista;

import com.calendarfx.model.Calendar;

import com.calendarfx.model.Calendar.Style;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import um.tds.controladores.ControladorJefe;
import um.tds.modelo.Gasto;

import java.util.List;
import java.util.stream.Collectors;

public class CalendarioController {

    @FXML private BorderPane contenedorCalendario;

    @FXML
    public void initialize() {
        CalendarView calendarView = new CalendarView();

        calendarView.showMonthPage();
        calendarView.setShowAddCalendarButton(false);
        calendarView.setShowPrintButton(false);
        calendarView.setShowPageToolBarControls(true);
        calendarView.setShowSearchField(false);

        Calendar calendarioGastos = new Calendar("Mis Gastos");
        calendarioGastos.setStyle(Style.STYLE1);

        List<Gasto> todosLosGastos = ControladorJefe.getInstancia().obtenerGastos(null, null, null);

        List<Gasto> misGastos = todosLosGastos.stream()
                .filter(g -> g.getIdCuentaCompartida() == null || "Yo".equals(g.getPagador()))
                .collect(Collectors.toList());

        for (Gasto g : misGastos) {
            String titulo = String.format("%s (%.2f€)", g.getDescripcion(), g.getCantidad());

            Entry<String> entrada = new Entry<>(titulo);

            entrada.setInterval(g.getFecha());
            entrada.setFullDay(true);

            calendarioGastos.addEntry(entrada);
        }

        CalendarSource fuenteDatos = new CalendarSource("Fuente Gastos");
        fuenteDatos.getCalendars().add(calendarioGastos);
        calendarView.getCalendarSources().setAll(fuenteDatos);

        contenedorCalendario.setCenter(calendarView);
    }
}