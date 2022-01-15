package it.tsa.WEB.controller;

import java.io.IOException;
import java.util.Arrays;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import it.tsa.EJB.services.ServicePkgService;

/**
 * Servlet implementation class CreateServicePkgServlet
 */
@WebServlet("/CreateServicePkg")
public class CreateServicePkgServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB(name="it.tsa.EJB.services/ServicePkgService")
	private ServicePkgService srvpkgService;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateServicePkgServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// if the employee is not logged in, redirect to the login
		String pathCtx = getServletContext().getContextPath();
		HttpSession session = request.getSession();
		if(session.isNew() || session.getAttribute("employee") == null) {
			response.sendRedirect(pathCtx + "/employee/login.html");
			return;
		}
   
		String name;
		String[] tvps, tservices, tprods;
		int[] vps, services, prods;
		try {
			name = request.getParameter("servicePkgName");
			
			tvps = request.getParameterValues("validperiods");
			if(tvps == null || tvps.length == 0) {
				throw new Exception("At least one validity period is required!");
			}
			vps = Arrays.stream(tvps).mapToInt(Integer::parseInt).toArray();
			
			tservices = request.getParameterValues("services");
			if(tservices == null || tservices.length == 0) {
				throw new Exception("At least one service is required!");
			}
			services = Arrays.stream(tservices).mapToInt(Integer::parseInt).toArray();
			
			tprods = request.getParameterValues("products");
			if(tprods != null) {
				prods = Arrays.stream(tprods).mapToInt(Integer::parseInt).toArray();
			} else {
				prods = null;
			}
			
			srvpkgService.createAServicePkg(name, vps, services, prods);
			
			request.getSession().setAttribute("cspErrMsg", "Create product " + name + " successfully!");
		} catch (Exception e) {	
			request.getSession().setAttribute("cspErrMsg", e.getMessage());
		}
		

		response.sendRedirect(pathCtx + "/EmployeeHome");
	}
}
