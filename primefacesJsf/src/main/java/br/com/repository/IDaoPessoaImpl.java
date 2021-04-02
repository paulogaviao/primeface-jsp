package br.com.repository;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.entidades.Estados;
import br.com.entidades.Pessoa;
import br.com.jpaUtil.JpaUtil;

public class IDaoPessoaImpl implements IDaoPessoa {

	@Override
	public Pessoa consultarUsuario(String login, String senha) {
		Pessoa pessoa = null;
		EntityManager conexao = JpaUtil.getEntityManager();
		EntityTransaction transacao = conexao.getTransaction();
		transacao.begin();
		pessoa =(Pessoa) conexao.createQuery("select p from Pessoa p where p.login= '"+ login+"' and p.senha = '"+senha+"'").getSingleResult();
		transacao.commit();
		conexao.close();
		return pessoa;
	}

	@Override
	public List<SelectItem> listarTodos() {
		List<SelectItem>selectItem= new ArrayList<SelectItem>();
		
		EntityManager conexao = JpaUtil.getEntityManager();
		EntityTransaction transacao = conexao.getTransaction();
		transacao.begin();
		List<Estados>lista= conexao.createQuery("from Estados").getResultList();
		
		for (Estados estado : lista) {
			selectItem.add(new SelectItem(estado, estado.getNome()));
		}
		
		return selectItem;
	}

}
