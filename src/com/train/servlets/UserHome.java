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

@WebServlet("/userhome")
public class UserHome extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		res.setContentType("text/html;charset=UTF-8");
		PrintWriter out = res.getWriter();

		HttpSession session = req.getSession(false);

		// If session doesn't exist or user not logged in, redirect to login
		if (session == null || session.getAttribute("userName") == null) {
			res.sendRedirect("UserLogin.html");
			return;
		}

		String userName = (String) session.getAttribute("userName");

		// Include UserHome HTML
		RequestDispatcher rd = req.getRequestDispatcher("UserHome.html");
		rd.include(req, res);

		out.println("<div class='user-home-container'>");
		out.println("<h2 class='welcome-title'>Welcome, " + userName + "!</h2>");
		out.println("<p class='welcome-text'>We are happy to have you on the NITRTC Railway Reservation System. "
				+ "This platform is designed to provide you with accurate and up-to-date "
				+ "railway information in a simple and user-friendly manner.</p>");

		out.println("<div class='info-box'>");
		out.println("<h3>What you can do here</h3>");
		out.println("<p>As a registered user, you can explore complete train information, "
				+ "check schedules, view fare details, and access various railway-related "
				+ "services efficiently using the navigation menu.</p>");
		out.println("</div>");

		out.println("<div class='info-box'>");
		out.println("<h3>Why choose NITRTC</h3>");
		out.println("<p>NITRTC focuses on providing reliable data, fast responses, and a smooth "
				+ "experience. Our system ensures that users get the information they need "
				+ "without complexity or confusion.</p>");
		out.println("</div>");

		out.println("<p class='footer-note'>Thank you for choosing NITRTC. We wish you a pleasant experience.</p>");
		out.println("</div>");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		doGet(req, res);
	}
}
