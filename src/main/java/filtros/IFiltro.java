package filtros;

import um.tds.modelo.Gasto;

public interface IFiltro  {
    boolean cumple(Gasto gasto);
}
