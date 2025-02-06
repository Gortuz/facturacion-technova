package facturacion.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
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
	private Double total;
	
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
			Double monto = Double.valueOf(pedidoCabTmp.getSubtotal().toString());
			Double montoIVA = monto * managerPedidos.getIVA() / 100;
			
			this.total = monto + montoIVA;
			System.out.println(monto+"+"+montoIVA+" = "+this.total);
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
	
	public String actionPagarPedidoConTarjeta(PedidoCab pedidoCab) {
		try {
	        // Obtén los datos de la tarjeta desde la solicitud (request)
	        FacesContext context = FacesContext.getCurrentInstance();
	        Map<String, String> params = context.getExternalContext().getRequestParameterMap();

	        String nombreForm = "form2:formPago:";
	        
	        String numeroTarjeta = params.get(nombreForm+"numeroTarjeta");
	        String titular = params.get(nombreForm+"titular");
	        String fechaExpiracion = params.get(nombreForm+"fechaExpiracion");
	        String cvv = params.get(nombreForm+"cvv");
	        String tipoMoneda = params.get(nombreForm+"tipoMoneda");
	        String descripcion = params.get(nombreForm+"descripcion");
	        
	        
	        
	        String res = managerPedidos.pagarConTarjeta(numeroTarjeta, titular, fechaExpiracion, cvv, tipoMoneda, descripcion, pedidoCab);
	        
	        if (res.equals("aprobado")) {
				JSFUtil.crearMensajeINFO("!Pago exitoso!");
				this.actionDespacharPedido(pedidoCab);
	        }
		 	else if (res.equals("rechazado")) {
				JSFUtil.crearMensajeERROR("Pago rechazado");
		 	}
		 	else if (res.equals("pendiente")) {
				JSFUtil.crearMensajeWARN("Pago pendiente");
		 	}
			
	        return res;
  
	    } catch (Exception e) {
	        JSFUtil.crearMensajeERROR(e.getMessage());
	    }
	    return "";
	}
	
	public String verificarEstadoTransaccion(PedidoCab pedidoCab) throws Exception {
		String r = managerPedidos.buscarTransaccion(pedidoCab);
		
		if (r.equals("4")) {
			System.out.println("llegué");
			JSFUtil.crearMensajeINFO("!Pago exitoso!");
			this.actionDespacharPedido(pedidoCab);
        }
	 	else if (r.equals("3")) {
			JSFUtil.crearMensajeERROR("Pago rechazado");
	 	}
	 	else if (r.equals("5")) {
			JSFUtil.crearMensajeWARN("La transacción está siendo procesada");
	 	}
		
		return "";
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
	
	
	public Double getTotal() {

		return this.total;
		
	
	}
	
	
}
