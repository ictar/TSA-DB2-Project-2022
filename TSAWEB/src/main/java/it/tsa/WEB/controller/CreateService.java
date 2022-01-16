package it.tsa.WEB.controller;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import it.tsa.EJB.services.ServiceService;

/**
 * Servlet implementation class CreateServiceServlet
 */
@WebServlet("/CreateService")
public class CreateService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB(name="it.tsa.EJB.services/ServiceService")
	private ServiceService srvService;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateService() {
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
		
        
        String name, type;
        int includedMin, includedSMS, includedGB;
        float extraMinFee, extraSMSFee, extraGBFee;
        
        try {
        		name = request.getParameter("serviceName");
        		type = request.getParameter("serviceType");
        		// get parameters and check
        		// initial to -1 imply no need by default
        		includedMin = includedSMS = includedGB = -1;
        		extraMinFee = extraSMSFee = extraGBFee = -1;
        		switch (type) {
        		case "fixphone":
        			break;
        		case "mbphone":
        			includedMin = Integer.valueOf(request.getParameter("extraMinute"));
        			includedSMS = Integer.valueOf(request.getParameter("extraSMS"));
        			extraMinFee = Float.valueOf(request.getParameter("extraMinuteFee"));
        			extraSMSFee = Float.valueOf(request.getParameter("extraSMSFee"));
        			
        			if(includedMin < 0 || includedSMS < 0 || extraMinFee < 0 || extraSMSFee < 0) {
        				throw new Exception("Invalid field!");
        			}
        			break;
        		case "int":
        			includedGB = Integer.valueOf(request.getParameter("extraGB"));
        			extraGBFee = Float.valueOf(request.getParameter("extraGBFee"));
        			
        			if(includedGB < 0 || extraGBFee < 0) {
        				throw new Exception("Invalid field!");
        			}
        			break;
        		default:
        			throw new Exception("Unknown service type!");
        		}
        		
        		// create a new service
        		srvService.createAService(name, includedMin, includedSMS, includedGB, extraMinFee, extraSMSFee, extraGBFee);
        				request.getSession().setAttribute("csErrMsg", "Create service " + name + " successfully!");
        		
        } catch (Exception e) {
        	request.getSession().setAttribute("csErrMsg", e.getMessage());
		}
		
		response.sendRedirect(pathCtx + "/EmployeeHome");
	}

}
