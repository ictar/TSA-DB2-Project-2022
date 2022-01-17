package it.tsa.WEB.controller;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;

import it.tsa.EJB.services.ValidityPeriodService;

/**
 * Servlet implementation class ValidityPeriodServlet
 */
@WebServlet("/CreateValidityPeriod")
public class CreateValidityPeriod extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB(name="it.tsa.EJB.services/ValidityPeriodService")
	private ValidityPeriodService vpService;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateValidityPeriod() {
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
		
        int duration;
        float price;
        
        try {
        		duration = Integer.valueOf(StringEscapeUtils.escapeJava(request.getParameter("duration")));
        		price = Float.valueOf(StringEscapeUtils.escapeJava(request.getParameter("price")));
        		 		
        		// create a new validity period
        		vpService.createAValidityPeriod(duration, price);
        		request.getSession().setAttribute("cvpErrMsg", "Create vadility period successfully!");
        		
        } catch (Exception e) {
        		request.getSession().setAttribute("cvpErrMsg", e.getMessage());
		}
		
		response.sendRedirect(pathCtx + "/EmployeeHome");
	}

}
