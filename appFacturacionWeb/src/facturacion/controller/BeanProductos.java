package facturacion.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import facturacion.model.manager.ManagerFacturacion;
import facturacion.model.dao.entities.Producto;

@ManagedBean
@SessionScoped
public class BeanProductos {
	private List<Producto> listaProductos;
	@EJB
	private ManagerFacturacion managerFacturacion;
	
	private Integer codigoProducto;
	private String descripcion;
	private Integer existencia;
	private String nombre;
	private BigDecimal precioUnitario;
	private String tieneImpuesto;
	
	
	public BeanProductos(){
		
	}
	
	public String actionInsertarProducto(){
		Producto p= new Producto();
		p.setCodigoProducto(codigoProducto);
		p.setDescripcion(descripcion);
		p.setExistencia(existencia);
		p.setNombre(nombre);
		p.setPrecioUnitario(precioUnitario);
		p.setTieneImpuesto(tieneImpuesto);
		try {
			managerFacturacion.insertarProducto(p);
			//limpiamos las variables del formulario:
			codigoProducto=null;
			descripcion="";
			existencia=null;
			nombre="";
			precioUnitario=null;
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
		return "";
	}
	public String actionEliminarProducto(Producto producto){
		try {
			managerFacturacion.eliminarProducto(producto.getCodigoProducto());
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
		return "";
	}
	public String actionCargarProducto(Producto producto){
		codigoProducto=producto.getCodigoProducto();
		descripcion=producto.getDescripcion();
		nombre=producto.getNombre();
		precioUnitario=producto.getPrecioUnitario();
		existencia=producto.getExistencia();
		tieneImpuesto=producto.getTieneImpuesto();
		return "productos_update";
	}
	public String actionActualizarProducto(){
		Producto p=new Producto();
		p.setCodigoProducto(codigoProducto);
		p.setNombre(nombre);
		p.setDescripcion(descripcion);
		p.setPrecioUnitario(precioUnitario);
		p.setExistencia(existencia);
		p.setTieneImpuesto(tieneImpuesto);
		try {
			managerFacturacion.actualizarProducto(p);
			//limpiamos las variables del formulario:
			codigoProducto=null;
			descripcion="";
			existencia=null;
			nombre="";
			precioUnitario=null;
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
		return "productos";
	}
	
	public List<Producto> getListaProductos(){
		listaProductos=managerFacturacion.findAllProductos();
		return listaProductos;
	}
	public Integer getCodigoProducto() {
		return codigoProducto;
	}
	public void setCodigoProducto(Integer codigoProducto) {
		this.codigoProducto = codigoProducto;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Integer getExistencia() {
		return existencia;
	}
	public void setExistencia(Integer existencia) {
		this.existencia = existencia;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public BigDecimal getPrecioUnitario() {
		return precioUnitario;
	}
	public void setPrecioUnitario(BigDecimal precioUnitario) {
		this.precioUnitario = precioUnitario;
	}
	public String getTieneImpuesto() {
		return tieneImpuesto;
	}
	public void setTieneImpuesto(String tieneImpuesto) {
		this.tieneImpuesto = tieneImpuesto;
	}
	
	
}
