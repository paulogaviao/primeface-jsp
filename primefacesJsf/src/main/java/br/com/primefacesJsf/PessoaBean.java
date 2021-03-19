package br.com.primefacesJsf;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import javax.faces.bean.ViewScoped;

import br.com.Dao.Dao;
import br.com.entidades.Pessoa;

@ViewScoped
@ManagedBean(name = "pessoaBean")

public class PessoaBean {
	/* instancia a classe pessoa e o dao */
	private Pessoa pessoa = new Pessoa();
	private Dao<Pessoa> dao = new Dao<Pessoa>();
	private List<Pessoa> pessoas = new ArrayList<Pessoa>();

	/*
	 * no Dao salvar antes de retornar a pagina de se instanciar novamente uma
	 * pessoa para que ele possa salvar novamente
	 */
	/*
	 * no Dao merge ele vai injetar os atributos de pessoa no metodo e vai retornar
	 * para a pagina os dados
	 */
	public String salvar() {
		pessoa = dao.merge(pessoa);
	  	listaPessoas();
		return "";
	}

	/*
	 * nesse metodo ira criar instanciar uma nova pessoa para ser salva no Banco de
	 * dados
	 */
	public String novo() {
		pessoa = new Pessoa();
		return "";
	}

	public String deletar() {
		dao.deletarPorId(pessoa);
		pessoa = new Pessoa();
		listaPessoas();
		return "";
	}
	@PostConstruct
	public void listaPessoas() {
		pessoas = dao.getListEntity(Pessoa.class);
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Dao<Pessoa> getDao() {
		return dao;
	}

	public void setDao(Dao<Pessoa> dao) {
		this.dao = dao;
	}

	public List<Pessoa> getPessoas() {
		return pessoas;
	}

}
