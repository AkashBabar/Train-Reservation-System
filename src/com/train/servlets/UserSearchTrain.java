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
@WebServlet("/searchtrainservlet")
public class UserSearchTrain extends HttpServlet {

	private TrainService trainService = new TrainServiceImpl();

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

		res.setContentType("text/html");
		PrintWriter pw = res.getWriter();

		TrainUtil.validateUserAuthorization(req, UserRole.CUSTOMER);

		try {
			String trainNo = req.getParameter("trainnumber");
			TrainBean train = trainService.getTrainById(trainNo);

			RequestDispatcher rd = req.getRequestDispatcher("SearchTrains.html");
			rd.include(req, res);

			if (train != null) {

				pw.println("<div class='tab'>" + "<table class='train-table'>"

						+ "<tr>" + "<th>Train Name</th>" + "<th>Train Number</th>" + "<th>From Station</th>"
						+ "<th>To Station</th>" + "<th>Available Seats</th>" + "<th>Fare (INR)</th>" + "</tr>"

						+ "<tr>" + "<td>" + train.getTr_name() + "</td>" + "<td>" + train.getTr_no() + "</td>" + "<td>"
						+ train.getFrom_stn() + "</td>" + "<td>" + train.getTo_stn() + "</td>" + "<td>"
						+ train.getSeats() + "</td>" + "<td>" + train.getFare() + " RS</td>" + "</tr>"

						+ "</table>" + "</div>");

			} else {
				pw.println("<div class='tab'>" + "<p1 class='menu'>Train No. " + trainNo + " is Not Available !</p1>"
						+ "</div>");
			}

		} catch (Exception e) {
			throw new TrainException(422, this.getClass().getName() + "_FAILED", e.getMessage());
		}
	}
}
