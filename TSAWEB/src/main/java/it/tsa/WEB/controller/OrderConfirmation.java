package it.tsa.WEB.controller;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
import it.tsa.EJB.entities.User;
import it.tsa.EJB.exceptions.CreationException;
import it.tsa.EJB.services.OrderService;

/**
 * Servlet implementation class GoToHomePage
 */
@WebServlet("/OrderConfirmation")
public class OrderConfirmation extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private TemplateEngine templateEngine;
	private ServletContext servletContext;
	private String path;
	private WebContext ctx;

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
		User loggedUser = (User) request.getSession().getAttribute("user");
		Order order = (Order) request.getSession().getAttribute("order");

		ctx = new WebContext(request, response, servletContext, request.getLocale());

		path = "/service/orderConfirmation.html";

		if (definedAndWantsToCreateOrder(order)) {

			// create Order instance
			try {
				int chosenSP = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("servicePackageId")));
				int chosenVP;
				String receivedVP = StringEscapeUtils.escapeJava(request.getParameter("validityPeriodId"));

				LocalDate startDate = LocalDate.parse(request.getParameter("startDate"));

				if (!startDate.isAfter(LocalDate.now()))
					throw new DateTimeParseException("ErrorCreatingStartDate", "startDate", 0);

				String[] names = request.getParameterValues("availableOptProdId");

				List<Integer> chosenOptProds = new ArrayList<Integer>();
				if (names != null) {
					List<String> receivedChoicesOfOP = Arrays.asList(names);
					chosenOptProds = getCorrectElements(receivedChoicesOfOP, chosenSP);
				}

				chosenVP = checkVPCorrectness(receivedVP, chosenSP);
				order = orderService.createOrder(loggedUser, chosenSP, chosenVP, chosenOptProds, startDate);
				request.getSession().setAttribute("order", order);
				ctx.setVariable("order", order);

			} catch (DateTimeParseException e) {
				errorOccurred("Error defining startDate", request);
			} catch (CreationException e) {
				errorOccurred("Error defining " + e.getMessage(), request);
			} catch (Exception e) {
				errorOccurred("Generic error", request);
			}
		} else {
			// get here after having logged in in order to confirm order

			order.setUser(loggedUser);
			ctx.setVariable("order", order);
		}
		ctx.setVariable("user", loggedUser);

		if (errorConfirmingOrder)
			response.sendRedirect(path);
		else
			templateEngine.process(path, ctx, response.getWriter());

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	private boolean definedAndWantsToCreateOrder(Order order) {
		return order == null;
	}

	// chosen opt prods are defined by two numbers: x,y
	// x: service package id
	// y: opt prod id
	private List<Integer> getCorrectElements(List<String> originalElements, int correctId) {
		List<Integer> chosenOptProds = originalElements.stream().filter(op -> op.startsWith(correctId + ",")) // looks
																												// for
																												// items
																												// starting
																												// with
																												// correct
																												// service
																												// package
				.map(op -> op.split(",")[1]) // removes the first part (x,) leaving only y
				.map(op -> Integer.parseInt(op)) // convert to int
				.toList();

		return chosenOptProds;
	}

	// check validityPeriod correctness
	private int checkVPCorrectness(String receivedVP, int chosenSP) throws CreationException {
		System.out.println("CheckVPCorrectness " + receivedVP);
		String[] params = receivedVP.split(",");
		if (Integer.parseInt(params[0]) == chosenSP)
			return Integer.parseInt(params[1]);
		else
			throw new CreationException("ValidityPeriod");
	}

	private void errorOccurred(String error, HttpServletRequest request) {
		errorConfirmingOrder = true;

		request.getSession().setAttribute("error", error);
		path = servletContext.getContextPath() + "/BuyService";
	}

}
