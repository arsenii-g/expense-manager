package um.tds.vista;

import java.time.LocalDate;

import java.util.List;
import java.util.Scanner;

import um.tds.controladores.ControladorJefe;
import um.tds.modelo.Categoria;
import um.tds.modelo.Gasto;

public class InterfazConsola {

    private Scanner scanner = new Scanner(System.in);
    private ControladorJefe controlador = ControladorJefe.getInstancia();

    public void iniciar() {
        boolean salir = false;
        System.out.println("=== GESTIÓN DE GASTOS (MODO CONSOLA) ===");

        while (!salir) {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Listar Gastos");
            System.out.println("2. Registrar Nuevo Gasto");
            System.out.println("3. Modificar Gasto ");
            System.out.println("4. Eliminar Gasto ");
            System.out.println("0. Salir");
            System.out.print("Elige una opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    listarGastos();
                    break;
                case "2":
                    registrarGasto();
                    break;
                case "3":
                    modificarGasto();
                    break;
                case "4":
                    eliminarGasto();
                    break;
                case "0":
                    salir = true;
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private void listarGastos() {
        System.out.println("\n--- LISTA DE GASTOS ---");
        List<Gasto> gastos = controlador.obtenerGastos(null, null, null);
        if (gastos.isEmpty()) {
            System.out.println("No hay gastos registrados.");
        } else {

            for (int i = 0; i < gastos.size(); i++) {
                Gasto g = gastos.get(i);
                System.out.printf("[%d] %s | %.2f€ | %s | Cat: %s\n",
                    (i + 1), g.getFecha(), g.getCantidad(), g.getDescripcion(), g.getCategoria().getNombre());
            }
        }
    }

    private void registrarGasto() {
        System.out.println("\n--- NUEVO GASTO ---");
        try {
            System.out.print("Descripción: ");
            String desc = scanner.nextLine();

            System.out.print("Cantidad: ");
            double cant = Double.parseDouble(scanner.nextLine());

            Categoria cat = seleccionarCategoria();
            if (cat == null) return;

            controlador.registrarGasto(LocalDate.now(), cant, cat, desc);
            System.out.println("Gasto guardado correctamente.");

        } catch (NumberFormatException e) {
            System.out.println("Error: La cantidad debe ser numérica y separada por puntos para la parte decimal.");
        }
    }

    private void modificarGasto() {
        System.out.println("\n--- MODIFICAR GASTO ---");
        Gasto gasto = seleccionarGasto();
        if (gasto == null) return;

        try {
            System.out.println("Introduce los nuevos datos (pulsa ENTER para mantener el valor actual):");

            System.out.print("Descripción [" + gasto.getDescripcion() + "]: ");
            String nuevaDesc = scanner.nextLine();
            if (!nuevaDesc.isEmpty()) gasto.setDescripcion(nuevaDesc);

            System.out.print("Cantidad [" + gasto.getCantidad() + "]: ");
            String nuevaCantStr = scanner.nextLine();
            if (!nuevaCantStr.isEmpty()) {
                gasto.setCantidad(Double.parseDouble(nuevaCantStr));
            }

            System.out.print("Fecha (YYYY-MM-DD) [" + gasto.getFecha() + "]: ");
            String nuevaFechaStr = scanner.nextLine();
            if (!nuevaFechaStr.isEmpty()) {
                gasto.setFecha(LocalDate.parse(nuevaFechaStr));
            }

            controlador.actualizarGasto(gasto);
            System.out.println("Gasto modificado correctamente.");

        } catch (Exception e) {
            System.out.println("Error al modificar: " + e.getMessage());
        }
    }

    private void eliminarGasto() {
        System.out.println("\n--- ELIMINAR GASTO ---");
        Gasto gasto = seleccionarGasto();
        if (gasto == null) return;

        System.out.print("¿Seguro que quieres borrar este gasto? (s/n): ");
        if (scanner.nextLine().equalsIgnoreCase("s")) {
            controlador.eliminarGasto(gasto);
            System.out.println("✅ Gasto eliminado.");
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    private Gasto seleccionarGasto() {
        List<Gasto> gastos = controlador.obtenerGastos(null, null, null);
        if (gastos.isEmpty()) {
            System.out.println("No hay gastos para seleccionar.");
            return null;
        }
        listarGastos();
        System.out.print("Selecciona el número del gasto: ");
        try {
            int indice = Integer.parseInt(scanner.nextLine()) - 1;
            if (indice >= 0 && indice < gastos.size()) {
                return gastos.get(indice);
            }
        } catch (NumberFormatException e) {}

        System.out.println("Selección inválida.");
        return null;
    }

    private Categoria seleccionarCategoria() {
        List<Categoria> categorias = controlador.obtenerCategorias();
        System.out.println("Categorías disponibles:");
        for (int i = 0; i < categorias.size(); i++) {
            System.out.println((i + 1) + ". " + categorias.get(i).getNombre());
        }
        System.out.print("Elige número de categoría: ");
        try {
            int indice = Integer.parseInt(scanner.nextLine()) - 1;
            if (indice >= 0 && indice < categorias.size()) {
                return categorias.get(indice);
            }
        } catch (Exception e) {}
        System.out.println("Categoría no válida.");
        return null;
    }
}