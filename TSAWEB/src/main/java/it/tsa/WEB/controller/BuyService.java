package it.tsa.WEB.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.tsa.EJB.entities.ServicePackage;
import it.tsa.EJB.entities.User;
import it.tsa.EJB.services.DbService;

/**
 * Servlet implementation class GoToHomePage
 */

@WebServlet("/BuyService")
public class BuyService extends HttpServlet{
	private static final long serialVersionUID = 1L;

	private TemplateEngine templateEngine;
	private ServletContext servletContext;
	private WebContext ctx;

	@EJB(name = "project.services/DbService")
	private DbService dbService;

	public BuyService() {
		super();
		// TODO Auto-generated constructor stub
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
		List<ServicePackage> servicePackages = null;
		servicePackages = dbService.findAllServicePackages();

		String path = "/service/buyservice.html";
		User loggedUser = (User) request.getSession().getAttribute("user");
		
		ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("servicePackages", servicePackages);
		ctx.setVariable("userIsLogged", loggedUser != null);
		templateEngine.process(path, ctx, response.getWriter());

	}
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}

