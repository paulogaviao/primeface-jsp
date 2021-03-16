package br.com.Dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.jpaUtil.JpaUtil;

public class Dao<E> {

	
	
	public void salvar(E entidade) {
		EntityManager connection = JpaUtil.getEntityManager();
		EntityTransaction transacao = connection.getTransaction();
		transacao.begin();
		connection.persist(entidade);
		transacao.commit();
		connection.close();
	}
	
}
