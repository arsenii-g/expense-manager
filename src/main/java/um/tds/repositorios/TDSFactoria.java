package um.tds.repositorios;

public class TDSFactoria extends Factoria {

    @Override
    public RepositorioGasto getRepositorioGasto() {
        return AdaptadorGastoJSON.getInstancia();
    }

    @Override
     public RepositorioCategoria getRepositorioCategoria(){
        return AdaptadorCategoriaJSON.getInstance();
     }

    @Override
    public RepositorioAlerta getRepositorioAlerta() {
        return AdaptadorAlertaJSON.getInstance();
    }

    @Override
    public RepositorioCuentaCompartida getRepositorioCuentaCompartida() {
        return AdaptadorCuentaCompartidaJSON.getInstance();
    }

    @Override
    public RepositorioNotificacion getRepositorioNotificacion() {
        return AdaptadorNotificacionJSON.getInstance();
    }
}

