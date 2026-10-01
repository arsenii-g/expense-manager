package um.tds.repositorios;

public abstract class Factoria {

    private static Factoria unicaInstancia;
    public static final String FAC_TDS = "um.tds.repositorios.TDSFactoria";

    public static Factoria getInstancia(String tipo) throws Exception {
        if (unicaInstancia == null) {
            unicaInstancia = (Factoria) Class.forName(tipo).getDeclaredConstructor().newInstance();
        }
        return unicaInstancia;
    }

    public static Factoria getInstancia() throws Exception {
        return getInstancia(FAC_TDS);
    }

    public abstract RepositorioGasto getRepositorioGasto();
    public abstract RepositorioCategoria getRepositorioCategoria();
    public abstract RepositorioAlerta getRepositorioAlerta();
    public abstract RepositorioCuentaCompartida getRepositorioCuentaCompartida();
    public abstract RepositorioNotificacion getRepositorioNotificacion();
}