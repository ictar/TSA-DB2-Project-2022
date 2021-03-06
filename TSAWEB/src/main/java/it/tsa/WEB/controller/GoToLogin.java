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
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.tsa.EJB.services.MiscService;

/**
 * Servlet implementation class GoToHomePage
 */
@WebServlet("/GoToLogin")
public class GoToLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private TemplateEngine templateEngine;
	private ServletContext servletContext;
	private WebContext ctx;

	@EJB(name = "project.services/MiscService")
	private MiscService miscService;

	public void init() throws ServletException {
		System.out.println("Start gotologin");

		servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = "/index.html";
		ctx = new WebContext(request, response, servletContext, request.getLocale());

		boolean onlyLogin = (boolean) request.getSession().getAttribute("onlyLogin");
		if (request.getAttribute("registrationResult") != null) {
			boolean registrationResult = (boolean) request.getAttribute("registrationResult");
			ctx.setVariable("registrationResult", registrationResult);
		}
		ctx.setVariable("onlyLogin", onlyLogin);
		templateEngine.process(path, ctx, response.getWriter());

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
