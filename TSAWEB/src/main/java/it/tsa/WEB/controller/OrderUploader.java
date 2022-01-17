package it.tsa.WEB.controller;

import java.io.IOException;
import java.util.Random;

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
import it.tsa.EJB.services.OrderService;

/**
 * Servlet implementation class GoToHomePage
 */
@WebServlet("/OrderUploader")
public class OrderUploader extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private TemplateEngine templateEngine;
	private ServletContext servletContext;

	private boolean errorConfirmingOrder = false;

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

		errorConfirmingOrder = false;
		Order order = (Order) request.getSession().getAttribute("order");
		User user = (User) request.getSession().getAttribute("user");
		
		String path = servletContext.getContextPath() + "/GoToHomepage";

		if (request.getParameter("deleteOrder") != null) {

			request.getSession().removeAttribute("order");

		} else if (request.getParameter("login") != null || user == null) {

			request.getSession().setAttribute("onlyLogin", true);
			path = servletContext.getContextPath() + "/GoToLogin";
		} else {

			//try uploading order
			try {
				/*
				 * did not place "toFixOrder" in servletContext because it (servletContext) is
				 * shared with all sessions, so I use session
				 */
				boolean isNewOrder = request.getSession().getAttribute("toFixOrder") == null;
				int orderId;
				
				if (isNewOrder)
					orderId = orderService.confirmOrder(order);
				else
					orderId = order.getId();

				if (request.getParameter("valid") != null) {
					user = orderService.attemptPayment(orderId, true);
			
				} else if (request.getParameter("notValid") != null) {
					user = orderService.attemptPayment(orderId, false);
				
				} else if (request.getParameter("random") != null) {
				
					Random booleanRandom = new Random();
					user = orderService.attemptPayment(orderId, booleanRandom.nextBoolean());

				} else {
					throw new Exception("Flow error");
				}

				request.getSession().setAttribute("user", user); // called to update user with new order
				request.getSession().removeAttribute("order");
				request.getSession().removeAttribute("toFixOrder");
				
			} catch (Exception e) {
				errorConfirmingOrder = true;
			}
		}
		if (!errorConfirmingOrder)
			response.sendRedirect(path);
		else {

			WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("error", "Error confirming order");
			ctx.setVariable("user", user);
			ctx.setVariable("order", order);

			templateEngine.process("/service/orderConfirmation.html", ctx, response.getWriter());
		}

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

}
