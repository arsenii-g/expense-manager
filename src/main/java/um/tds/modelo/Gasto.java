package um.tds.modelo;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class Gasto {

    private String id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate fecha;

    private double cantidad;
    private String descripcion;
    private Categoria categoria;
    private String idCuentaCompartida;
    private String pagador;
    private String nombreCuenta;

    public Gasto() {
        this.id = UUID.randomUUID().toString();
    }

    public Gasto(LocalDate fecha, double cantidad, Categoria categoria) {
        this();
        this.fecha = Objects.requireNonNull(fecha);
        this.cantidad = cantidad;
        this.categoria = Objects.requireNonNull(categoria);
        this.descripcion = "";
    }

    public Gasto(LocalDate fecha, double cantidad, Categoria categoria, String descripcion) {
        this(fecha, cantidad, categoria);
        this.descripcion = descripcion != null ? descripcion.trim() : "";
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public double getCantidad() { return cantidad; }
    public void setCantidad(double cantidad) { this.cantidad = cantidad; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public String getIdCuentaCompartida() { return idCuentaCompartida; }
    public void setIdCuentaCompartida(String idCuentaCompartida) { this.idCuentaCompartida = idCuentaCompartida; }

    public String getPagador() { return pagador; }
    public void setPagador(String pagador) { this.pagador = pagador; }

    public String getNombreCuenta() { return nombreCuenta; }
    public void setNombreCuenta(String nombreCuenta) { this.nombreCuenta = nombreCuenta; }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Gasto other = (Gasto) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "Gasto [id=" + id + ", fecha=" + fecha + ", cantidad=" + cantidad +
               ", nombreCuenta=" + nombreCuenta + ", idCC=" + idCuentaCompartida + "]";
    }
}