package com.train.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.train.beans.TrainBean;
import com.train.beans.TrainException;
import com.train.constant.UserRole;
import com.train.service.TrainService;
import com.train.service.impl.TrainServiceImpl;
import com.train.utility.TrainUtil;

@SuppressWarnings("serial")
@WebServlet("/viewadmin")
public class AdminViewLinkFwd extends HttpServlet {

	private TrainService trainService = new TrainServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

		res.setContentType("text/html");
		PrintWriter pw = res.getWriter();

		// Authorization check
		TrainUtil.validateUserAuthorization(req, UserRole.ADMIN);

		try {
			String trainNo = req.getParameter("trainNo");
			TrainBean train = trainService.getTrainById(trainNo);

			if (train != null) {

				// Include Admin Navbar/Home
				RequestDispatcher rd = req.getRequestDispatcher("AdminHome.html");
				rd.include(req, res);

				// ================= Page Title =================
				pw.println("<div class='page-title'>");
				pw.println("<h2>Selected Train Detail</h2>");
				pw.println("<p>Complete information of the selected train</p>");
				pw.println("</div>");

				// ================= Card Container =================
				pw.println("<section class='content-wrapper'>");
				pw.println("<table>");

				pw.println("<tr><th>Field</th><th>Details</th></tr>");

				pw.println("<tr><td>Train Name</td><td>" + train.getTr_name() + "</td></tr>");
				pw.println("<tr><td>Train Number</td><td>" + train.getTr_no() + "</td></tr>");
				pw.println("<tr><td>From Station</td><td>" + train.getFrom_stn() + "</td></tr>");
				pw.println("<tr><td>To Station</td><td>" + train.getTo_stn() + "</td></tr>");
				pw.println("<tr><td>Available Seats</td><td>" + train.getSeats() + "</td></tr>");
				pw.println("<tr><td>Fare (INR)</td><td>" + train.getFare() + " Rs</td></tr>");

				pw.println("</table>");
				pw.println("</section>");

			} else {

				// Train not found
				RequestDispatcher rd = req.getRequestDispatcher("AdminSearchTrains.html");
				rd.include(req, res);

				pw.println("<div class='page-title'>");
				pw.println("<h2>Train Not Found</h2>");
				pw.println("<p>Train Number " + trainNo + " is not available</p>");
				pw.println("</div>");
			}

		} catch (Exception e) {
			throw new TrainException(422, this.getClass().getName() + "_FAILED", e.getMessage());
		}
	}
}
