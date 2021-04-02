package br.com.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.entidades.Estados;
import br.com.jpaUtil.JpaUtil;

@FacesConverter(forClass = Estados.class, value="converterEstados")
public class converterEstado implements Converter ,Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String codigo) {
		
		EntityManager conexao = JpaUtil.getEntityManager();
		EntityTransaction transacao  = conexao.getTransaction();
		transacao.begin();
		Estados estados =(Estados)conexao.find(Estados.class, Long.parseLong(codigo));
		
		return estados;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object estado) {
		if(estado == null) {
			return null;
		}if(estado instanceof Estados) {
			return ((Estados)estado).getId().toString();
		}else {
			return estado.toString();
		}
		
	}

}
