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
@WebServlet("/adminsearchtrain")
public class AdminSearchTrain extends HttpServlet {

	private TrainService trainService = new TrainServiceImpl();

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

		res.setContentType("text/html");
		PrintWriter pw = res.getWriter();
		TrainUtil.validateUserAuthorization(req, UserRole.ADMIN);

		try {
			String trainNo = req.getParameter("trainnumber");
			TrainBean train = trainService.getTrainById(trainNo);

			// Include Header + Navbar
			RequestDispatcher rd = req.getRequestDispatcher("AdminSearchTrain.html");
			rd.include(req, res);

			// Page title
			pw.println("<section class='page-title'>");
			pw.println("<p>Train information based on your search</p>");
			pw.println("</section>");

			if (train != null) {

				pw.println("<section class='card table-card'>");
				pw.println("<div class='table-wrapper'>");
				pw.println("<table class='train-table'>");

				// Table header
				pw.println("<tr>");
				pw.println("<th>Train Name</th>");
				pw.println("<th>Train Number</th>");
				pw.println("<th>From Station</th>");
				pw.println("<th>To Station</th>");
				pw.println("<th>Available Seats</th>");
				pw.println("<th>Fare (₹)</th>");
				pw.println("</tr>");

				// Table data
				pw.println("<tr>");
				pw.println("<td>" + train.getTr_name() + "</td>");
				pw.println("<td>" + train.getTr_no() + "</td>");
				pw.println("<td>" + train.getFrom_stn() + "</td>");
				pw.println("<td>" + train.getTo_stn() + "</td>");
				pw.println("<td>" + train.getSeats() + "</td>");
				pw.println("<td>" + train.getFare() + "</td>");
				pw.println("</tr>");

				pw.println("</table>");
				pw.println("</div>");
				pw.println("</section>");

			} else {

				pw.println("<section class='card'>");
				pw.println("<p style='color:#d32f2f; font-weight:600; text-align:center;'>");
				pw.println("No train found with Train Number <strong>" + trainNo + "</strong>.");
				pw.println("</p>");
				pw.println("</section>");
			}

		} catch (Exception e) {
			throw new TrainException(422, this.getClass().getName() + "_FAILED", e.getMessage());
		}
	}
}
