package com.train.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.train.beans.HistoryBean;
import com.train.beans.TrainBean;
import com.train.beans.TrainException;
import com.train.constant.ResponseCode;
import com.train.constant.UserRole;
import com.train.service.BookingService;
import com.train.service.TrainService;
import com.train.service.impl.BookingServiceImpl;
import com.train.service.impl.TrainServiceImpl;
import com.train.utility.TrainUtil;

@SuppressWarnings("serial")
@WebServlet("/booktrains")
public class BookTrains extends HttpServlet {

	private TrainService trainService = new TrainServiceImpl();
	private BookingService bookingService = new BookingServiceImpl();

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		PrintWriter pw = res.getWriter();
		res.setContentType("text/html");
		TrainUtil.validateUserAuthorization(req, UserRole.CUSTOMER);

		RequestDispatcher rd = req.getRequestDispatcher("PaymentSuccess.html");
		rd.include(req, res);

		ServletContext sct = req.getServletContext();

		try {
			int seat = (int) sct.getAttribute("seats");
			String trainNo = (String) sct.getAttribute("trainnumber");
			Date journeyDate = (Date) sct.getAttribute("journeydate"); // java.sql.Date
			String seatClass = (String) sct.getAttribute("class");

			String userMailId = TrainUtil.getCurrentUserEmail(req);
			String dateStr = journeyDate.toString();

			TrainBean train = trainService.getTrainById(trainNo);

			if (train != null) {
				int avail = train.getSeats();
				if (seat > avail) {
					pw.println("<div class='tab'><p class='menu red'>Only " + avail
							+ " Seats are Available in this Train!</p></div>");
				} else {
					avail -= seat;
					train.setSeats(avail);
					String responseCode = trainService.updateTrain(train);
					if (ResponseCode.SUCCESS.toString().equalsIgnoreCase(responseCode)) {

						HistoryBean bookingDetails = new HistoryBean();
						bookingDetails.setAmount(train.getFare() * seat);
						bookingDetails.setFrom_stn(train.getFrom_stn());
						bookingDetails.setTo_stn(train.getTo_stn());
						bookingDetails.setTr_no(trainNo);
						bookingDetails.setSeats(seat);
						bookingDetails.setMailId(userMailId);
						bookingDetails.setDate(dateStr);

						HistoryBean transaction = bookingService.createHistory(bookingDetails);

						// ===== Success Message =====
						pw.println("<div class='tab'>" + "<p class='menu green'>" + seat
								+ " Seats Booked Successfully!<br><br>"
								+ "Your Transaction Id is: <span class='trans-id'>" + transaction.getTransId()
								+ "</span></p>" + "</div>");

						// ===== Booking Details Table =====
						pw.println("<div class='tab'>" + "<p class='menu'>" + "<table>"
								+ "<tr><td>PNR No:</td><td colspan='3' class='highlight'>" + transaction.getTransId()
								+ "</td></tr>" + "<tr><td>Train Name:</td><td>" + train.getTr_name() + "</td>"
								+ "<td>Train No:</td><td>" + transaction.getTr_no() + "</td></tr>"
								+ "<tr><td>Booked From:</td><td>" + transaction.getFrom_stn() + "</td>"
								+ "<td>To Station:</td><td>" + transaction.getTo_stn() + "</td></tr>"
								+ "<tr><td>Date Of Journey:</td><td>" + transaction.getDate() + "</td>"
								+ "<td>Time(HH:MM):</td><td>11:23</td></tr>" + "<tr><td>Passengers:</td><td>"
								+ transaction.getSeats() + "</td>" + "<td>Class:</td><td>" + seatClass + "</td></tr>"
								+ "<tr><td>Booking Status:</td><td class='status green'>CNF/S10/35</td>"
								+ "<td>Amount Paid:</td><td class='highlight'>&#8377; " + transaction.getAmount()
								+ "</td></tr>" + "</table>" + "</p></div>");

					} else {
						pw.println("<div class='tab'><p class='menu red'>Transaction Declined. Try Again!</p></div>");
					}
				}
			} else {
				pw.println("<div class='tab'><p class='menu red'>Invalid Train Number!</p></div>");
			}

		} catch (Exception e) {
			throw new TrainException(422, this.getClass().getName() + "_FAILED", e.getMessage());
		}

		// Remove attributes from ServletContext
		sct.removeAttribute("seats");
		sct.removeAttribute("trainnumber");
		sct.removeAttribute("journeydate");
		sct.removeAttribute("class");
	}
}
