package it.tsa.WEB.controller;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.tsa.EJB.entities.Order;
import it.tsa.EJB.entities.User;
import it.tsa.EJB.services.OrderService;

/**
 * Servlet implementation class GoToHomePage
 */
@WebServlet("/OrderConfirmation")
public class OrderConfirmation extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private TemplateEngine templateEngine;
	private ServletContext servletContext;

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
		String path = servletContext.getContextPath() + "/GoToHomepage";

		if (request.getParameter("deleteOrder") != null) {

			request.getSession().removeAttribute("order");

		} else if (request.getParameter("login") != null || user == null) {

			request.getSession().setAttribute("onlyLogin", true);
			path = servletContext.getContextPath() + "/GoToLogin";
		} else {

			/* 
			 * did not place "toFixOrder" in servletContext because it is shared with
			 * all sessions, so I use session
			 */		
			if (request.getSession().getAttribute("toFixOrder") == null) {
				orderService.confirmOrder(order); // maybe stateful?

				if (request.getParameter("valid") != null) {

					// payment ok
					orderService.addOrder(order, user, true);
				} else if (request.getParameter("notValid") != null) {
					// payment wrong
					orderService.addOrder(order, user, false);
				} else if (request.getParameter("random") != null) {


					// generate Random
				} else {
					// possible
					System.out.println("Entered in Third else in orderconfirmation.doPost(.)<");

				}
			} else {

				if (request.getParameter("valid") != null) {

					// payment ok
					orderService.fixOrder(order, user, true);
				} else if (request.getParameter("notValid") != null) {
					// payment wrong
					orderService.fixOrder(order, user, false);
				} else if (request.getParameter("random") != null) {
					// generate Random
				} else {
					// possible
					System.out.println("Entered in Third else in orderconfirmation.doPost(.)<");

				}
				request.getSession().removeAttribute("toFixOrder");
			}

			request.getSession().setAttribute("user", user); // called to update user with new order
			request.getSession().removeAttribute("order");
		}
		response.sendRedirect(path);

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

}
