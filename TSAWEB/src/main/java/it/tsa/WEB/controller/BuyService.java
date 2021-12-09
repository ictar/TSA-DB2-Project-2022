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

	public BuyService() {
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

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		User loggedUser = (User) request.getSession().getAttribute("user");
		ctx = new WebContext(request, response, servletContext, request.getLocale());
		String path;
		if (request.getParameter("confirmButton") == null) {
			List<ServicePackage> servicePackages = null;
			servicePackages = dbService.findAllServicePackages();

			path = "/service/buyservice.html";

			ctx.setVariable("servicePackages", servicePackages);
		} else {

			int chosenSP = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("servicePackageId")));
			int chosenVP;
			String receivedVP = StringEscapeUtils.escapeJava(request.getParameter("validityPeriodId"));
			
			String dateValue[] = StringEscapeUtils.escapeJava(request.getParameter("startDate"))
					.split("-");
		
			//TODO maybe do some checking
			Date startDate = new Date(Integer.parseInt(dateValue[0]) - 1901, Integer.parseInt(dateValue[1]),Integer.parseInt(dateValue[2]));
			String[] names = request.getParameterValues("availableOptProdId");
			List<String> receivedChoicesOfOP = Arrays.asList(names);
			List<Integer> chosenOptProds = getCorrectElements(receivedChoicesOfOP, chosenSP);
			chosenVP = checkVPCorrectness(receivedVP, chosenSP);

			Order order = dbService.createOrder(loggedUser, chosenSP, chosenVP, chosenOptProds, startDate);
			request.getSession().setAttribute("order", order);
			path = "/service/orderConfirmation.html";
			ctx.setVariable("order", order);
		}
		ctx.setVariable("userIsLogged", loggedUser != null);
		templateEngine.process(path, ctx, response.getWriter());

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	//chosen opt prods are defined by two numbers: x,y
		//x: service package id
		//y: opt prod id
		private List<Integer> getCorrectElements(List<String> originalElements, int correctId) {
			List<Integer> chosenOptProds = originalElements.stream()
					.filter(op -> op.startsWith(correctId + ",")) //looks for items starting with correct service package
					.map(op -> op.split(",")[1]) //removes the first part (x,) leaving only y
					.map(op -> Integer.parseInt(op)) //convert to int
					.toList(); 
		
			return chosenOptProds;
		}
		
		private Integer checkVPCorrectness(String receivedVP, int chosenSP) {
			String[] params = receivedVP.split(",");
			if (Integer.parseInt(params[0])==chosenSP)
				return Integer.parseInt(params[1]);
			else
				return null;
		}
		
}
