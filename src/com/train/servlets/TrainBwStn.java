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
@WebServlet("/trainbwstn")
public class TrainBwStn extends HttpServlet {

	private final TrainService trainService = new TrainServiceImpl();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

		res.setContentType("text/html");
		PrintWriter pw = res.getWriter();

		/* ================= AUTHORIZATION ================= */
		TrainUtil.validateUserAuthorization(req, UserRole.CUSTOMER);

		try {
			String fromStation = req.getParameter("fromstation");
			String toStation = req.getParameter("tostation");

			/* ================= INPUT VALIDATION ================= */
			if (fromStation == null || toStation == null || fromStation.trim().isEmpty()
					|| toStation.trim().isEmpty()) {

				RequestDispatcher rd = req.getRequestDispatcher("TrainBwStn.html");
				rd.include(req, res);
				pw.println("<div class='tab'><p1 class='menu'>Please select both stations</p1></div>");
				return;
			}

			List<TrainBean> trains = trainService.getTrainsBetweenStations(fromStation.trim(), toStation.trim());

			RequestDispatcher rd = req.getRequestDispatcher("TrainBwStn.html");
			rd.include(req, res);

			/* ================= DATA DISPLAY ================= */
			if (trains != null && !trains.isEmpty()) {

				pw.println("<div class='main'>");
				pw.println("<p1 class='menu'>Trains Between Station " + fromStation + " and " + toStation + "</p1>");
				pw.println("</div>");

				pw.println("<div class='tab'>");
				pw.println("<table class='train-table'>");

				/* ===== TABLE HEADER ===== */
				pw.println("<tr>" + "<th>Train Name</th>" + "<th>Train No</th>" + "<th>From Station</th>"
						+ "<th>To Station</th>" + "<th>Time</th>" + "<th>Seats</th>" + "<th>Fare (INR)</th>"
						+ "<th>Action</th>" + "</tr>");

				/* ===== TABLE ROWS ===== */
				for (TrainBean train : trains) {

					int hr = (int) (Math.random() * 24);
					int min = (int) (Math.random() * 60);
					String time = String.format("%02d:%02d", hr, min);

					pw.println("<tr>" + "<td>" + train.getTr_name() + "</td>" + "<td>" + train.getTr_no() + "</td>"
							+ "<td>" + train.getFrom_stn() + "</td>" + "<td>" + train.getTo_stn() + "</td>" + "<td>"
							+ time + "</td>" + "<td>" + train.getSeats() + "</td>" + "<td>" + train.getFare()
							+ " RS</td>" + "<td>" + "<a href='booktrainbyref?trainNo=" + train.getTr_no() + "&fromStn="
							+ train.getFrom_stn() + "&toStn=" + train.getTo_stn() + "'>"
							+ "<div class='red'>Book Now</div>" + "</a>" + "</td>" + "</tr>");
				}

				pw.println("</table>");
				pw.println("</div>");

			} else {
				pw.println("<div class='tab'>");
				pw.println(
						"<p1 class='menu'>There are no trains Between " + fromStation + " and " + toStation + "</p1>");
				pw.println("</div>");
			}

		} catch (Exception e) {
			throw new TrainException(422, this.getClass().getName() + "_FAILED", e.getMessage());
		}
	}
}
