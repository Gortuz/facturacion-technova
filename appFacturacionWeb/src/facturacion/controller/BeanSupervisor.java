package facturacion.controller;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import facturacion.model.dao.entities.PedidoCab;
import facturacion.model.manager.ManagerPedidos;
import java.io.Serializable;

@Named
@SessionScoped
public class BeanSupervisor implements Serializable {
	private static final long serialVersionUID = 1L;
	private Date fechaInicio;
	private Date fechaFinal;
	@EJB
	private ManagerPedidos managerPedidos;
	private PedidoCab pedidoCabTmp;
	
	//Inyeccion de beans manejados:
	@Inject
	private BeanLogin beanLogin;
	
	public BeanSupervisor(){
		
	}
	public String actionBuscar(){
		if (fechaInicio == null || fechaFinal == null) {
	        JSFUtil.crearMensajeWARN("Debe seleccionar ambas fechas.");
	        return ""; 
	    }
	    if (fechaFinal.before(fechaInicio)) {
	        JSFUtil.crearMensajeERROR("La fecha final no puede ser menor que la fecha inicial.");
	        return "";
	    }
		//unicamente se invoca esta accion para actualizar
		//los parametros de fechas.
		return "";
	}
	public String actionBorrarFiltros(){
		setFechaInicio(null);
		setFechaFinal(null);
		return "";
	}
	/**
	 * 
	 * @param pedidoCab
	 * @return
	 */
	public String actionCargarPedido(PedidoCab pedidoCab){
		try {
			//capturamos el valor enviado desde el DataTable:
			pedidoCabTmp=pedidoCab;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	public boolean despachado(String des) {
	    return "Pedido despachado".equalsIgnoreCase(des != null ? des : "");
	}

	
	
	
	public String actionDespacharPedido(PedidoCab pedidoCab){
		try {
			//invocamos a ManagerFacturacion para crear una nueva factura:
			managerPedidos.despacharPedido(beanLogin.getCodigoUsuario(),pedidoCab.getNumeroPedido());
			JSFUtil.crearMensajeINFO("Pedido Despachado correctamente");
		} catch (Exception e) {
			e.printStackTrace();
			JSFUtil.crearMensajeERROR(e.getMessage());
		}
		return "";
	}
	public List<PedidoCab> getListaPedidoCab(){
		try {
			return managerPedidos.findPedidoCabByFechas(fechaInicio, fechaFinal);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public Date getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public Date getFechaFinal() {
		return fechaFinal;
	}
	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	public PedidoCab getPedidoCabTmp() {
		return pedidoCabTmp;
	}
	public void setPedidoCabTmp(PedidoCab pedidoCabTmp) {
		this.pedidoCabTmp = pedidoCabTmp;
	}
	//Un bean inyectado debe tener sus metodos accesores:
	public void setBeanLogin(BeanLogin beanLogin) {
		this.beanLogin = beanLogin;
	}
	public BeanLogin getBeanLogin() {
		return beanLogin;
	}
	
	
}
