package it.tsa.WEB.controller;

import java.io.*;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.tsa.EJB.entities.User;
import it.tsa.EJB.exceptions.LoginErrorException;
import it.tsa.EJB.services.DbService;

/**
 * Servlet implementation class CreatePhoto
 */
@WebServlet("/UserLogin")
public class UserLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private TemplateEngine templateEngine;
	private ServletContext servletContext;
	private WebContext ctx;

	@EJB(name = "project.services/DbService")
	private DbService dbService;

	public UserLogin() {
		super();
	}

	public void init() throws ServletException {
		servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String path;

		// get values from html page
		String username = StringEscapeUtils.escapeJava(request.getParameter("username"));
		String password = StringEscapeUtils.escapeJava(request.getParameter("password"));

		try {
			User success = dbService.checkCredentials(username, password);
/*
 stateful service defined by teacher, useless to us so far
 
			QueryService qService = null;
			try {

				/*
				 * We need one distinct EJB for each user. Get the Initial Context for the JNDI
				 * lookup for a local EJB. Note that the path may be different in different EJB
				 * environments. In IntelliJ use: ic.lookup(
				 * "java:/openejb/local/ArtifactFileNameWeb/ArtifactNameWeb/QueryServiceLocalBean"
				 * );
				 */
/*
				InitialContext ic = new InitialContext(); // Retrieve the EJB using JNDI lookup 
				qService = (QueryService) ic.lookup("java:/openejb/local/QueryServiceLocalBean");
			} catch (Exception e) {
				e.printStackTrace();

			}
			request.getSession().setAttribute("queryService", qService);
*/
			// go to homepage calling GoToHomepage servlet
			request.getSession().setAttribute("user", success);
			path = getServletContext().getContextPath() + "/GoToHomepage";
			response.sendRedirect(path);

		} catch (LoginErrorException e) {

			ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("errorMsgLogin", "Wrong us or pass");
			String paath = "/index.html";
			templateEngine.process(paath, ctx, response.getWriter());
		
		}
	}

}
