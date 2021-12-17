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

import it.tsa.EJB.entities.Order;
import it.tsa.EJB.entities.User;
import it.tsa.EJB.exceptions.LoginErrorException;
import it.tsa.EJB.services.DbService;
import it.tsa.EJB.services.UserService;

/**
 * Servlet implementation class CreatePhoto
 */
@WebServlet("/UserLogin")
public class UserLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private TemplateEngine templateEngine;
	private ServletContext servletContext;
	private WebContext ctx;
	
	@EJB(name = "project.services/UserService")
	private UserService userService;

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
			User success = userService.checkCredentials(username, password);
			Order createdOrder = (Order) request.getSession().getAttribute("order");
			request.getSession().removeAttribute("onlyLogin");

			// go to homepage calling GoToHomepage servlet
			request.getSession().setAttribute("user", success);
			if(createdOrder != null)
				path = servletContext.getContextPath() + "/BuyService";
			else
				path = servletContext.getContextPath() + "/GoToHomepage";
			response.sendRedirect(path);
			
		} catch (LoginErrorException e) {

			ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("errorMsgLogin", "Wrong us or pass");
			path = "/index.html";
			templateEngine.process(path, ctx, response.getWriter());
		
		}
	}

}
