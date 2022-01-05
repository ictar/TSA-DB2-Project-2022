package it.tsa.WEB.controller;

import java.io.IOException;
import java.util.ArrayList;
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

/**
 * Servlet implementation class GoToHomePage
 */

@WebServlet("/BuyService")
public class BuyService extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private TemplateEngine templateEngine;
	private ServletContext servletContext;
	private WebContext ctx;

	@EJB(name = "project.services/DbService")
	private DbService dbService;

	@EJB(name = "project.services/OrderService")
	private OrderService orderService;

	public void init() throws ServletException {
		System.out.println("Start buyservcie");
		servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String path;

		User loggedUser = (User) request.getSession().getAttribute("user");
		Order order = (Order) request.getSession().getAttribute("order");
		
		ctx = new WebContext(request, response, servletContext, request.getLocale());
		
		path = "/service/orderConfirmation.html";

		if (needsToCreateOrder(request, order)) {
			// if didnt press Confirm button then there is no order to confirm,
			// so show possible services to buy

			path = "/service/buyservice.html";

			// retrieves servicePackages to show
			List<ServicePackage> servicePackages = null;
			servicePackages = dbService.findAllServicePackages();
			ctx.setVariable("servicePackages", servicePackages);

		} else if (definedAndWantsToCreateOrder(order)) {
			// pressed confirm button then collect all info

			int chosenSP = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("servicePackageId")));
			int chosenVP;
			String receivedVP = StringEscapeUtils.escapeJava(request.getParameter("validityPeriodId"));

			String dateValue[] = StringEscapeUtils.escapeJava(request.getParameter("startDate")).split("-");

			// TODO maybe do some checking
			Date startDate = new Date(Integer.parseInt(dateValue[0]) - 1900, Integer.parseInt(dateValue[1]) - 1,
					Integer.parseInt(dateValue[2]));
			String[] names = request.getParameterValues("availableOptProdId");
			List<Integer> chosenOptProds = new ArrayList<Integer>();
			if (names != null) {
				List<String> receivedChoicesOfOP = Arrays.asList(names);
				chosenOptProds = getCorrectElements(receivedChoicesOfOP, chosenSP);
			}

			chosenVP = checkVPCorrectness(receivedVP, chosenSP);
			if (chosenVP != -1) {
				order = orderService.createOrder(loggedUser, chosenSP, chosenVP, chosenOptProds, startDate);
				request.getSession().setAttribute("order", order);
				ctx.setVariable("order", order);
			}
			else {
				System.out.println("ERRORE IN VP");
				path = servletContext.getContextPath() + "/GoToHomepage";
			}

		} else {
			// get here after being asked to login to confirm order

			order.setUser(loggedUser);
			ctx.setVariable("order", order);
		}
		ctx.setVariable("user", loggedUser);
		
		templateEngine.process(path, ctx, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
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
	private int checkVPCorrectness(String receivedVP, int chosenSP) {
		System.out.println("CheckVPCorrectness " + receivedVP);
		String[] params = receivedVP.split(",");
		if (Integer.parseInt(params[0]) == chosenSP)
			return Integer.parseInt(params[1]);
		else
			return -1;
	}

	private boolean needsToCreateOrder(HttpServletRequest request, Order order) {
		return request.getParameter("confirmButton") == null && order == null;
	}

	private boolean definedAndWantsToCreateOrder(Order order) {
		return order == null;
	}

}
