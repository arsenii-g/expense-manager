package um.tds.controladores;

import java.util.List;
import um.tds.modelo.Alerta;
import um.tds.modelo.Categoria;
import um.tds.modelo.TipoAlerta;
import um.tds.servicios.ServicioAlerta;

public class ControladorAlerta {

    private ServicioAlerta servicioAlerta;

    public ControladorAlerta() {
        this.servicioAlerta = ServicioAlerta.getInstancia();
    }

    public void crearAlerta(double limite, Categoria categoria, TipoAlerta tipoAlerta) {
        servicioAlerta.crearAlerta(limite, categoria, tipoAlerta);
    }

    public void eliminarAlerta(String id) {
        servicioAlerta.eliminarAlerta(id);
    }

    public List<Alerta> mostrarAlertas() {
        return servicioAlerta.obtenerAlertas();
    }

    public void verificarAlertas() {
        servicioAlerta.verificarAlertas();
    }
}
