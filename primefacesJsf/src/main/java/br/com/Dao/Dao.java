package br.com.Dao;

import java.util.List;

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

	public E merge(E entidade) {
		EntityManager connection = JpaUtil.getEntityManager();
		EntityTransaction transacao = connection.getTransaction();
		transacao.begin();
		E retorno = connection.merge(entidade);
		transacao.commit();
		connection.close();
		return retorno;
	}

	public void deletar(E entidade) {
		EntityManager connection = JpaUtil.getEntityManager();
		EntityTransaction transacao = connection.getTransaction();
		transacao.begin();
		connection.remove(entidade);
		transacao.commit();
		connection.close();
	}

	public void deletarPorId(E entidade) {
		EntityManager connection = JpaUtil.getEntityManager();
		EntityTransaction transacao = connection.getTransaction();
		transacao.begin();
		Object id = JpaUtil.getPrimaryKey(entidade);
		connection.createQuery("delete from " + entidade.getClass().getCanonicalName() + " where id = " + id)
				.executeUpdate();
		transacao.commit();
		connection.close();
	}

	public List<E> getListEntity(Class<E> entidade){
    EntityManager connection = JpaUtil.getEntityManager();
    EntityTransaction transacao = connection.getTransaction();
    transacao.begin();
    List<E> lista = connection.createQuery(" from "+ entidade.getName()).getResultList();
	transacao.commit();
	connection.close();
	return lista;
	}

}
