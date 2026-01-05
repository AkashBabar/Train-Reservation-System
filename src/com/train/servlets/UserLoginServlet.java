package com.train.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.train.constant.ResponseCode;
import com.train.constant.UserRole;
import com.train.utility.TrainUtil;

@WebServlet("/userlogin")
public class UserLoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		res.setContentType("text/html;charset=UTF-8");
		PrintWriter pw = res.getWriter();

		String uName = req.getParameter("uname");
		String pWord = req.getParameter("pword");

		// Call TrainUtil login
		String responseMsg = TrainUtil.login(req, res, UserRole.CUSTOMER, uName, pWord);

		if (ResponseCode.SUCCESS.toString().equalsIgnoreCase(responseMsg)) {
			// Create session and store user info
			HttpSession session = req.getSession(true);
			session.setAttribute("userName", uName);
			session.setAttribute("role", UserRole.CUSTOMER.toString());

			// Redirect to UserHome servlet
			res.sendRedirect("userhome");
		} else {
			// Login failed, show login page with error
			RequestDispatcher rd = req.getRequestDispatcher("UserLogin.html");
			rd.include(req, res);

			pw.println("<div class='tab'><p class='menu'>" + responseMsg + "</p></div>");
		}
	}
}
