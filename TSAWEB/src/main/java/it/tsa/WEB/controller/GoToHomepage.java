package it.tsa.WEB.controller;

import java.io.IOException;
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

import it.tsa.EJB.entities.Order;
import it.tsa.EJB.entities.ServicePackage;
import it.tsa.EJB.entities.User;
import it.tsa.EJB.services.DbService;

/**
 * Servlet implementation class GoToHomePage
 */
@WebServlet("/GoToHomepage")
public class GoToHomepage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private TemplateEngine templateEngine;
	private ServletContext servletContext;
	private WebContext ctx;

	@EJB(name = "project.services/DbService")
	private DbService dbService;

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
		String path = "/service/homepage.html";
		List<Order> rejectedOrders;
//		System.out.println("FLOW: getGotohomepageservlet");

		User loggedUser = (User) request.getSession().getAttribute("user");
		System.out.println("Number of orders: " + loggedUser.getOrders().size());
		System.out.println("Insolvent: " + loggedUser.isInsolventFlag());
		rejectedOrders = loggedUser.getOrders().stream().filter(order -> order.isRejectedFlag()).toList();
		ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("rejectedOrders", rejectedOrders);

		// TODO how to make it nicer
		ctx.setVariable("user", loggedUser);
		ctx.setVariable("userIsLogged", loggedUser != null);

//		System.out.println("FLOW: callinghomepagehtml");
		templateEngine.process(path, ctx, response.getWriter());

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
