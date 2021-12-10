package it.tsa.WEB.controller;

import java.io.IOException;

import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.ejb.EJB;
import javax.persistence.NonUniqueResultException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import it.tsa.EJB.entities.Employee;
import it.tsa.EJB.exceptions.CredentialsException;
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
		
		try {
			name = request.getParameter("username");
			pwd = request.getParameter("password");
			
			if(name == null || pwd == null || name.isEmpty() || pwd.isEmpty()) {
				throw new Exception("Missing credential value");
			}
		}catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credential value");
			return;
		}
		
		Employee emp;
		try {
			emp = empService.checkCredentials(name, pwd);
		} catch(CredentialsException | NonUniqueResultException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Check credentials fail");
			return;
		}
		String path;
		if (emp == null) {
			ServletContext servletContext = getServletContext();
            final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
            ctx.setVariable("errMsg", "Incorrect username or password");
            path = "/employee/login.html";
            tmplEngine.process(path, ctx, response.getWriter());
            
		} else {
			request.getSession().setAttribute("employee", emp);
			path = getServletContext().getContextPath() + "/EmployeeHome";
			response.sendRedirect(path);
		}
	}

}
