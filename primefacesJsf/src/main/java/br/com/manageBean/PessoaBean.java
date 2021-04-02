package br.com.manageBean;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;

import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.DatatypeConverter;

import com.google.gson.Gson;

import br.com.Dao.Dao;
import br.com.entidades.Cidades;
import br.com.entidades.Estados;
import br.com.entidades.Pessoa;
import br.com.jpaUtil.JpaUtil;
import br.com.repository.IDaoPessoa;
import br.com.repository.IDaoPessoaImpl;

@ViewScoped
@ManagedBean(name = "pessoaBean")

public class PessoaBean {
	/* instancia a classe pessoa e o dao */
	private Pessoa pessoa = new Pessoa();
	private Dao<Pessoa> dao = new Dao<Pessoa>();
	private List<Pessoa> pessoas = new ArrayList<Pessoa>();
	private IDaoPessoa daoPessoa = new IDaoPessoaImpl();
	private List<SelectItem> estados = new ArrayList<SelectItem>();
    private List<SelectItem>cidades = new ArrayList<SelectItem>();
    private Part arquivoFoto;
    
    
	/*
	 * no Dao salvar antes de retornar a pagina de se instanciar novamente uma
	 * pessoa para que ele possa salvar novamente
	 */
	/*
	 * no Dao merge ele vai injetar os atributos de pessoa no metodo e vai retornar
	 * para a pagina os dados
	 */
	public String salvar() throws IOException{
		/*começa processandoo o arquivo vindo da tela*/
/*inicio um aquivo byte*/     /*joga o arquivo vindo da tela para o metodo  */
		byte[] arquivo = getByte(arquivoFoto.getInputStream());
		pessoa.setFotooriginal(arquivo);/*salva np BD a foto original */
		/*começa o processo de transformar a imagem em uma miniatura para apresentar na tela */
		BufferedImage BF = ImageIO.read(new ByteArrayInputStream(arquivo));
		/*cria uma condição e injeta o tipo da imagem em "type"*/
		int type = BF.getType() == 0? BF.TYPE_INT_ARGB : BF.getType();
		
		/*criando a miniatura de imagem*/
		int altura= 200;
		int largura= 200;
		
		BufferedImage redimencionando = new BufferedImage(altura, largura,type);
		Graphics2D g = redimencionando.createGraphics();
		g.drawImage(BF, 0, 0, altura, largura, null);
		g.dispose();
		
		/*deve-se ler a imagem apartir desta linha */
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String extensao = arquivoFoto.getContentType().split("\\/")[1];/*formato:image/png com o split ele formata apartir da barra ["image"=0/"extensao" =1]*/
		ImageIO.write(redimencionando, extensao, baos);
		/*processando a imagem para mostrar na tela*/
		String miniImage = "data:"+arquivoFoto.getContentType()+";base64,"+
		DatatypeConverter.printBase64Binary(baos.toByteArray());
	/*	fim do processo*/
		/*finalizando seta os atributos na classe pessoa*/
		pessoa.setFotoIconBase64(miniImage);
		pessoa.setExtensao(extensao);
		
		
		pessoa = dao.merge(pessoa);
		
		listaPessoas();
		mostrarMsg("Salvo com sucesso!!!");
		return "";
	}

