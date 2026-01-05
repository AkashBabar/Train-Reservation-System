package com.train.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.train.beans.TrainException;
import com.train.beans.UserBean;
import com.train.constant.UserRole;
import com.train.service.UserService;
import com.train.service.impl.UserServiceImpl;

@SuppressWarnings("serial")
@WebServlet("/userreg")
public class UserRegServlet extends HttpServlet {

	private UserService userService = new UserServiceImpl(UserRole.CUSTOMER);

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		res.setContentType("text/html");
		PrintWriter pw = res.getWriter();

		try {
			// Get and trim parameters
			String mailid = req.getParameter("mailid") != null ? req.getParameter("mailid").trim() : "";
			String pword = req.getParameter("pword") != null ? req.getParameter("pword").trim() : "";
			String fname = req.getParameter("firstname") != null ? req.getParameter("firstname").trim() : "";
			String lname = req.getParameter("lastname") != null ? req.getParameter("lastname").trim() : "";
			String addr = req.getParameter("address") != null ? req.getParameter("address").trim() : "";
			String phonenoStr = req.getParameter("phoneno") != null ? req.getParameter("phoneno").trim() : "";

			// Validate required fields
			if (mailid.isEmpty() || pword.isEmpty() || fname.isEmpty() || phonenoStr.isEmpty()) {
				RequestDispatcher rd = req.getRequestDispatcher("UserRegister.html");
				rd.include(req, res);
				pw.println("<div class='tab error-msg'>Please fill all required fields!</div>");
				return;
			}

			// Parse phone number safely
			long phno = 0;
			try {
				phno = Long.parseLong(phonenoStr);
			} catch (NumberFormatException e) {
				RequestDispatcher rd = req.getRequestDispatcher("UserRegister.html");
				rd.include(req, res);
				pw.println("<div class='tab error-msg'>Invalid phone number!</div>");
				return;
			}

			// Create UserBean
			UserBean user = new UserBean();
			user.setMailId(mailid);
			user.setPWord(pword);
			user.setFName(fname);
			user.setLName(lname);
			user.setAddr(addr);
			user.setPhNo(phno);

			// Register user
			String message = userService.registerUser(user);

			if ("SUCCESS".equalsIgnoreCase(message)) {
				RequestDispatcher rd = req.getRequestDispatcher("UserLogin.html");
				rd.include(req, res);
				pw.println("<div class='tab success-msg'>User Registered Successfully!</div>");
			} else {
				RequestDispatcher rd = req.getRequestDispatcher("UserRegister.html");
				rd.include(req, res);
				pw.println("<div class='tab error-msg'>" + message + "</div>");
			}

		} catch (Exception e) {
			throw new TrainException(422, this.getClass().getName() + "_FAILED", e.getMessage());
		}
	}
}
