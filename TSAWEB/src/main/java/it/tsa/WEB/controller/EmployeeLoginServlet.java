package it.tsa.WEB.controller;

import java.io.IOException;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import it.tsa.EJB.entities.Employee;
import it.tsa.EJB.services.EmployeeService;

/**
 * Servlet implementation class EmployeeLoginServlet
 */
@WebServlet("/EmployeeLogin")
public class EmployeeLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine tmplEngine;
	
	@EJB(name="it.tsa.EJB.services/EmployeeService")
	private EmployeeService empService;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeeLoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    

    public void init() throws ServletException {
    		ServletContext servletContext = getServletContext();
    		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
            templateResolver.setTemplateMode(TemplateMode.HTML);
            this.tmplEngine = new TemplateEngine();
            this.tmplEngine.setTemplateResolver(templateResolver);
            templateResolver.setSuffix(".html");
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name, pwd;
		
		ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

        String path;
		
		try {
			name = request.getParameter("username");
			pwd = request.getParameter("password");
			
			if(name == null || pwd == null || name.isEmpty() || pwd.isEmpty()) {
				throw new Exception("Missing credential value");
			}
			
			Employee emp;		
			emp = empService.checkCredentials(name, pwd);
			
			if(emp == null) {
				throw new Exception("Incorrect username or password");
			}
			
			// everything is ok
			request.getSession().setAttribute("employee", emp);
			path = getServletContext().getContextPath() + "/EmployeeHome";
			response.sendRedirect(path);
			
		}catch (Exception e) {
			path = "/employee/login.html";
			ctx.setVariable("errMsg", e.getMessage());
			tmplEngine.process(path, ctx, response.getWriter());
		
			return;
		}
		
	}

}
