package br.com.manageBean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import br.com.Dao.Dao;
import br.com.entidades.Lancamento;
import br.com.entidades.Pessoa;
import br.com.repository.IDaoLancamentos;
import br.com.repository.IDaoLancamentosImpl;

@ViewScoped
@ManagedBean
public class LancamentoBean {

	private Lancamento lancamento = new Lancamento();
	private Dao<Lancamento> daoLanc = new Dao<Lancamento>();
	private List<Lancamento> lancamentos = new ArrayList<Lancamento>();
    private IDaoLancamentos Idao = new IDaoLancamentosImpl();
	public Lancamento getLancamento() {
		return lancamento;
	}

	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}

	public Dao<Lancamento> getDaoLanc() {
		return daoLanc;
	}

	public void setDaoLanc(Dao<Lancamento> daoLanc) {
		this.daoLanc = daoLanc;
	}

	public List<Lancamento> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(List<Lancamento> lancamentos) {
		this.lancamentos = lancamentos;
	}

	public String salvar() {
		/* recupera Usuario logado na sessão */
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Pessoa usuario = (Pessoa) externalContext.getSessionMap().get("userLogado"); /*adiciona o usuario ao objto para setar na nota fiscal*/
		lancamento.setUsuario(usuario);/*seta o usuario na nota */
		daoLanc.merge(lancamento); 
		
		carregarLancamentos();

		return "";
	}
	@PostConstruct
	private void carregarLancamentos() {
		
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Pessoa usuario = (Pessoa) externalContext.getSessionMap().get("userLogado");
		lancamentos = Idao.consultar(usuario.getId());
	}
	
	public String novo() {
		
		lancamento = new Lancamento();
		carregarLancamentos();
		
		return "";
	}
	public String deletar() {
		
		daoLanc.deletarPorId(lancamento);
		lancamento = new Lancamento();
		carregarLancamentos();
		
		return"";
	}

}
