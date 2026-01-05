package com.train.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

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
@WebServlet("/fareenq")
public class FareEnq extends HttpServlet {

	private final TrainService trainService = new TrainServiceImpl();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

		res.setContentType("text/html");
		PrintWriter pw = res.getWriter();

		/* ================= AUTHORIZATION ================= */
		// ✅ CORRECT USAGE (NO if, NO !)
		TrainUtil.validateUserAuthorization(req, UserRole.CUSTOMER);

		try {
			String fromStation = req.getParameter("fromstation");
			String toStation = req.getParameter("tostation");

			/* ================= INPUT VALIDATION ================= */
			if (fromStation == null || toStation == null || fromStation.trim().isEmpty()
					|| toStation.trim().isEmpty()) {

				RequestDispatcher rd = req.getRequestDispatcher("FareEnquiry.html");
				rd.include(req, res);
				pw.println("<div class='tab'><p1 class='menu'>Please select both stations</p1></div>");
				return;
			}

			List<TrainBean> trains = trainService.getTrainsBetweenStations(fromStation.trim(), toStation.trim());

			RequestDispatcher rd = req.getRequestDispatcher("FareEnquiry.html");
			rd.include(req, res);

			/* ================= DATA RENDERING ================= */
			if (trains != null && !trains.isEmpty()) {

				pw.println("<div class='main'>");
				pw.println("<p1 class='menu'>Fare for Trains Between " + fromStation + " and " + toStation + "</p1>");
				pw.println("</div>");

				pw.println("<div class='tab'>");
				pw.println("<table class='train-table'>");

				pw.println("<tr>" + "<th>Train Name</th>" + "<th>Train No</th>" + "<th>From</th>" + "<th>To</th>"
						+ "<th>Time</th>" + "<th>Seats</th>" + "<th>Fare (₹)</th>" + "<th>Action</th>" + "</tr>");

				for (TrainBean train : trains) {

					int hr = (int) (Math.random() * 24);
					int min = (int) (Math.random() * 60);
					String time = String.format("%02d:%02d", hr, min);

					pw.println("<tr>" + "<td>" + train.getTr_name() + "</td>" + "<td>" + train.getTr_no() + "</td>"
							+ "<td>" + train.getFrom_stn() + "</td>" + "<td>" + train.getTo_stn() + "</td>" + "<td>"
							+ time + "</td>" + "<td>" + train.getSeats() + "</td>" + "<td>" + train.getFare() + "</td>"
							+ "<td>" + "<a href='booktrainbyref?trainNo=" + train.getTr_no() + "&fromStn="
							+ train.getFrom_stn() + "&toStn=" + train.getTo_stn() + "'>"
							+ "<div class='red'>Book Now</div>" + "</a>" + "</td>" + "</tr>");
				}

				pw.println("</table>");
				pw.println("</div>");

			} else {
				pw.println("<div class='tab'>");
				pw.println(
						"<p1 class='menu'>No trains available between " + fromStation + " and " + toStation + "</p1>");
				pw.println("</div>");
			}

		} catch (TrainException te) {
			throw te;
		} catch (Exception e) {
			throw new TrainException(500, this.getClass().getName() + "_ERROR", "Unable to fetch fare details");
		}
	}
}
