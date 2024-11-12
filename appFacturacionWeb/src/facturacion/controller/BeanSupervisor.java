package facturacion.controller;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import facturacion.model.dao.entities.PedidoCab;
import facturacion.model.manager.ManagerPedidos;

@ManagedBean
@SessionScoped
public class BeanSupervisor {
	private Date fechaInicio;
	private Date fechaFinal;
	@EJB
	private ManagerPedidos managerPedidos;
	private PedidoCab pedidoCabTmp;
	
	//Inyeccion de beans manejados:
	@ManagedProperty(value="#{beanLogin}")
	private BeanLogin beanLogin;
	
	public BeanSupervisor(){
		
	}
	public String actionBuscar(){
		//unicamente se invoca esta accion para actualizar
		//los parametros de fechas.
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
	public String actionDespacharPedido(PedidoCab pedidoCab){
		try {
			//invocamos a ManagerFacturacion para crear una nueva factura:
			managerPedidos.despacharPedido(beanLogin.getCodigoUsuario(),pedidoCab.getNumeroPedido());
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
