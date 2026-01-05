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
import com.train.constant.ResponseCode;
import com.train.constant.UserRole;
import com.train.service.TrainService;
import com.train.service.impl.TrainServiceImpl;
import com.train.utility.TrainUtil;

@WebServlet("/admincancletrain")
public class AdminCancleTrain extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
		TrainUtil.validateUserAuthorization(req, UserRole.ADMIN);
		try {
			String trainNo = req.getParameter("trainno");
			String message = trainService.deleteTrainById(trainNo);
			if (ResponseCode.SUCCESS.toString().equalsIgnoreCase(message)) {
				RequestDispatcher rd = req.getRequestDispatcher("AdminDeleteTrain.html");
				rd.include(req, res);
				pw.println("<div class='card'>");
				pw.println("<p class='menu' style='color:green; font-weight:600;'>✅ Train number " + trainNo
						+ " has been Deleted Successfully.</p>");
				pw.println("</div>");
			} else {
				RequestDispatcher rd = req.getRequestDispatcher("AdminDeleteTrain.html");
				rd.include(req, res);
				pw.println("<div class='card'>");
				pw.println("<p class='menu' style='color:red; font-weight:600;'>❌ Train No." + trainNo
						+ " is Not Available!</p>");
				pw.println("</div>");
			}

		} catch (Exception e) {
			throw new TrainException(422, this.getClass().getName() + "_FAILED", e.getMessage());
		}

	}

}
