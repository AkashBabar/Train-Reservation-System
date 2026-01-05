package com.train.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.train.constant.UserRole;
import com.train.utility.TrainUtil;

@SuppressWarnings("serial")
@WebServlet("/userprofile")
public class UserProfile extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

		res.setContentType("text/html;charset=UTF-8");
		PrintWriter pw = res.getWriter();

		/* Authorization check */
		TrainUtil.validateUserAuthorization(req, UserRole.CUSTOMER);

		/* Include static layout (Navbar + Page Title) */
		RequestDispatcher rd = req.getRequestDispatcher("UserProfile.html");
		rd.include(req, res);

		String userName = TrainUtil.getCurrentUserName(req);

		/* ================= Welcome Tab ================= */
		pw.println("<div class='tab'>");
		pw.println("<p class='menu'>Hello " + userName + " ! Welcome to our new NITRTC Website</p>");
		pw.println("</div>");

		/* ================= Action Menu ================= */
		pw.println("<div class='main'>");

		pw.println("<p class='menu'><a href='viewuserprofile'>View Profile</a></p>");
		pw.println("<p class='menu'><a href='edituserprofile'>Edit Profile</a></p>");
		pw.println("<p class='menu'><a href='changeuserpassword'>Change Password</a></p>");

		pw.println("</div>");

		/* ================= Info Message ================= */
		pw.println("<div class='tab yellow'>");
		pw.println("Hey <b>" + userName + "</b>,");
		pw.println("Here you can <b>View</b>, <b>Edit</b> your profile and ");
		pw.println("<b>Change your password</b> securely.<br/>");
		pw.println("Thanks for being connected with us!");
		pw.println("</div>");
	}
}
