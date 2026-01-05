package com.train.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.train.constant.UserRole;
import com.train.utility.TrainUtil;

@SuppressWarnings("serial")
@WebServlet("/booktrainbyref")
public class BookTrainByRef extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

		res.setContentType("text/html");
		PrintWriter pw = res.getWriter();

		/* ================= Authorization (VOID method – NO !) ================= */
		TrainUtil.validateUserAuthorization(req, UserRole.CUSTOMER);

		try {
			String emailId = TrainUtil.getCurrentUserEmail(req);

			/* ================= Parameter Validation ================= */
			String trainNoParam = req.getParameter("trainNo");
			String fromStn = req.getParameter("fromStn");
			String toStn = req.getParameter("toStn");

			if (trainNoParam == null || fromStn == null || toStn == null) {
				res.sendRedirect("UserViewTrains.html");
				return;
			}

			long trainNo = Long.parseLong(trainNoParam);
			int seat = 1;

			/* ================= Include Header ================= */
			RequestDispatcher rd = req.getRequestDispatcher("UserViewTrains.html");
			rd.include(req, res);

			/* ================= Page Title ================= */
			pw.println("<div class='title'>");
			pw.println("<p1 class='menu'>Your Ticket Booking Information</p1>");
			pw.println("</div>");

			/* ================= Booking Form ================= */
			pw.println("<div class='tab'>");
			pw.println("<form action='payment' method='post'>");
			pw.println("<table>");

			pw.println("<tr>" + "<td>User ID:</td><td>" + emailId + "</td>" + "<td>Train No:</td><td>" + trainNo
					+ "</td>" + "</tr>");

			pw.println("<tr>" + "<td>From Station:</td><td>" + fromStn + "</td>" + "<td>To Station:</td><td>" + toStn
					+ "</td>" + "</tr>");

			pw.println("<tr>" + "<td>Journey Date:</td>" + "<td>" + "<input type='hidden' name='trainnumber' value='"
					+ trainNo + "'>" + "<input type='date' name='journeydate' min='" + LocalDate.now() + "' value='"
					+ LocalDate.now() + "'>" + "</td>" + "<td>No of Seats:</td>"
					+ "<td><input type='number' name='seats' min='1' value='" + seat + "'></td>" + "</tr>");

			pw.println("<tr>" + "<td>Select Class:</td>" + "<td>" + "<select name='class' required>"
					+ "<option value='Sleeper(SL)'>Sleeper (SL)</option>"
					+ "<option value='Second Sitting(2S)'>Second Sitting (2S)</option>"
					+ "<option value='AC First Class(1A)'>AC First Class (1A)</option>"
					+ "<option value='AC 2 Tier(2A)'>AC 2 Tier (2A)</option>" + "</select>" + "</td>"
					+ "<td>Berth Preference:</td>" + "<td>" + "<select name='berth'>"
					+ "<option value='NO'>No Preference</option>" + "<option value='LB'>Lower Berth (LB)</option>"
					+ "<option value='UB'>Upper Berth (UB)</option>" + "<option value='C'>Cabin</option>" + "</select>"
					+ "</td>" + "</tr>");

			pw.println("</table>");
			pw.println("</div>");

			/* ================= Submit Button ================= */
			pw.println("<div class='tab'>");
			pw.println("<p1 class='menu'>");
			pw.println("<input type='submit' value='Pay And Book'>");
			pw.println("</p1>");
			pw.println("</div>");

			pw.println("</form>");

		} catch (Exception e) {
			throw new ServletException("Unable to load booking page", e);
		}
	}
}
