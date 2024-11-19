package facturacion.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;

import facturacion.model.dao.entities.Cliente;
import facturacion.model.dao.entities.PedidoCab;
import facturacion.model.dao.entities.Producto;
import facturacion.model.manager.ManagerFacturacion;
import facturacion.model.manager.ManagerPedidos;
import facturacion.model.dao.entities.PedidoDet;
import java.io.Serializable;
import java.math.BigDecimal;
@Named
@SessionScoped
public class BeanPedidos implements Serializable {
	private static final long serialVersionUID = 1L;
	private String cedula;
	private String nombres;
	private String apellidos;
	private String direccion;
	private String clave;
	
	private List<Producto> listaProductos;
	@EJB
	private ManagerFacturacion managerFacturacion;
	@EJB
	private ManagerPedidos managerPedidos;
	
	private PedidoCab pedidoCabTmp;

	public BeanPedidos() {

	}
	@PostConstruct
	public void iniciar(){
		listaProductos=managerFacturacion.findAllProductos();
	}

	public String actionComprobarCedula() {
		try {
			Cliente c = managerFacturacion.findClienteById(cedula);
			// verificamos la existencia del cliente:
			if (c == null)
				return "registro";// debe registrarse
			
			//caso contrario si ya existe el cliente, recuperamos la informacion
			// para presentarla en la pagina de pedidos:
			nombres = c.getNombres();
			apellidos = c.getApellidos();
			direccion = c.getDireccion();
			//creamos el pedido temporal y asignamos el cliente automaticamente:
			pedidoCabTmp=managerPedidos.crearPedidoTmp();
			managerPedidos.asignarClientePedidoTmp(pedidoCabTmp, cedula);

			return "pedido";
		} catch (Exception e) {
			// error no esperado:
			e.printStackTrace();
			JSFUtil.crearMensajeERROR(e.getMessage());
			return "";
		}
	}

	public String actionInsertarCliente() {
		try {
			managerFacturacion.insertarCliente(cedula, nombres, apellidos,
					direccion, clave);
			return "pedido";
		} catch (Exception e) {
			e.printStackTrace();
			JSFUtil.crearMensajeERROR(e.getMessage());
		}
		return "";

	}
	
	public void actionInsertarProducto(Producto p){
		try {
			System.out.println("EEEEEEEH");
			boolean bandera=true;
			if(pedidoCabTmp==null)
				pedidoCabTmp=managerPedidos.crearPedidoTmp();
			for (PedidoDet pedidoDet : pedidoCabTmp.getPedidoDets()) {
				if(pedidoDet.getProducto().getCodigoProducto()==(p.getCodigoProducto())) {	
					pedidoDet.setCantidad(pedidoDet.getCantidad()+1);
					//pedidoDet.setPrecioUnitarioVenta(pedidoDet.getPrecioUnitarioVenta());
					bandera=false;
					System.out.println("AAAAAAH");
				}
			}
			if(bandera) {
				//agregamos un nuevo producto al carrito de compras:
				managerPedidos.agregarDetallePedidoTmp(pedidoCabTmp, p.getCodigoProducto(), 1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			JSFUtil.crearMensajeERROR(e.getMessage());
		}
	}
	public String actionGuardarPedido(){
		try {
			managerPedidos.guardarPedidoTemporal(pedidoCabTmp);
		} catch (Exception e) {
			e.printStackTrace();
			JSFUtil.crearMensajeERROR(e.getMessage());
		}
		return "pedido_imprimir";
	}
	public String actionCerrarPedido(){
		pedidoCabTmp=null;
		//creamos el pedido temporal y asignamos el cliente automaticamente:
		pedidoCabTmp=managerPedidos.crearPedidoTmp();
		try {
			managerPedidos.asignarClientePedidoTmp(pedidoCabTmp, cedula);
		} catch (Exception e) {
			e.printStackTrace();
			JSFUtil.crearMensajeERROR(e.getMessage());
		}
		return "pedido";
	}
	
	public void validarNombresApellidos(FacesContext context, UIComponent component, Object value) {
	    String campo = (String) component.getAttributes().get("campo");
		String nombresOApellidos = (String) value;

	    // Expresión regular para validación de nombre
	    String regex = "^[A-ZÁÉÍÓÚÑ][a-záéíóúñ]{2,}(\\s[A-ZÁÉÍÓÚÑ][a-záéíóúñ]{2,})*$";
	    
	    // Validar con expresión regular
	    if (nombresOApellidos != null && !nombresOApellidos.matches(regex)) {
	        String mensaje = "El campo " + campo + " debe contener al mejor 3 letras, empezando con mayúscula";
	        throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, 
	            "El campo Nombres debe contener al menos 3 letras por palabra, empezando con mayúscula", null));
	    }
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public List<Producto> getListaProductos() {
		return listaProductos;
	}

	public void setListaProductos(List<Producto> listaProductos) {
		this.listaProductos = listaProductos;
	}

	public PedidoCab getPedidoCabTmp() {
		return pedidoCabTmp;
	}

	public void setPedidoCabTmp(PedidoCab pedidoCabTmp) {
		this.pedidoCabTmp = pedidoCabTmp;
	}

}
