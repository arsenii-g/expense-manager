package um.tds.modelo;

import java.util.Objects;
import java.util.UUID;

public class Categoria {
	private String id;
	private String nombre;
	private String descripcion;

	public String getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		Objects.requireNonNull(nombre, "El nombre no puede ser nulo");
		if(nombre.trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre no puede estar vacío");
		}
		this.nombre=nombre.trim();

	}

	public String getDescripcion() {
		return descripcion;
	}

	  public void setDescripcion(String descripcion) {
	        this.descripcion = descripcion != null ? descripcion.trim() : "";
	    }

		public Categoria(String nombre, String descripcion) {
			Objects.requireNonNull(nombre, "El nombre no puede ser nulo");
			if(nombre.trim().isEmpty()) {
				throw new IllegalArgumentException("El nombre no puede estar vacío");
			}
			this.id=UUID.randomUUID().toString();
			this.nombre = nombre.trim();
			this.descripcion = descripcion!=null ? descripcion.trim(): "";
		}

		public Categoria(String id, String nombre, String descripcion) {
	        Objects.requireNonNull(id, "El ID no puede ser nulo");
	        Objects.requireNonNull(nombre, "El nombre no puede ser nulo");

			this.id = id;
			this.nombre = nombre;
			this.descripcion = descripcion != null ? descripcion.trim(): "";
		}

		public Categoria() {

		    this.id = UUID.randomUUID().toString();
		}

		public void setId(String id) {
			this.id=id;
		}

	@Override
	public String toString() {
		return nombre;
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
		Categoria other = (Categoria) obj;
		return Objects.equals(id, other.id);

	}
}
