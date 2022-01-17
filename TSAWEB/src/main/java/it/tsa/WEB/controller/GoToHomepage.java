package it.tsa.WEB.controller;

import java.io.IOException;
import java.util.ArrayList;
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
import it.tsa.EJB.entities.User;
import it.tsa.EJB.services.MiscService;

/**
 * Servlet implementation class GoToHomePage
 */
@WebServlet("/GoToHomepage")
public class GoToHomepage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private TemplateEngine templateEngine;
	private ServletContext servletContext;
	private WebContext ctx;

	@EJB(name = "project.services/MiscService")
	private MiscService miscService;

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

		User loggedUser = (User) request.getSession().getAttribute("user");

		ctx = new WebContext(request, response, servletContext, request.getLocale());

		if (loggedUser != null) {

			List<Order> allOrders = new ArrayList<Order>(loggedUser.getOrders());
			List<Order> rejectedOrders = new ArrayList<Order>();

			for (Order order : allOrders) {

				if (order.isRejectedFlag() || !order.isValidityFlag())
					rejectedOrders.add(order);
			}

			ctx.setVariable("rejectedOrders", rejectedOrders);

			ctx.setVariable("user", loggedUser);
			templateEngine.process(path, ctx, response.getWriter());
		} else {
			response.sendRedirect(servletContext.getContextPath() + "/BuyService");
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}

//rejectedOrders = loggedUser.getOrders().stream().filter(order -> order.isRejectedFlag())			
//.toList();