package facturacion.controller;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import facturacion.model.manager.ManagerLogin;

@ManagedBean
@SessionScoped
public class BeanLogin {
	private String codigoUsuario;
	private String clave;
	private String tipoUsuario;
	private boolean acceso;
	@EJB
	private ManagerLogin managerLogin;

	public BeanLogin() {
		managerLogin=new ManagerLogin();
	}
	/**
	 * Action que permite el acceso al sistema.
	 * @return
	 */
	public String accederSistema(){
		acceso=false;
		try {
			//verificamos el acceso del usuario:
			tipoUsuario=managerLogin.accederSistema(codigoUsuario, clave);
			//por seguridad borramos la clave en la sesion:
			clave="";
			//dependiendo del tipo de usuario, direccionamos:
			if(tipoUsuario.equals("SP")){
				acceso=true;
				return "supervisor/index";
			}
			if(tipoUsuario.equals("VD")){
				acceso=true;
				return "vendedor/facturacion";
			}
			//caso contrario, ocurrio un error con el tipo de usuario:
			JSFUtil.crearMensajeERROR("Error al acceder (tipo de usuario).");
			return "";
		} catch (Exception e) {
			e.printStackTrace();
			JSFUtil.crearMensajeERROR(e.getMessage());
		}
		return "";
	}
	
	/**
	 * Finaliza la sesion web del usuario.
	 * @return
	 */
	public String salirSistema(){
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "/index.xhtml?faces-redirect=true";
	}

	public String getCodigoUsuario() {
		return codigoUsuario;
	}

	public void setCodigoUsuario(String codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public boolean isAcceso() {
		return acceso;
	}

	public String getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}
	
}
