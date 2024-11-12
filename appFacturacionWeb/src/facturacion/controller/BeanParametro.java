package facturacion.controller;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import facturacion.model.dao.entities.Parametro;
import facturacion.model.manager.ManagerFacturacion;
import java.io.Serializable;

/**
 * ManagedBean JSF para el manejo de parametros.
 * @author mrea
 *
 */
@Named
@SessionScoped
public class BeanParametro implements Serializable {
	private static final long serialVersionUID = 1L;
	@EJB
	private ManagerFacturacion mFacturacion;
	
	public List<Parametro> getListaParametros(){
		return mFacturacion.findAllParametros();
	}
	
}
