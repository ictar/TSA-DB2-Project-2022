package it.tsa.WEB.controller;

import java.io.IOException;
import java.util.Set;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.tsa.EJB.entities.OptProduct;
import it.tsa.EJB.entities.Service;
import it.tsa.EJB.entities.ValidityPeriod;
import it.tsa.EJB.services.EmployeeService;
import it.tsa.EJB.services.ProductService;
import it.tsa.EJB.services.ServicePkgService;
import it.tsa.EJB.services.ServiceService;
import it.tsa.EJB.services.ValidityPeriodService;

/**
 * Servlet implementation class EmployeeHomeServlet
 */
@WebServlet("/EmployeeHome")
public class EmployeeHomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private TemplateEngine tmplEngine;
	
	@EJB(name="it.tsa.EJB.services/ProductService")
	private ProductService prodService;
	
	@EJB(name="it.tsa.EJB.services/ServiceService")
	private ServiceService srvService;
	
	@EJB(name="it.tsa.EJB.services/ValidityPeriodService")
	private ValidityPeriodService vpService;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeeHomeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.tmplEngine = new TemplateEngine();
        this.tmplEngine.setTemplateResolver(templateResolver);
        templateResolver.setSuffix(".html");
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// if the employee is not logged in, redirect to the login
		String pathCtx = getServletContext().getContextPath();
		HttpSession session = request.getSession();
		if(session.isNew() || session.getAttribute("employee") == null) {
			response.sendRedirect(pathCtx+"/employee/login.html");
			return;
		}
		
		String path = "/employee/home.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext);
		
		/* if there are any error messages */
		if(session.getAttribute("cpErrMsg") != null) {
			ctx.setVariable("cpErrMsg", session.getAttribute("cpErrMsg"));
			request.getSession().removeAttribute("cpErrMsg");
		}
		
		if(session.getAttribute("csErrMsg") != null) {
			ctx.setVariable("csErrMsg", session.getAttribute("csErrMsg"));
			request.getSession().removeAttribute("csErrMsg");
		}
		
		if(session.getAttribute("cvpErrMsg") != null) {
			ctx.setVariable("cvpErrMsg", session.getAttribute("cvpErrMsg"));
			request.getSession().removeAttribute("cvpErrMsg");
		}
		
		if(session.getAttribute("cspErrMsg") != null) {
			ctx.setVariable("cspErrMsg", session.getAttribute("cspErrMsg"));
			request.getSession().removeAttribute("cspErrMsg");
		}
		
		/* retreive current services, products and validiyperiod to populate the service package creation form.
		 */
		Set<Service> services;
		Set<OptProduct> prods;
		Set<ValidityPeriod> vps;
		
		try {
			services = srvService.findAllService();
			prods = prodService.findAllProducts();
			vps = vpService.findAllValidityPeriods();
		} catch (Exception e){
			ctx.setVariable("cspErrMsg", e.getMessage());
			tmplEngine.process(path, ctx, response.getWriter());
			return;
		}
		
		ctx.setVariable("serviceSet", services);
		ctx.setVariable("productSet", prods);
		ctx.setVariable("validperiodSet", vps);
		
		tmplEngine.process(path, ctx, response.getWriter());
	}


}
