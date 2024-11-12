package facturacion.model.manager;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import facturacion.model.dao.entities.Usuario;

/**
 * Manager para el manejo de autentificacion de usuarios.
 * @author mrea
 *
 */
@Stateless
@LocalBean
public class ManagerLogin {
	@EJB
	private ManagerDAO managerDAO;
	
	public ManagerLogin() {
		
	}
	
	/**
	 * Metodo que le permite a un usuario acceder al sistema.
	 * @param codigoUsuario Identificador del usuario.
	 * @param clave Clave de acceso.
	 * @return Retorna el tipo de usuario. Puede tener dos valores:
	 * 			SP (supervisor) o VD (vendedor).
	 * @throws Exception Cuando no coincide la clave proporcionada o si ocurrio
	 * un error con la consulta a la base de datos.
	 */
	public String accederSistema(String codigoUsuario,String clave) throws Exception{
		Usuario usuario=(Usuario)managerDAO.findById(Usuario.class, codigoUsuario);
		
		if(usuario==null)
			throw new Exception("Usuario no existe.");
		
		if(!usuario.getClave().equals(clave))
			throw new Exception("No coincide la clave.");
		
		return usuario.getTipoUsuario();
	}
	
}
