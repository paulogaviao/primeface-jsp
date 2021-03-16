package br.com.primefacesJsf;



import javax.faces.bean.ManagedBean;

import javax.faces.bean.ViewScoped;

import br.com.Dao.Dao;
import br.com.entidades.Pessoa;



@ViewScoped
@ManagedBean(name="pessoaBean")

public class PessoaBean {
    /*instancia  a classe pessoa  e o dao*/
	private Pessoa pessoa = new Pessoa();
	private Dao<Pessoa> dao = new Dao<Pessoa>();
	/*antes de retornar a pagina de se instanciar novamente uma pessoa para que ele possa salvar novamente*/
	public String salvar() {
		dao.salvar(pessoa);
		pessoa = new Pessoa();
		return "";
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
	
	
	

}

	
	

