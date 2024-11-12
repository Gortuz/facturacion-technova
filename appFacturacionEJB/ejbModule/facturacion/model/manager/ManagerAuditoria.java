package facturacion.model.manager;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import facturacion.model.dao.entities.Evento;

/**
 * Clase que implementa la logica de manejo de
 * pistas de auditoria.
 * @author mrea
 *
 */
@Stateless
@LocalBean
public class ManagerAuditoria {
	@EJB
	private ManagerDAO managerDAO;
	
	public ManagerAuditoria() {
		
	}
	
	/**
	 * Almacena la informacion de un evento en la tabla de auditoria.
	 * @param idUsuario Codigo del usuario que genero el evento.
	 * @param metodo Nombre del metodo que genero el evento.
	 * @param descripcion Informacion detallada del evento.
	 * @throws Exception 
	 */
	public void crearEvento(String idUsuario,String metodo,String descripcion) throws Exception{
		Evento evento=new Evento();
		
		if(idUsuario==null||idUsuario.length()==0)
			throw new Exception("Error auditoria: debe indicar el codigo del usuario.");
		if(metodo==null||metodo.length()==0)
			throw new Exception("Error auditoria: debe indicar el metodo que genera el evento.");

		evento.setIdUsuario(idUsuario);
		evento.setMetodo(metodo);
		evento.setDescripcion(descripcion);
		evento.setFechaEvento(new Date());
		
		managerDAO.insertar(evento);
	}

}
