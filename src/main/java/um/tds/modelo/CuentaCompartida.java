package um.tds.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")

public class CuentaCompartida {
	private String id;
	private String nombre;
	private List<PersonaCuenta> miembros;
	private boolean equitativa;
	private List<Gasto> gastos;

	public String getNombre() {
		return nombre;
	}
	public void setId(String id) {
		this.id=id;

	}

	public void setNombre(String nombre) {
		Objects.requireNonNull(nombre, "El nombre no puede ser nulo");
		if(nombre.trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre no puede estar vacío");

		}
		this.nombre=nombre.trim();
	}

	public List<PersonaCuenta> getMiembros() {
		return new ArrayList<>(miembros);
	}

	public boolean isEquitativa() {
		return equitativa;
	}

	public String getId() {
		return id;
	}

	public List<Gasto> getGastos() {
        if (this.gastos == null) {
            this.gastos = new ArrayList<>();
        }
        return new ArrayList<>(gastos);
    }
	public void setGastos(List<Gasto> gastos) { this.gastos = gastos; }

	public CuentaCompartida(String nombre, List<PersonaCuenta> miembros) {
        Objects.requireNonNull(nombre, "El nombre no puede ser nulo");
        Objects.requireNonNull(miembros, "La lista de miembros no puede ser nula");
        if (nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (miembros.isEmpty()) {
            throw new IllegalArgumentException("La cuenta debe tener al menos un miembro");
        }

        this.id = UUID.randomUUID().toString();
        this.nombre = nombre.trim();
        this.miembros = new ArrayList<>(miembros);
        this.equitativa = true;
        this.gastos = new ArrayList<>();

        calcularPorcentajesEquitativos();
    }

	public CuentaCompartida() {
        this.id = UUID.randomUUID().toString();
        this.nombre = "";
        this.miembros = new ArrayList<>();
        this.equitativa = true;
        this.gastos=new ArrayList<>();
    }

	 public CuentaCompartida(String nombre, List<PersonaCuenta> miembros, boolean validarPorcentajes) {
	        Objects.requireNonNull(nombre, "El nombre no puede ser nulo");
	        Objects.requireNonNull(miembros, "La lista de miembros no puede ser nula");
	        if (nombre.trim().isEmpty()) {
	            throw new IllegalArgumentException("El nombre no puede estar vacío");
	        }
	        if (miembros.isEmpty()) {
	            throw new IllegalArgumentException("La cuenta debe tener al menos un miembro");
	        }

	        this.id = UUID.randomUUID().toString();
	        this.nombre = nombre.trim();
	        this.miembros = new ArrayList<>(miembros);
	        this.equitativa = false;
	        this.gastos = new ArrayList<>();

	        if (validarPorcentajes) {
	            validarSumaPorcentajes();
	        }
	    }

	    private void calcularPorcentajesEquitativos() {
	        double porcentajeEquitativo = 100.0 / miembros.size();
	        for (PersonaCuenta miembro : miembros) {
	            miembro.setPorcentaje(porcentajeEquitativo);
	        }
	    }

	    private void validarSumaPorcentajes() {
	        double suma = miembros.stream()
	                              .mapToDouble(PersonaCuenta::getPorcentaje)
	                              .sum();

	        if (Math.abs(suma - 100.0) > 0.01) {
	            throw new IllegalArgumentException(
	                "La suma de porcentajes debe ser 100%. Actual: " + suma + "%"
	            );
	        }
	    }

	public void crearCuenta() {

	}
	public void calcularSaldos(double cantidadGasto, PersonaCuenta pagador) {
	        Objects.requireNonNull(pagador, "El pagador no puede ser nulo");
	        if (cantidadGasto <= 0) {
	            throw new IllegalArgumentException("La cantidad del gasto debe ser positiva");
	        }
	        if (!miembros.contains(pagador)) {
	            throw new IllegalArgumentException("El pagador debe ser miembro de la cuenta");
	        }

	        for (PersonaCuenta miembro : miembros) {
	            double parteQueDebePagar = cantidadGasto * (miembro.getPorcentaje() / 100.0);

	            if (miembro.equals(pagador)) {

	                miembro.actualizarSaldo(cantidadGasto - parteQueDebePagar);
	            } else {

	                miembro.actualizarSaldo(-parteQueDebePagar);
	            }
	        }
	 }
	public double obtenerSaldoMiembro(String nombreMiembro) {
        return miembros.stream()
                       .filter(m -> m.getNombre().equals(nombreMiembro))
                       .findFirst()
                       .map(PersonaCuenta::getSaldo)
                       .orElseThrow(() -> new IllegalArgumentException(
                           "No existe un miembro con el nombre: " + nombreMiembro
                       ));
    }

	public void setMiembros(List<PersonaCuenta> miembros) {
	    this.miembros = miembros;
	}

	public void agregarGasto(Gasto gasto) {
		this.gastos.add(gasto);
	}

    public boolean esMiembro(PersonaCuenta persona) {
        return miembros.contains(persona);
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
		CuentaCompartida other = (CuentaCompartida) obj;
		return Objects.equals(id, other.id);
	}
	@Override
	public String toString() {
	    return this.nombre;
	}

}
