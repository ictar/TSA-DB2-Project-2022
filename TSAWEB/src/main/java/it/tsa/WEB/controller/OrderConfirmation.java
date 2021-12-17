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

/**
 * Servlet implementation class GoToHomePage
 */
@WebServlet("/OrderConfirmation")
public class OrderConfirmation extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private TemplateEngine templateEngine;
	private ServletContext servletContext;
	private WebContext ctx;

	@EJB(name = "project.services/DbService")
	private DbService dbService;

	@EJB(name = "project.services/OrderService")
	private OrderService orderService;
	
	@EJB(name = "project.services/UserService")
	private UserService userService;

	public OrderConfirmation() {
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Order order = (Order) request.getSession().getAttribute("order");
		User user = (User) request.getSession().getAttribute("user");
		
		orderService.confirmOrder(order);
		System.out.println("Orderconfirmed");
		
		if (request.getParameter("valid") != null) {
			//payment ok
			orderActivated(order);
		}
		else if (request.getParameter("notValid") != null) {
			//payment wrong
			orderRejected(order, user);
		} else if(request.getParameter("random") != null) {
			//generate Random
		} else {
			//default
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	
	
	private void orderActivated(Order order) {
		orderService.setOrderValidity(order, true);
		dbService.createActivationSchedule(order);		
	}
	
	private void orderRejected(Order order, User user) {
		orderService.setOrderValidity(order, false);
		userService.userInsolvent(user);
		//dbService.createAuditing(order, user);
	}
		
}

