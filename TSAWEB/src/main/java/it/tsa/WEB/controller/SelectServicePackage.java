package it.tsa.WEB.controller;

import java.io.IOException;
import java.util.Arrays;
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

import it.tsa.EJB.entities.ServicePackage;
import it.tsa.EJB.entities.User;
import it.tsa.EJB.services.DbService;

/**
 * Servlet implementation class GoToHomePage
 */
@WebServlet("/SelectServicePackage")
public class SelectServicePackage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private TemplateEngine templateEngine;
	private ServletContext servletContext;
	private WebContext ctx;

	@EJB(name = "project.services/DbService")
	private DbService dbService;

	public SelectServicePackage() {
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
				
		int chosenSP = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("servicePackageId")));
		int chosenVP;
		String receivedVP = StringEscapeUtils.escapeJava(request.getParameter("validityPeriodId"));
		
		
		String[] names = request.getParameterValues("availableOptProdId");
		List<String> receivedChoicesOfOP = Arrays.asList(names);
		List<Integer> chosenOptProds = getCorrectElements(receivedChoicesOfOP, chosenSP);
		chosenVP = checkVPCorrectness(receivedVP, chosenSP);
		User loggedUser = (User) request.getSession().getAttribute("user");

		dbService.createOrder(loggedUser, chosenSP, chosenVP, chosenOptProds);
		
		
		/*
		 * String chosenServicePackage =
		 * StringEscapeUtils.escapeJava(request.getParameter("servicePackage"));
		 * ServicePackage result =
		 * dbService.retrieveServicePackage(Integer.parseInt(chosenServicePackage));
		 * String path = "/service/servicePackageDefinition.html";
		 * 
		 * User loggedUser = (User) request.getSession().getAttribute("user"); //TODO
		 * how to make it nicer ctx = new WebContext(request, response, servletContext,
		 * request.getLocale()); ctx.setVariable("userIsLogged", loggedUser != null);
		 * ctx.setVariable("servicePackage", result); templateEngine.process(path, ctx,
		 * response.getWriter());
		 */
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

