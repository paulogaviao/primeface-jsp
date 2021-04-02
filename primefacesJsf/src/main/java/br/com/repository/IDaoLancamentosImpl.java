package br.com.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.entidades.Lancamento;
import br.com.jpaUtil.JpaUtil;

public class IDaoLancamentosImpl implements IDaoLancamentos {

	@Override
	public List<Lancamento> consultar(Long codUser) {
		List<Lancamento>lista = null;
		
		EntityManager conexao = JpaUtil.getEntityManager();
		EntityTransaction transacao = conexao.getTransaction();
		
		transacao.begin();
		lista = conexao.createQuery(" from Lancamento where usuario.id = "+codUser).getResultList();
		transacao.commit();
		conexao.close();
		return lista;
	}

}
