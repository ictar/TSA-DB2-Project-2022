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
@WebServlet("/RejectedOrder")
public class RejectedOrder extends HttpServlet {
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
		String path = "/service/orderConfirmation.html";
		int orderId = Integer.parseInt(request.getParameter("toFixOrderId"));
		request.getSession().setAttribute("toFixOrder", orderId);
		User currentUser = (User) request.getSession().getAttribute("user");
		List<Order> orders = currentUser.getOrders().stream().filter(order -> order.getId() == orderId).toList();
		
		ctx = new WebContext(request, response, servletContext, request.getLocale());
		if (orders.size() == 1) {
			request.getSession().setAttribute("order", orders.get(0));
			
			ctx.setVariable("order", orders.get(0));
			ctx.setVariable("user", currentUser);
		}
		
		templateEngine.process(path, ctx, response.getWriter());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

}