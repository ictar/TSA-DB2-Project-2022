package it.tsa.WEB.controller;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.tsa.EJB.services.ProductService;

/**
 * Servlet implementation class CreateProductServlet
 */
@WebServlet("/CreateProduct")
public class CreateProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@EJB(name = "it.tsa.EJB.services/ProductService")
	private ProductService prodService;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CreateProductServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// if the employee is not logged in, redirect to the login
		String pathCtx = getServletContext().getContextPath();
		HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute("employee") == null) {
			response.sendRedirect(pathCtx + "/employee/login.html");
			return;
		}

		String name;
		float fee;

		try {
			name = request.getParameter("productName");
			fee = Float.valueOf(request.getParameter("monthlyFee"));

			if (name == null || name.isEmpty() || fee <= 0) {
				throw new Exception("No empty field!");
			}

			prodService.createAProduct(name, fee);

			request.getSession().setAttribute("cpErrMsg", "Create product " + name + " successfully!");
		} catch (Exception e) {
			request.getSession().setAttribute("cpErrMsg", e.getMessage());
		}

		response.sendRedirect(pathCtx + "/EmployeeHome");
	}

}
