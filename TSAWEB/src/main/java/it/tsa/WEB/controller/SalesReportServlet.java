package it.tsa.WEB.controller;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class SalesReportServlet
 */
@WebServlet("/SalesReport")
public class SalesReportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
/*	@EJB(name="it.tsa.EJB.services/UserService")
	private UserService usrService;
	*/
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SalesReportServlet() {
        super();
        // TODO Auto-generated constructor stub
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
		
		
		
	}

}
