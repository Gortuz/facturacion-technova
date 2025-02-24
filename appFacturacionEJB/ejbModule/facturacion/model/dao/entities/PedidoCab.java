package facturacion.model.dao.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the pedido_cab database table.
 * 
 */
@Entity
@Table(name="pedido_cab")
@NamedQuery(name="PedidoCab.findAll", query="SELECT p FROM PedidoCab p")
public class PedidoCab implements Serializable {
	private static final long serialVersionUID = 1L;

	@GeneratedValue(generator="seq_pedido_cab")
	@SequenceGenerator(name="seq_pedido_cab",sequenceName="seq_pedido_cab", allocationSize=1)
	@Id
	@Column(name="numero_pedido", unique=true, nullable=false)
	private Integer numeroPedido;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_pedido", nullable=false)
	private Date fechaPedido;

	@Column(nullable=false, precision=12, scale=2)
	private BigDecimal subtotal;

	//bi-directional many-to-one association to Cliente
	@ManyToOne
	@JoinColumn(name="cedula_cliente", nullable=false)
	private Cliente cliente;

	//bi-directional many-to-one association to EstadoPedido
	@ManyToOne
	@JoinColumn(name="id_estado_pedido", nullable=false)
	private EstadoPedido estadoPedido;

	//bi-directional many-to-one association to PedidoDet
	@OneToMany(mappedBy="pedidoCab",cascade=CascadeType.ALL)
	private List<PedidoDet> pedidoDets;

	@Column(name="transaccion_pedido")
	private Integer transaccionPedido;
	
	@Column(name="transaccion_estado")
	private Integer transaccionEstado;
	
	public PedidoCab() {
	}

	public Integer getNumeroPedido() {
		return this.numeroPedido;
	}

	public void setNumeroPedido(Integer numeroPedido) {
		this.numeroPedido = numeroPedido;
	}

	public Date getFechaPedido() {
		return this.fechaPedido;
	}

	public void setFechaPedido(Date fechaPedido) {
		this.fechaPedido = fechaPedido;
	}

	public BigDecimal getSubtotal() {
		return this.subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public EstadoPedido getEstadoPedido() {
		return this.estadoPedido;
	}

	public void setEstadoPedido(EstadoPedido estadoPedido) {
		this.estadoPedido = estadoPedido;
	}

	public List<PedidoDet> getPedidoDets() {
		return this.pedidoDets;
	}

	public void setPedidoDets(List<PedidoDet> pedidoDets) {
		this.pedidoDets = pedidoDets;
	}

	public PedidoDet addPedidoDet(PedidoDet pedidoDet) {
		getPedidoDets().add(pedidoDet);
		pedidoDet.setPedidoCab(this);

		return pedidoDet;
	}

	public PedidoDet removePedidoDet(PedidoDet pedidoDet) {
		getPedidoDets().remove(pedidoDet);
		pedidoDet.setPedidoCab(null);

		return pedidoDet;
	}
	
	public Integer getTransaccionPedido() {
		return this.transaccionPedido;
	}

	public void setTransaccionPedido(Integer transaccionPedido) {
		this.transaccionPedido = transaccionPedido;
	}
	
	public Integer getTransaccionEstado() {
		return this.transaccionEstado;
	}

	public void setTransaccionEstado(Integer estadoPago) {
		this.transaccionEstado = estadoPago;
	}

}