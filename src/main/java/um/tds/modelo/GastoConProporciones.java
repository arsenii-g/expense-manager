package um.tds.modelo;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class GastoConProporciones {

    private final List<Gasto> gastos;
    private final double total;
    private final List<Double> proporciones;

    public GastoConProporciones(List<Gasto> gastos) {
        this.gastos = new ArrayList<>(gastos);
        this.total = calcularTotal();
        this.proporciones = calcularProporciones();
    }

    private double calcularTotal() {
        return gastos.stream()
                     .mapToDouble(Gasto::getCantidad)
                     .sum();
    }

    private List<Double> calcularProporciones() {
        if (total == 0) {
            return gastos.stream().map(g -> 0.0).collect(Collectors.toList());
        }
        return gastos.stream()
                     .map(g -> g.getCantidad() / total)
                     .collect(Collectors.toList());
    }

    public List<Gasto> getGastos() {
        return new ArrayList<>(gastos);
    }

    public double getTotal() {
        return total;
    }

    public List<Double> getProporciones() {
        return new ArrayList<>(proporciones);
    }

    public double getProporcion(Gasto gasto) {
        int index = gastos.indexOf(gasto);
        if (index >= 0 && index < proporciones.size()) {
            return proporciones.get(index);
        }
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("GastoConProporciones:\n");
        for (int i = 0; i < gastos.size(); i++) {
            sb.append(gastos.get(i).getDescripcion())
              .append(": ")
              .append(gastos.get(i).getCantidad())
              .append(" (")
              .append(String.format("%.2f%%", proporciones.get(i) * 100))
              .append(")\n");
        }
        sb.append("Total: ").append(total);
        return sb.toString();
    }
}
