package br.com.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import br.com.entidades.Pessoa;
import br.com.jpaUtil.JpaUtil;
/*essa anotação intercepta todas as paginas para fazer o filtro*/
@WebFilter(urlPatterns = {"/*"})
public class FilterAutenticacao implements Filter {

	@Override
	public void destroy() {
		
	}
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		JpaUtil.getEntityManager();
	}
	
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		/*cria uma requisição http */
		HttpServletRequest req = (HttpServletRequest) request;
		/*abre uma sessão*/
		HttpSession session = req.getSession();
		/*verifica se usuario esta logado */
		Pessoa userLogado = (Pessoa) session.getAttribute("userLogado");
		String url = req.getServletPath();
		if(!url.equalsIgnoreCase("index.jsf") && userLogado==null) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsf");
			dispatcher.forward(request, response);
			return;
		}else {
			chain.doFilter(request, response);
		}
	}

}
