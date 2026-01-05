package com.train.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.train.beans.TrainException;
import com.train.constant.ResponseCode;

public class ErrorHandlerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		res.setContentType("text/html");
		PrintWriter pw = res.getWriter();

		Throwable throwable = (Throwable) req.getAttribute("javax.servlet.error.exception");
		Integer statusCode = (Integer) req.getAttribute("javax.servlet.error.status_code");
		String servletName = (String) req.getAttribute("javax.servlet.error.servlet_name");
		String requestUri = (String) req.getAttribute("javax.servlet.error.request_uri");

		String errorMessage = ResponseCode.INTERNAL_SERVER_ERROR.getMessage();
		String errorCode = ResponseCode.INTERNAL_SERVER_ERROR.name();

		if (statusCode == null) {
			statusCode = 500;
		}

		Optional<ResponseCode> responseCode = ResponseCode.getMessageByStatusCode(statusCode);
		if (responseCode.isPresent()) {
			errorMessage = responseCode.get().getMessage();
			errorCode = responseCode.get().name();
		}

		if (throwable instanceof TrainException) {
			TrainException te = (TrainException) throwable;
			errorMessage = te.getMessage();
			statusCode = te.getStatusCode();
			errorCode = te.getErrorCode();
		} else if (throwable != null) {
			errorMessage = throwable.getMessage();
			errorCode = "UNEXPECTED_ERROR";
		}

		System.out.println("====== ERROR TRIGGERED ======");
		System.out.println("Servlet Name : " + servletName);
		System.out.println("Request URI  : " + requestUri);
		System.out.println("Status Code  : " + statusCode);
		System.out.println("Error Code   : " + errorCode);
		System.out.println("Message      : " + errorMessage);
		System.out.println("============================");

		if (statusCode == 401) {
			RequestDispatcher rd = req.getRequestDispatcher("UserLogin.html");
			rd.include(req, res);
			pw.println("<div class='tab'><p class='menu'>" + errorMessage + "</p></div>");
		} else {
			RequestDispatcher rd = req.getRequestDispatcher("error.html");
			rd.include(req, res);
			pw.println("<div style='margin-top:20%; text-align:center;'>" + "<p class='menu' style='color:red'>"
					+ errorCode + "</p><br>" + "<p class='menu'>" + errorMessage + "</p>" + "</div>");
		}
	}
}
