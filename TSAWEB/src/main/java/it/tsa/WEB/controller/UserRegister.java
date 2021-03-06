package it.tsa.WEB.controller;

import java.io.*;

import javax.ejb.EJB;
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

import it.tsa.EJB.services.UserService;

/**
 * Servlet implementation class CreatePhoto
 */
@WebServlet("/UserRegister")
public class UserRegister extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private TemplateEngine templateEngine;
	private ServletContext servletContext;
	private WebContext ctx;

	@EJB(name = "project.services/UserService")
	private UserService userService;

	public UserRegister() {
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
		String result;
		
		String username = StringEscapeUtils.escapeJava(request.getParameter("username"));
		String password = StringEscapeUtils.escapeJava(request.getParameter("password"));
		String email = StringEscapeUtils.escapeJava(request.getParameter("email"));

		boolean success = userService.createUser(username, password, email);
		
		if (!success)
			result = "Error occurred";
		else
			result = "Registration was successful";

		ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("registrationResult", result);
		templateEngine.process("/index.html", ctx, response.getWriter());
	
	}

}
