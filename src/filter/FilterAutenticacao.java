package filter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import connection.SingleConnectionBanco;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@WebFilter(urlPatterns = {"/principal/*"}) // Intercepta todas as requisições que vierem do projeto ou mapeamento
public class FilterAutenticacao implements Filter {
	
	private static Connection connection;
	
	

    public FilterAutenticacao() {
    }

    // Encerra processos ao parar o servidor
    public void destroy() {
    	
    	try {
			connection.close();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
    	
    	
    	
    	
    }

    // Intercepta todas as requisições e respostas do sistema
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    	
    	try {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();

        String usuarioLogado = (String) session.getAttribute("usuario");

        String urlParaAutenticar = req.getServletPath();

        if (usuarioLogado == null&&
                !urlParaAutenticar.equalsIgnoreCase("/principal/ServletLogin")) {

            RequestDispatcher redireciona = request.getRequestDispatcher("/index.jsp?url=" + urlParaAutenticar);
            request.setAttribute("msg", "Por favor, realize o login");
            redireciona.forward(request, response);
            return;
        } else {
            chain.doFilter(request, response);
        }
    
        
        connection.commit();
        
        
        
        
        
        
    	}catch (Exception e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				
				e1.printStackTrace();
			}
			
		}
    	}

    // Inicia processos ou recursos quando o servidor sobe o projeto
    public void init(FilterConfig fConfig) throws ServletException {
    	
    	connection= SingleConnectionBanco.getConnection();
    	
    		
    	
    	
    }
}
