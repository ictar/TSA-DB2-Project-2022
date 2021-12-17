package it.tsa.WEB.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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

import it.tsa.EJB.entities.Order;
import it.tsa.EJB.entities.ServicePackage;
import it.tsa.EJB.entities.User;
import it.tsa.EJB.services.DbService;
import it.tsa.EJB.services.OrderService;
import it.tsa.EJB.services.UserService;

/**
 * Servlet implementation class GoToHomePage
 */
@WebServlet("/OrderConfirmation")
public class OrderConfirmation extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private TemplateEngine templateEngine;
	private ServletContext servletContext;
	private WebContext ctx;

	@EJB(name = "project.services/OrderService")
	private OrderService orderService;

	public void init() throws ServletException {
		servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Order order = (Order) request.getSession().getAttribute("order");
		User user = (User) request.getSession().getAttribute("user");
		String path = "/service/homepage.html";

		if (request.getParameter("deleteOrder") != null) {
			System.out.println("in deleteOrder");
			request.getSession().removeAttribute("order");
			
			path = servletContext.getContextPath() + "/GoToHomepage";
			System.out.println("before send redirect");
			response.sendRedirect(path);
		} else if (request.getParameter("login") != null || user == null) {

			request.getSession().setAttribute("onlyLogin", true);
			path = servletContext.getContextPath() + "/GoToLogin";
			response.sendRedirect(path);
		} else {

			// maybe stateful?
			orderService.confirmOrder(order);

			if (request.getParameter("valid") != null) {
				// payment ok
				orderService.orderActivated(order);
			} else if (request.getParameter("notValid") != null) {
				// payment wrong
				orderService.orderRejected(order, user);
			} else if (request.getParameter("random") != null) {
				// generate Random
			} else {
				// possible
				System.out.println("Entered in Third else in orderconfirmation.dopost(.)<");

			}
			ctx = new WebContext(request, response, servletContext, request.getLocale());
			templateEngine.process(path, ctx, response.getWriter());
		}

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

}
