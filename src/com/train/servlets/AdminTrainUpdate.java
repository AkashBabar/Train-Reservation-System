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
import com.train.service.TrainService;
import com.train.service.impl.TrainServiceImpl;

@SuppressWarnings("serial")
@WebServlet("/adminupdatetrain")
public class AdminTrainUpdate extends HttpServlet {

	private TrainService trainService = new TrainServiceImpl();

	/**
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		res.setContentType("text/html");
		PrintWriter pw = res.getWriter();

		try {
			String trainNo = req.getParameter("trainnumber");
			TrainBean train = trainService.getTrainById(trainNo);
			if (train != null) {
				RequestDispatcher rd = req.getRequestDispatcher("AdminUpdateTrain.html");
				rd.include(req, res);
				pw.println("<div class='card'>");
				pw.println("<h2>Train Schedule Update</h2>");
				pw.println("<p class='menu'>Update the details of the train below and submit.</p>");
				pw.println("<form action='updatetrainschedule' method='post' class='train-form'>");

				// Train Number
				pw.println("<div class='form-group'>");
				pw.println("<label>Train Number</label>");
				pw.println("<input type='text' name='trainno' value='" + train.getTr_no() + "' readonly>");
				pw.println("</div>");

				// Train Name
				pw.println("<div class='form-group'>");
				pw.println("<label>Train Name</label>");
				pw.println("<input type='text' name='trainname' value='" + train.getTr_name() + "' required>");
				pw.println("</div>");

				// From Station
				pw.println("<div class='form-group'>");
				pw.println("<label>From Station</label>");
				pw.println("<input type='text' name='fromstation' value='" + train.getFrom_stn() + "' required>");
				pw.println("</div>");

				// To Station
				pw.println("<div class='form-group'>");
				pw.println("<label>To Station</label>");
				pw.println("<input type='text' name='tostation' value='" + train.getTo_stn() + "' required>");
				pw.println("</div>");

				// Available Seats
				pw.println("<div class='form-group'>");
				pw.println("<label>Available Seats</label>");
				pw.println("<input type='number' name='available' value='" + train.getSeats() + "' required>");
				pw.println("</div>");

				// Fare
				pw.println("<div class='form-group'>");
				pw.println("<label>Fare (INR)</label>");
				pw.println("<input type='number' name='fare' value='" + train.getFare() + "' required>");
				pw.println("</div>");

				// Submit Button
				pw.println("<input type='submit' value='Update Train Schedule' class='btn-submit'>");

				pw.println("</form>");
				pw.println("</div>");

			} else {
				RequestDispatcher rd = req.getRequestDispatcher("AdminUpdateTrain.html");
				rd.include(req, res);
				pw.println("<div class='tab'>Train Not Available</div>");
			}
		} catch (Exception e) {
			throw new TrainException(422, this.getClass().getName() + "_FAILED", e.getMessage());

		}

	}

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		doPost(req, res);
	}

}
