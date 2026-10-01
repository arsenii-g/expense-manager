package um.tds.modelo;

import java.util.Objects;
import java.util.UUID;

public class PersonaCuenta {
	private String id;
	private String nombre;
	private double porcentaje;
	private double saldo;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
        Objects.requireNonNull(nombre, "El nombre no puede ser nulo");
        if (nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        this.nombre = nombre.trim();
    }
	public double getPorcentaje() {
		return porcentaje;
	}

	 public void setPorcentaje(double porcentaje) {
	        if (porcentaje < 0 || porcentaje > 100) {
	            throw new IllegalArgumentException("El porcentaje debe estar entre 0 y 100");
	        }
	        this.porcentaje = porcentaje;
	    }

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	public String getId() {
		return id;
	}

	public PersonaCuenta(String nombre, double porcentaje) {
		Objects.requireNonNull(nombre, "El nombre no puede ser nulo");
		if(nombre.trim().isEmpty()) {
			 throw new IllegalArgumentException("El nombre no puede estar vacío");
		}
		if (porcentaje < 0 || porcentaje > 100) {
            throw new IllegalArgumentException("El porcentaje debe estar entre 0 y 100");
        }
		this.id=UUID.randomUUID().toString();
		this.nombre = nombre.trim();
        this.porcentaje = porcentaje;
        this.saldo = 0.0;
	}

    public PersonaCuenta(String id, String nombre, double porcentaje, double saldo) {
        Objects.requireNonNull(id, "El id no puede ser nulo");
        Objects.requireNonNull(nombre, "El nombre no puede ser nulo");
        if (nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (porcentaje < 0 || porcentaje > 100) {
            throw new IllegalArgumentException("El porcentaje debe estar entre 0 y 100");
        }

        this.id = id;
        this.nombre = nombre.trim();
        this.porcentaje = porcentaje;
        this.saldo = saldo;
    }

    public PersonaCuenta() {
        this.id = UUID.randomUUID().toString();
        this.nombre = "";
        this.porcentaje = 0.0;
        this.saldo = 0.0;
    }

	public void actualizarSaldo(double cantidad) {
		this.saldo+=cantidad;
	}

	public boolean debeDinero() {
		return saldo <0;
	}

	public boolean leDeben() {
		return saldo >0;
	}

	public void setId(String id) {
		this.id=id;
	}

	@Override
	public String toString() {
		return "PersonaCuenta [id=" + id + ", nombre=" + nombre + ", porcentaje=" + porcentaje + ", saldo=" + saldo
				+ "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonaCuenta other = (PersonaCuenta) obj;
		return Objects.equals(id, other.id);
	}

}
