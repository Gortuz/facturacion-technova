package facturacion.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import facturacion.model.manager.ManagerFacturacion;

import facturacion.model.dao.entities.Producto;
import java.io.Serializable;

@Named
@SessionScoped
public class BeanProductos implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<Producto> listaProductos;
	@EJB
	private ManagerFacturacion managerFacturacion;
	
	private Integer codigoProducto;
	private String descripcion;
	private Integer existencia;
	private String nombre;
	private BigDecimal precioUnitario;
	private String tieneImpuesto;
	private Producto productoSelecionado;
	private String filtro;
	
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
	public String actionEliminarProducto(){
		return actionEliminarProducto(productoSelecionado);
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
	
	public String actionObtenerProductoSeleccionado(Producto producto){
		try {
			//capturamos el valor enviado desde el DataTable:
			productoSelecionado = producto;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public List<Producto> getListaProductos(){
		if (filtro == null || filtro.isEmpty()) {
	        listaProductos = managerFacturacion.findAllProductos();
	    } else {
	        listaProductos = managerFacturacion.findAllProductosByFilter(filtro);
	    }
		return listaProductos;
	}
	public String filtrarListaProductos(){
		return "";
	}
	public String limpiarFiltro() {
		setFiltro(null);
		return "";
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

	public Producto getProductoSelecionado() {
		return productoSelecionado;
	}

	public void setProductoSelecionado(Producto productoSelecionado) {
		this.productoSelecionado = productoSelecionado;
	}
	public String getFiltro() {
		return filtro;
	}
	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}	
}
