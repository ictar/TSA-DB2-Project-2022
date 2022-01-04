package it.tsa.WEB.controller;

import java.io.IOException;
import java.util.List;

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

import it.tsa.EJB.services.AuditingService;
import it.tsa.EJB.services.OrderService;
import it.tsa.EJB.services.SalesReportService;
import it.tsa.EJB.services.UserService;
import it.tsa.EJB.entities.Auditing;
import it.tsa.EJB.entities.OptProduct;
import it.tsa.EJB.entities.Order;
import it.tsa.EJB.entities.User;

/**
 * Servlet implementation class SalesReportServlet
 */
@WebServlet("/SalesReport")
public class SalesReportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private TemplateEngine tmplEngine;
       
	@EJB(name="it.tsa.EJB.services/UserService")
	private UserService usrService;
	@EJB(name="it.tsa.EJB.services/OrderService")
	private OrderService orderService;
	@EJB(name="it.tsa.EJB.services/AuditingService")
	private AuditingService audService;
	@EJB(name="it.tsa.EJB.services/SalesReportService")
	private SalesReportService srService;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SalesReportServlet() {
        super();
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
			response.sendRedirect(pathCtx + "/employee/login.html");
			return;
		}
		
		String path = "/employee/salesreport.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext);
		
		// Number of total purchases per package.
		List spps;
		try {
			spps = srService.getTotalPurchasePerSP();
		} catch (Exception e) {
			ctx.setVariable("ppErrMsg", e.getMessage());
			tmplEngine.process(path, ctx, response.getWriter());
			return;
		}
		
		ctx.setVariable("packagePurchaseList", spps);
		// Number of total purchases per package and validity period.
		List spvpps;
		try {
			spvpps = srService.getTotalPurchasePerSPandVP();
		} catch (Exception e) {
			ctx.setVariable("pvppErrMsg", e.getMessage());
			tmplEngine.process(path, ctx, response.getWriter());
			return;
		}
		
		ctx.setVariable("packageVPPurchaseList", spvpps);
		
		// Total value of sales per package with and without the optional products.
		List pswp;
		try {
			pswp = srService.getTotalSalesPerSPWithProd();
		} catch (Exception e) {
			ctx.setVariable("pswpErrMsg", e.getMessage());
			tmplEngine.process(path, ctx, response.getWriter());
			return;
		}
		
		ctx.setVariable("packageSaleWProdList", spps);
		
		List psnp;
		try {
			psnp = srService.getTotalSalesPerSPWithoutProd();
		} catch (Exception e) {
			ctx.setVariable("psnpErrMsg", e.getMessage());
			tmplEngine.process(path, ctx, response.getWriter());
			return;
		}
		
		ctx.setVariable("packageSaleNProdList", spps);
		
		// Average number of optional products sold together with each service package.
		List asppsp;
		try {
			asppsp = srService.getAvgProdPerSP();
		} catch (Exception e) {
			ctx.setVariable("asppspErrMsg", e.getMessage());
			tmplEngine.process(path, ctx, response.getWriter());
			return;
		}
		
		ctx.setVariable("avgSaleProdPerSPList", spps);
		
		// List of insolvent users, suspended orders and alerts.
		List<User> isusers;
		try {
			isusers = usrService.getInsolventUsers();
		} catch (Exception e) {
			ctx.setVariable("iuErrMsg", e.getMessage());
			tmplEngine.process(path, ctx, response.getWriter());
			return;
		}
		
		ctx.setVariable("insolventUserList", isusers);
		
		List<Order> susporders;
		try {
			susporders = orderService.getAllSuspendedOrders();
		} catch (Exception e) {
			ctx.setVariable("soErrMsg", e.getMessage());
			tmplEngine.process(path, ctx, response.getWriter());
			return;
		}
		
		ctx.setVariable("suspOrderList", susporders);
		
		List<Auditing> audits;
		try {
			audits = audService.getAllAuditings();
		} catch (Exception e) {
			ctx.setVariable("audErrMsg", e.getMessage());
			tmplEngine.process(path, ctx, response.getWriter());
			return;
		}
		
		ctx.setVariable("auditList", audits);
		
		// Best seller optional product, 
		// i.e. the optional product with the greatest value of sales across all the sold service packages.
		Object bsprod;
		try {
			bsprod = srService.getBestSellerProduct();	
		}  catch (Exception e) {
			ctx.setVariable("bspErrMsg", e.getMessage());
			tmplEngine.process(path, ctx, response.getWriter());
			return;
		}
		
		ctx.setVariable("bestOptProd", bsprod);
		
		tmplEngine.process(path, ctx, response.getWriter());
		
	}

}
