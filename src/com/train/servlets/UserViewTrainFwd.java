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
@WebServlet("/userviewtrainfwd")
public class UserViewTrainFwd extends HttpServlet {

    private TrainService trainService = new TrainServiceImpl();

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

        // Validate that the logged-in user is a customer
        TrainUtil.validateUserAuthorization(req, UserRole.CUSTOMER);

        res.setContentType("text/html");
        PrintWriter pw = res.getWriter();

        // Include static HTML (Navbar + layout)
        RequestDispatcher rd = req.getRequestDispatcher("UserViewTrains.html");
        rd.include(req, res);

        try {
            List<TrainBean> trains = trainService.getAllTrains();

            // Wrap all dynamic content inside #train-container
            pw.println("<script>");
            pw.println("document.getElementById('train-container').innerHTML = `");

            pw.println("<div class='main-content'>");

            if (trains != null && !trains.isEmpty()) {

                pw.println("<h2>Running Trains</h2>");
                pw.println("<p>Select a train and proceed to booking.</p>");

                pw.println("<table class='train-table'>");
                pw.println("<tr>"
                        + "<th>Train Name</th>"
                        + "<th>Train No</th>"
                        + "<th>From</th>"
                        + "<th>To</th>"
                        + "<th>Time</th>"
                        + "<th>Seats</th>"
                        + "<th>Fare</th>"
                        + "<th>Booking</th>"
                        + "</tr>");

                for (TrainBean train : trains) {

                    // Randomized departure time for display purposes
                    int hr = (int) (Math.random() * 24);
                    int min = (int) (Math.random() * 60);
                    String time = String.format("%02d:%02d", hr, min);

                    pw.println("<tr>");
                    pw.println("<td>" + train.getTr_name() + "</td>");
                    pw.println("<td>" + train.getTr_no() + "</td>");
                    pw.println("<td>" + train.getFrom_stn() + "</td>");
                    pw.println("<td>" + train.getTo_stn() + "</td>");
                    pw.println("<td>" + time + "</td>");
                    pw.println("<td>" + train.getSeats() + "</td>");
                    pw.println("<td>₹" + train.getFare() + "</td>");
                    pw.println("<td>"
                            + "<a class='book-btn' href='booktrainbyref?trainNo=" + train.getTr_no()
                            + "&fromStn=" + train.getFrom_stn()
                            + "&toStn=" + train.getTo_stn()
                            + "'>Book Now</a>"
                            + "</td>");
                    pw.println("</tr>");
                }

                pw.println("</table>");

            } else {
                pw.println("<h2>No Running Trains</h2>");
                pw.println("<p>Please check again later.</p>");
            }

            pw.println("</div>`;"); // end of innerHTML string
            pw.println("</script>");

        } catch (Exception e) {
            throw new TrainException(422, this.getClass().getSimpleName() + "_FAILED", e.getMessage());
        }
    }
}
