package facturacion.model.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import facturacion.model.dao.entities.Cliente;
import facturacion.model.dao.entities.FacturaCab;
import facturacion.model.dao.entities.FacturaDet;
import facturacion.model.dao.entities.Parametro;
import facturacion.model.dao.entities.PedidoCab;
import facturacion.model.dao.entities.PedidoDet;
import facturacion.model.dao.entities.Producto;
/**
 * Clase que implementa las reglas de negocio relacionadas al sistema de facturacion.
 * @author mrea
 *
 */
@Stateless
@LocalBean
public class ManagerFacturacion {
	@EJB
	private ManagerDAO managerDAO;
	
	
	public ManagerFacturacion(){
		
	}
	
	//MANEJO DE PRODUCTOS:
	
	public int obtenerExistencia(Integer codigoProducto) throws Exception{
		Producto p;
		p=findProductoById(codigoProducto);
		return p.getExistencia().intValue();
	}
	/**
	 * Metodo finder para consulta de productos.
	 * Hace uso del componente {@link facturacion.model.manager.ManagerDAO ManagerDAO} de la capa model.
	 * @return listado de Productos ordenados por nombre.
	 */
	@SuppressWarnings("unchecked")
	public List<Producto> findAllProductos(){
		return managerDAO.findAll(Producto.class, "o.nombre");
	}
	
	/**
	 * Metodo finder para consulta de productos.
	 * Hace uso del componente {@link facturacion.model.manager.ManagerDAO ManagerDAO} de la capa model.
	 * @param codigoProducto codigo del producto que se desea buscar.
	 * @return el producto encontrado.
	 * @throws Exception
	 */
	public Producto findProductoById(Integer codigoProducto) throws Exception{
		return (Producto) managerDAO.findById(Producto.class, codigoProducto);
	}
	
	/**
	 * Guarda un nuevo producto en la base de datos.
	 * Hace uso del componente {@link facturacion.model.manager.ManagerDAO ManagerDAO} de la capa model.
	 * @param p El nuevo producto.
	 * @throws Exception
	 */
	public void insertarProducto(Producto p) throws Exception{
		managerDAO.insertar(p);
	}
	
	/**
	 * Borra de la base de datos un producto especifico.
	 * Hace uso del componente {@link facturacion.model.manager.ManagerDAO ManagerDAO} de la capa model.
	 * @param codigoProducto el codigo del producto que se desea eliminar.
	 * @throws Exception
	 */
	public void eliminarProducto(Integer codigoProducto) throws Exception{
		managerDAO.eliminar(Producto.class, codigoProducto);
	}
	
