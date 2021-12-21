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

//	@EJB(name = "project.services/UserService")
//	private UserService userService;

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

		System.out.println("FLOW: post OrderConfirmation");
		Order order = (Order) request.getSession().getAttribute("order");
		User user = (User) request.getSession().getAttribute("user");
		String path;

		if (request.getParameter("deleteOrder") != null) {

			System.out.println("FLOW: want to delete order created");
			request.getSession().removeAttribute("order");

			path = servletContext.getContextPath() + "/GoToHomepage";
			System.out.println("FLOW: going to gotohomepageservlet");
		} else if (request.getParameter("login") != null || user == null) {

			System.out.println("FLOW: not logged. calling gotologin servlet");
			request.getSession().setAttribute("onlyLogin", true);
			path = servletContext.getContextPath() + "/GoToLogin";
		} else {

			System.out.println("FLOW: wants to confirm ordero");

			System.out.println("Number of orders: " + user.getOrders().size());
			System.out.println("Insolvent: " + user.isInsolventFlag());
			// maybe stateful?
			orderService.confirmOrder(order);

			if (request.getParameter("valid") != null) {

				System.out.println("FLOW: valid payment");
				// payment ok
				orderService.addOrder(order, user, true);
			} else if (request.getParameter("notValid") != null) {
				System.out.println("FLOW: notvalidpayment");
				// payment wrong
				orderService.addOrder(order, user, false);
			} else if (request.getParameter("random") != null) {
				// generate Random
			} else {
				// possible
				System.out.println("Entered in Third else in orderconfirmation.doPost(.)<");

			}

			System.out.println("FLOW: afterOrderUpdate");

			//here insolventFlag is updated, order list in user not updated
			System.out.println("Number of orders: " + user.getOrders().size());
			System.out.println("Insolvent: " + user.isInsolventFlag());

//			request.getSession().setAttribute("user", userService.);
			request.getSession().removeAttribute("order");
			System.out.println("FLOW: calling gotohomepage servlet");
			path = servletContext.getContextPath() + "/GoToHomepage";
		}
		response.sendRedirect(path);

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

}
