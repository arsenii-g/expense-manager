package um.tds.modelo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Notificacion {

    private String id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate fecha;
    private String mensaje;
    private boolean leida;
    private String alertaId;

    public Notificacion(String id, LocalDate fecha, String mensaje, boolean leida, String alertaId) {
        Objects.requireNonNull(id, "El id no puede ser nulo");
        Objects.requireNonNull(fecha, "La fecha no puede ser nula");
        Objects.requireNonNull(mensaje, "El mensaje no puede ser nulo");
        if (mensaje.trim().isEmpty()) throw new IllegalArgumentException("El mensaje no puede estar vacío");

        this.id = id;
        this.fecha = fecha;
        this.mensaje = mensaje.trim();
        this.leida = leida;
        this.alertaId = alertaId;
    }

    public Notificacion () {}

    public String getId() { return id; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) {
        Objects.requireNonNull(mensaje, "El mensaje no puede ser nulo");
        if (mensaje.trim().isEmpty()) throw new IllegalArgumentException("El mensaje no puede estar vacío");
        this.mensaje = mensaje.trim();
    }
    public boolean isLeida() { return leida; }
    public void setLeida(boolean leida) { this.leida = leida; }
    public String getAlertaId() { return alertaId; }
    public void setAlertaId(String alertaId) { this.alertaId = alertaId; }
    public void marcarComoLeida() { this.leida = true; }
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Notificacion other = (Notificacion) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaFormateada = (fecha != null) ? fecha.format(formatter) : "--/--/----";
        return "[" + fechaFormateada + "] " + mensaje;
    }
}