	/**
	 * Actualiza la informacion de un producto en la base de datos.
	 * Hace uso del componente {@link facturacion.model.manager.ManagerDAO ManagerDAO} de la capa model.
	 * @param producto Los datos del producto que se desea actualizar.
	 * @throws Exception
	 */
	public void actualizarProducto(Producto producto) throws Exception{
		Producto p=null;
		try {
			//buscamos el producto a modificar desde la bdd:
			p=findProductoById(producto.getCodigoProducto());
			//actualizamos las propiedades:
			p.setDescripcion(producto.getDescripcion());
			p.setExistencia(producto.getExistencia());
			p.setNombre(producto.getNombre());
			p.setPrecioUnitario(producto.getPrecioUnitario());
			p.setTieneImpuesto(producto.getTieneImpuesto());
			//actualizamos:
			managerDAO.actualizar(p);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	
	//MANEJO DE PARAMETROS:
	
	/**
	 * Metodo finder para la consulta de parametros.
	 * Hace uso del componente {@link facturacion.model.manager.ManagerDAO ManagerDAO} de la capa model.
	 * @return listado de parametros.
	 */
	@SuppressWarnings("unchecked")
	public List<Parametro> findAllParametros(){
		return managerDAO.findAll(Parametro.class);
	}
	/**
	 * Obtiene el valor actual para el porcentaje de impuesto IVA.
	 * @return valor del IVA
	 */
	public double getPorcentajeIVA(){
		Parametro parametro;
		try {
			parametro=(Parametro)managerDAO.findById(Parametro.class, "valor_iva");
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return Double.parseDouble(parametro.getValorParametro());
		
	}
	
	/**
	 * Retorna el valor actual del contador de facturas. 
	 * Este contador es un parametro del sistema.
	 * @return ultimo valor del contador de facturas
	 * @throws Exception
	 */
	private int getContFacturas() throws Exception{
		int contFacturas=0;
		Parametro parametro=null;
		try {
			parametro=(Parametro)managerDAO.findById(Parametro.class, "cont_facturas");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Revise el parametro 'cont_facturas': "+e.getMessage());
		}
		contFacturas=Integer.parseInt(parametro.getValorParametro());
		return contFacturas;
	}
	
	/**
	 * Retorna el valor actual del contador de los detalles de facturas. 
	 * Este contador es un parametro del sistema.
	 * @return ultimo valor del contador del detalle de facturas
	 * @throws Exception
	 */
	private int getContFacturasDet() throws Exception{
		int contFacturasDet=0;
		Parametro parametro=null;
		try {
			parametro=(Parametro)managerDAO.findById(Parametro.class, "cont_facturas_det");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Revise el parametro 'cont_facturas_det': "+e.getMessage());
		}
		contFacturasDet=Integer.parseInt(parametro.getValorParametro());
		return contFacturasDet;
	}
	
	/**
	 * Actualiza el valor del contador de facturas.
	 * @param nuevoContadorFacturas nuevo valor del contador.
	 * @throws Exception
	 */
	private void actualizarContFacturas(int nuevoContadorFacturas) throws Exception{
		Parametro parametro=null;
		try {
			parametro=(Parametro)managerDAO.findById(Parametro.class, "cont_facturas");
			parametro.setValorParametro(Integer.toString(nuevoContadorFacturas));
			managerDAO.actualizar(parametro);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al actualizar el parametro 'cont_facturas': "+e.getMessage());
		}
	}
	
	/**
	 * Actualiza el contador del detalle de facturas.
	 * @param nuevoContadorFacturasDet nuevo valor del contador.
	 * @throws Exception
	 */
	private void actualizarContFacturasDet(int nuevoContadorFacturasDet) throws Exception{
		Parametro parametro=null;
		try {
			parametro=(Parametro)managerDAO.findById(Parametro.class, "cont_facturas_det");
			parametro.setValorParametro(Integer.toString(nuevoContadorFacturasDet));
			managerDAO.actualizar(parametro);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al actualizar el parametro 'cont_facturas_det': "+e.getMessage());
		}
	}
	
	//MANEJO DE CLIENTES:
	
	/**
	 * Metodo finder para la consulta de clientes.
	 * Hace uso del componente {@link facturacion.model.manager.ManagerDAO ManagerDAO} de la capa model.
	 * @return listado de clientes ordenados por apellidos.
	 */
	@SuppressWarnings("unchecked")
	public List<Cliente> findAllClientes(){
		return managerDAO.findAll(Cliente.class, "o.apellidos");
	}
	
	/**
	 * Metodo finder para la consulta de un cliente especifico.
	 * @param cedula cedula del cliente que se desea buscar.
	 * @return datos del cliente.
	 * @throws Exception
	 */
	public Cliente findClienteById(String cedula) throws Exception{
		Cliente cliente=null;
		try {
			cliente=(Cliente)managerDAO.findById(Cliente.class, cedula);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al buscar cliente: "+e.getMessage());
		}
		return cliente;
	}
	
	/**
	 * Permite guardar un nuevo cliente en la BDD.
	 * @param cedula Cedula del nuevo cliente.
	 * @param nombres Los nombres.
	 * @param apellidos Los apellidos.
	 * @param direccion La direccion del cliente.
	 * @param clave La clave o password.
	 * @throws Exception
	 */
	public void insertarCliente(String cedula,String nombres,
			String apellidos,String direccion, String clave) throws Exception{
		Cliente c=new Cliente();
		c.setCedulaCliente(cedula);
		c.setNombres(nombres);
		c.setApellidos(apellidos);
		c.setDireccion(direccion);
		c.setClave(clave);
		managerDAO.insertar(c);
	}
	
	//MANEJO DE FACTURAS:
	
	/**
	 * Metodo finder para la consulta de facturas.
	 * Hace uso del componente {@link facturacion.model.manager.ManagerDAO ManagerDAO} de la capa model.
	 * @return Listado de facturas ordenadas por fecha de emision y numero de factura.
	 */
	@SuppressWarnings("unchecked")
	public List<FacturaCab> findAllFacturaCab(){
		List<FacturaCab> listado= managerDAO.findAll(FacturaCab.class, "o.fechaEmision desc,o.numeroFactura desc");
		//recorremos las facturas cabecera para extraer los datos de los detalles:
		for(FacturaCab fc:listado){
			for(FacturaDet fd:fc.getFacturaDets()){
				fd.getCantidad();
			}
		}
		return listado;
	}
	
	/**
	 * Crea una nueva cabecera de factura temporal, para que desde el programa
	 * cliente pueda manipularla y llenarle con la informacion respectiva.
	 * Esta informacion solo se mantiene en memoria.
	 * @return la nueva factura temporal.
	 */
	public FacturaCab crearFacturaTmp(){
		FacturaCab facturaCabTmp=new FacturaCab();
		facturaCabTmp.setFechaEmision(new Date());
		facturaCabTmp.setFacturaDets(new ArrayList<FacturaDet>());
		return facturaCabTmp;
	}
	
	/**
	 * Asigna un cliente a una factura temporal.
	 * @param facturaCabTmp Factura temporal creada en memoria.
	 * @param cedulaCliente codigo del cliente.
	 * @throws Exception
	 */
	public void asignarClienteFacturaTmp(FacturaCab facturaCabTmp,String cedulaCliente) throws Exception{
		
		Cliente cliente=null;
		if(cedulaCliente==null||cedulaCliente.length()==0)
			throw new Exception("Error debe especificar la cedula del cliente.");
		try {
			cliente=findClienteById(cedulaCliente);
			if(cliente==null)
				throw new Exception("Error al asignar cliente.");
			facturaCabTmp.setCliente(cliente);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al asignar cliente: "+e.getMessage());
		}
	}
	
	/**
	 * Realiza los calculos de subtotales, impuestos y totales.
	 * @param facturaCabTmp Factura temporal creada en memoria.
	 */
	private void calcularFacturaTmp(FacturaCab facturaCabTmp){
		double sumaSubtotales;
		double porcentajeIVA,valorIVA,totalFactura;
		//verificamos los campos calculados:
		sumaSubtotales=0;
		for(FacturaDet det:facturaCabTmp.getFacturaDets()){
			sumaSubtotales+= det.getCantidad().intValue() * det.getPrecioUnitarioVenta().doubleValue();
		}
		
		porcentajeIVA=getPorcentajeIVA();
		valorIVA=sumaSubtotales*porcentajeIVA/100;
		totalFactura=sumaSubtotales+valorIVA;
		
		facturaCabTmp.setSubtotal(new BigDecimal(sumaSubtotales));
		facturaCabTmp.setValorIva(new BigDecimal(valorIVA));
		facturaCabTmp.setBaseCero(new BigDecimal(0));//no calculamos la base cero en este ejemplo.
		facturaCabTmp.setTotal(new BigDecimal(totalFactura));
	}
	
	/**
	 * Adiciona un item detalle a una factura temporal. Estos valores permanecen
	 * en memoria. 
	 * @param codigoProducto codigo del producto.
	 * @param cantidad cantidad del producto.
	 * @throws Exception problemas ocurridos al momento de insertar el item detalle.
	 */
	public void agregarDetalleFacturaTmp(FacturaCab facturaCabTmp,Integer codigoProducto,Integer cantidad) throws Exception{
		Producto p;
		FacturaDet fd;	
		
		if(facturaCabTmp==null)
			throw new Exception("Error primero debe crear una nueva factura.");
		if(codigoProducto==null||codigoProducto.intValue()<0)
			throw new Exception("Error debe especificar el codigo del producto.");
		if(cantidad==null||cantidad.intValue()<=0)
			throw new Exception("Error debe especificar la cantidad del producto.");
		
		//buscamos el producto:
		p=findProductoById(codigoProducto);
		//creamos un nuevo detalle y llenamos sus propiedades:
		fd=new FacturaDet();
		fd.setCantidad(cantidad);
		fd.setPrecioUnitarioVenta(p.getPrecioUnitario());
		fd.setProducto(p);
		facturaCabTmp.getFacturaDets().add(fd);
		
		//verificamos los campos calculados:
		calcularFacturaTmp(facturaCabTmp);
	}
	
	/**
	 * Guarda en la base de datos una factura.
	 * @param facturaCabTmp factura temporal creada en memoria.
	 * @throws Exception problemas ocurridos en la insercion.
	 */
	public void guardarFacturaTemporal(FacturaCab facturaCabTmp) throws Exception{
		
		if(facturaCabTmp==null)
			throw new Exception("Debe crear una factura primero.");
		if(facturaCabTmp.getFacturaDets()==null || facturaCabTmp.getFacturaDets().size()==0)
			throw new Exception("Debe ingresar los productos en la factura.");
		if(facturaCabTmp.getCliente()==null)
			throw new Exception("Debe registrar el cliente.");

		facturaCabTmp.setFechaEmision(new Date());
		
		//obtenemos el numero de la nueva factura:
		int contFacturas;
		contFacturas=getContFacturas();
		contFacturas++;
		facturaCabTmp.setNumeroFactura(Integer.toString(contFacturas));
		
		//verificamos los campos calculados:
		calcularFacturaTmp(facturaCabTmp);
		
		//asignamos la clave primaria a los detalles:
		int contFacturasDet;
		contFacturasDet=getContFacturasDet();
		
		for(FacturaDet det:facturaCabTmp.getFacturaDets()){
			contFacturasDet++;
			det.setNumeroFacturaDet(new Integer(contFacturasDet));
			//vinculamos el detalle a la cabecera (relacion bidireccional):
			det.setFacturaCab(facturaCabTmp);
		}
		
		//guardamos la factura completa en la bdd:
		managerDAO.insertar(facturaCabTmp);
		
		//actualizamos los parametros contadores de facturas:
		actualizarContFacturas(contFacturas);
		actualizarContFacturasDet(contFacturasDet);
		
		facturaCabTmp=null;		
		
	}
	/**
	 * Permite generar una factura a partir de un pedido de compra
	 * de un cliente. Este metodo reutiliza toda la logica de negocio
	 * que previamente fue implementada en ManagerFacturacion.
	 * @param pedidoCab El pedido del cliente.
	 * @throws Exception
	 */
	public void crearFacturaConPedido(PedidoCab pedidoCab) throws Exception{
		if(pedidoCab==null)
			throw new Exception("Debe crear un pedido primero.");

		//Creamos una factura temporal:
		FacturaCab facturaCabTmp=crearFacturaTmp();
		//Asignamos la informacion de cliente:
		asignarClienteFacturaTmp(facturaCabTmp, pedidoCab.getCliente().getCedulaCliente());
		//Agregamos los productos:
		for(PedidoDet pd:pedidoCab.getPedidoDets()){
			agregarDetalleFacturaTmp(facturaCabTmp, pd.getProducto().getCodigoProducto(), pd.getCantidad());
		}
		//Finalmente guardamos la nueva factura:
		guardarFacturaTemporal(facturaCabTmp);
	}
	
}
