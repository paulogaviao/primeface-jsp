package br.com.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.entidades.Cidades;
import br.com.jpaUtil.JpaUtil;

@FacesConverter(forClass = Cidades.class , value="converterCidades")
public class ConverterCidades implements Converter, Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String codigocidade) {
		
		EntityManager conexao = JpaUtil.getEntityManager();
		EntityTransaction transacao = conexao.getTransaction();
		transacao.begin();
		Cidades cidade = conexao.find(Cidades.class, Long.parseLong(codigocidade));
		
		
		return cidade;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object cidade) {
		if(cidade == null) {
			return null;
		}
		if(cidade instanceof Cidades) {
		return ((Cidades)cidade).getId().toString();
		}else {
			return cidade.toString();
		}
	}

}
