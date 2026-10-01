package um.tds.modelo;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import um.tds.modelo.estrategias.EstrategiaAlerta;
import um.tds.modelo.estrategias.EstrategiaAlertaMensual;
import um.tds.modelo.estrategias.EstrategiaAlertaSemanal;

public class Alerta {

    private String id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate fechaInicio;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate fechaFin;

    private double limite;
    private Categoria categoria;
    private boolean activo;
    private boolean superado;

    private EstrategiaAlerta estrategia;
    private TipoAlerta tipoEstrategia;

    public Alerta(double limite, Categoria categoria, TipoAlerta tipoEstrategia) {
        Objects.requireNonNull(categoria, "La categoría no puede ser nula");
        Objects.requireNonNull(tipoEstrategia, "El tipo de estrategia no puede ser nulo");

        this.id = UUID.randomUUID().toString();
        this.limite = limite;
        this.categoria = categoria;
        this.tipoEstrategia = tipoEstrategia;
        this.fechaInicio = LocalDate.now();
        this.fechaFin = calcularFechaFin(tipoEstrategia);
        this.activo = true;
        this.superado = false;
        this.estrategia = asignarEstrategia(tipoEstrategia);
    }

    public Alerta() {

    }

    private LocalDate calcularFechaFin(TipoAlerta tipo) {
        switch (tipo) {
            case SEMANAL:
                return fechaInicio.plusWeeks(1);
            case MENSUAL:
                return fechaInicio.plusMonths(1);
            default:
                return fechaInicio.plusMonths(1);
        }
    }

    private EstrategiaAlerta asignarEstrategia(TipoAlerta tipo) {
        switch (tipo) {
            case SEMANAL:
                return new EstrategiaAlertaSemanal();
            case MENSUAL:
                return new EstrategiaAlertaMensual();
            default:
                return new EstrategiaAlertaMensual();
        }
    }

    public String getId() { return id; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public double getLimite() { return limite; }
    public void setLimite(double limite) { this.limite = limite; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public boolean isSuperado() { return superado; }
    public void setSuperado(boolean superado) { this.superado = superado; }
    @JsonIgnore
    public EstrategiaAlerta getEstrategia() { return estrategia; }
    public TipoAlerta getTipoEstrategia() { return tipoEstrategia; }

    public void setTipoEstrategia(TipoAlerta tipo) {
        this.tipoEstrategia = tipo;
        this.fechaFin = calcularFechaFin(tipo);
        this.estrategia = asignarEstrategia(tipo);
    }

    public boolean comprobarLimites(List<Gasto> gastos) {
        if (estrategia == null) return false;
        return estrategia.comprobarLimites(gastos, this);
    }
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Alerta other = (Alerta) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "Alerta [id=" + id + ", limite=" + limite + ", categoria=" + categoria +
               ", activo=" + activo + ", superado=" + superado + ", tipoEstrategia=" + tipoEstrategia + "]";
    }
}
