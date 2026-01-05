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

import com.train.beans.HistoryBean;
import com.train.beans.TrainException;
import com.train.constant.UserRole;
import com.train.service.BookingService;
import com.train.service.impl.BookingServiceImpl;
import com.train.utility.TrainUtil;

@SuppressWarnings("serial")
@WebServlet("/bookingdetails")
public class TicketBookingHistory extends HttpServlet {

	BookingService bookingService = new BookingServiceImpl();

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		res.setContentType("text/html");
		PrintWriter pw = res.getWriter();
		TrainUtil.validateUserAuthorization(req, UserRole.CUSTOMER);

		try {
			String customerId = TrainUtil.getCurrentUserEmail(req);
			List<HistoryBean> details = bookingService.getAllBookingsByCustomerId(customerId);

			if (details != null && !details.isEmpty()) {
				// Include header HTML
				RequestDispatcher rd = req.getRequestDispatcher("TicketBookingHistory.html");
				rd.include(req, res);

				// Page Title
				pw.println("<div class='main'><p class='menu'>Booked Ticket History</p></div>");

				// Horizontal Ticket Cards
				for (HistoryBean trans : details) {
					pw.println("<div class='ticket-card'>" + "<div class='ticket-row horizontal'>"
							+ "<div class='ticket-item'><span class='label'>Transaction ID:</span> <span class='value'>"
							+ trans.getTransId() + "</span></div>"
							+ "<div class='ticket-item'><span class='label'>Train No:</span> <span class='value'>"
							+ trans.getTr_no() + "</span></div>"
							+ "<div class='ticket-item'><span class='label'>From:</span> <span class='value'>"
							+ trans.getFrom_stn() + "</span></div>"
							+ "<div class='ticket-item'><span class='label'>To:</span> <span class='value'>"
							+ trans.getTo_stn() + "</span></div>"
							+ "<div class='ticket-item'><span class='label'>Journey Date:</span> <span class='value'>"
							+ trans.getDate() + "</span></div>"
							+ "<div class='ticket-item'><span class='label'>Seat:</span> <span class='value'>"
							+ trans.getSeats() + "</span></div>"
							+ "<div class='ticket-item'><span class='label'>Amount Paid:</span> <span class='value'>&#8377; "
							+ trans.getAmount() + "</span></div>" + "</div>" + "</div>");
				}

			} else {
				RequestDispatcher rd = req.getRequestDispatcher("UserViewTrains.html");
				rd.include(req, res);
				pw.println(
						"<div class='main'><p class='menu red'>No tickets booked yet. Book your first ticket now!</p></div>");
			}

		} catch (Exception e) {
			throw new TrainException(422, this.getClass().getName() + "_FAILED", e.getMessage());
		}
	}
}