	private void mostrarMsg(String msg) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(msg);
		context.addMessage(null, message);
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
		mostrarMsg("Removido com sucesso!!");
		listaPessoas();
		return "";
	}

	@PostConstruct
	public void listaPessoas() {
		pessoas = dao.getListEntity(Pessoa.class);
	}

	public String deslogar() {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext ec = context.getExternalContext();
		ec.getSessionMap().remove("userLogado");
		HttpServletRequest sr = (HttpServletRequest) context.getCurrentInstance().getExternalContext().getRequest();
		sr.getSession().invalidate();

		return "index.jsf";
	}

	public String Logar() {
		Pessoa usuario = daoPessoa.consultarUsuario(pessoa.getLogin(),
				pessoa.getSenha());/* o IDaoPessoa intercepta esses dados e manda pro IDaoPssoaImpl */
		if (usuario != null) {
			/* encontrou o usuario */
			/* precisa adicionar o usuario a sessão */
			FacesContext context = FacesContext
					.getCurrentInstance();/* requerer uma informação do ambiente de execução */
			ExternalContext externalContext = context.getExternalContext();
			externalContext.getSessionMap().put("userLogado", usuario);
			return "primeiraPagina.jsf";
		} else {
			return"";
		}

	}

	public boolean permitirAcesso(String acesso) {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Pessoa pessoa = (Pessoa) externalContext.getSessionMap().get("userLogado");
		return pessoa.getPerfil().equals(acesso);
	}

	public void pesquisaCep(AjaxBehaviorEvent evento) { /* vai receber o ajax da tela jsf de cep */

		try {
			URL url = new URL("https://viacep.com.br/ws/" + pessoa.getCep() + "/json/"); /* cria url para conexao */
			URLConnection conexao = url.openConnection(); /* conecta ao servidor apara fazer a pesquisa */
			InputStream is = conexao.getInputStream(); /*
														 * pega os valores em bytes pesquisados na conexao e joga em um
														 * objeto para o tratamento
														 */
			BufferedReader br = new BufferedReader(
					new InputStreamReader(is, "UTF-8")); /* VAI LER E FORMATAR OS DADOS */
			String cep = "";
			StringBuilder json = new StringBuilder();
			while ((cep = br.readLine()) != null) { /* enquanto tiver linhas para ler vai montar uma string builder */
				json.append(cep); /* enquanto tiver linhas vai sendo adicionado ao StringBuilder */
			}
			Pessoa gsonAux = new Gson().fromJson(json.toString(), Pessoa.class);
			pessoa.setCep(gsonAux.getCep());
			pessoa.setLogradouro(gsonAux.getLogradouro());
			pessoa.setComplemento(gsonAux.getComplemento());
			pessoa.setBairro(gsonAux.getBairro());
			pessoa.setLocalidade(gsonAux.getLocalidade());
			pessoa.setUf(gsonAux.getUf());
			pessoa.setIbge(gsonAux.getIbge());
			pessoa.setGia(gsonAux.getGia());
			pessoa.setDdd(gsonAux.getDdd());
			pessoa.setSiafi(gsonAux.getSiafi());

		} catch (Exception e) {
			e.printStackTrace();
			mostrarMsg("Erro ao Pesquisar o Cep!");

		}

	}

	public List<SelectItem> getEstados() {
		estados = daoPessoa.listarTodos();
		return estados;
	}

	public List<SelectItem> getCidades() {
		return cidades;
	}
	public void setCidades(List<SelectItem> cidades) {
		this.cidades = cidades;
	}
	public void mostrarCidade(AjaxBehaviorEvent evento) {

		Estados estado = (Estados)((HtmlSelectOneMenu)evento.getSource()).getValue();
		
			if(estado != null) {
				pessoa.setEstados(estado);
				List<Cidades> cidades = JpaUtil.getEntityManager().createQuery("from Cidades where estados.id = "+estado.getId()).getResultList();
				List<SelectItem>selectCidade = new ArrayList<SelectItem>();
				for (Cidades cidade : cidades) {
					selectCidade.add(new SelectItem(cidade, cidade.getNome()));
				}
				setCidades(selectCidade);
			}	
	}

	public void editar() {
		if(pessoa.getCidade()!=null) {
			Estados estado = pessoa.getCidade().getEstado();
			pessoa.setEstados(estado);
			
			List<Cidades> cidades = JpaUtil.getEntityManager().createQuery("from Cidades where estados.id = "+estado.getId()).getResultList();
			List<SelectItem>selectCidade = new ArrayList<SelectItem>();
			for (Cidades cidade : cidades) {
				selectCidade.add(new SelectItem(cidade, cidade.getNome()));
			}
			setCidades(selectCidade);
		}
	}
	/*converte um inputStream em um array de bytes*/
	private byte[] getByte(InputStream is) throws IOException {
		/* variaveis de controle */
		int size = 1024;
		int len;
		byte[] buf = null;
		if (is instanceof ByteArrayInputStream) {
			try {
				size = is.available(); /* retorna a estimativa de tamanho do arquivo */
				buf = new byte[size];
				len = is.read(buf, 0, size);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			buf = new byte[size];

			while ((len = is.read(buf, 0, size)) != -1) {

				bos.write(buf, 0, len);
			}
			buf = bos.toByteArray();
		}
		return buf;

	}
	
	public void download() throws IOException {
		Map<String, String>params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String fileDownloadId = params.get("fileDownloadId");
		Pessoa pessoa = dao.buscar(Pessoa.class, fileDownloadId);
		/*tem que dar uma resposta para o navegador*/
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-Disposition","attachment;filename=download."+pessoa.getExtensao());/*cria um cabeçalho para o download expecificando o arquivo de retorno*/
		response.setContentType("application/octet-stream");/*seta o tipo de dados a ser feito o download o arquivo de midia*/
		response.setContentLength(pessoa.getFotooriginal().length);/*seta o tamanho do arquivo para o download*/
		response.getOutputStream().write(pessoa.getFotooriginal());/*vai ler o aarquivo */
		response.getOutputStream().flush();
		FacesContext.getCurrentInstance().responseComplete();/*informa o jsf que a ação foi completa */
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

	public Part getArquivoFoto() {
		return arquivoFoto;
	}

	public void setArquivoFoto(Part arquivoFoto) {
		this.arquivoFoto = arquivoFoto;
	}

}
