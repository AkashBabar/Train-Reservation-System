package com.train.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.train.beans.HistoryBean;
import com.train.beans.TrainException;
import com.train.constant.ResponseCode;
import com.train.service.BookingService;
import com.train.utility.DBUtil;

//Service Implementation class for booking details of the ticket
//Creates the booking history and saves to database
public class BookingServiceImpl implements BookingService {

	@Override
	public List<HistoryBean> getAllBookingsByCustomerId(String customerEmailId) throws TrainException {
		List<HistoryBean> transactions = new ArrayList<>();
		String query = "SELECT * FROM HISTORY WHERE MAILID=?";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {

			ps.setString(1, customerEmailId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				HistoryBean transaction = new HistoryBean();
				transaction.setTransId(rs.getString("transid"));
				transaction.setFrom_stn(rs.getString("from_stn"));
				transaction.setTo_stn(rs.getString("to_stn"));
				transaction.setDate(rs.getDate("travel_date").toString()); // Use toString for YYYY-MM-DD
				transaction.setMailId(rs.getString("mailid"));
				transaction.setSeats(rs.getInt("seats"));
				transaction.setAmount(rs.getDouble("amount"));
				transaction.setTr_no(rs.getString("tr_no"));
				transactions.add(transaction);
			}

		} catch (SQLException | TrainException e) {
			System.out.println(e.getMessage());
			throw new TrainException(e.getMessage());
		}
		return transactions;
	}

	@Override
	public HistoryBean createHistory(HistoryBean details) throws TrainException {
		HistoryBean history = null;
		String query = "INSERT INTO HISTORY (transid, mailid, tr_no, travel_date, from_stn, to_stn, seats, amount) VALUES(?,?,?,?,?,?,?,?)";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {

			String transactionId = UUID.randomUUID().toString();
			ps.setString(1, transactionId);
			ps.setString(2, details.getMailId());
			ps.setString(3, details.getTr_no());

			// Correctly convert String date (YYYY-MM-DD) to java.sql.Date
			Date sqlDate = Date.valueOf(details.getDate());
			ps.setDate(4, sqlDate);

			ps.setString(5, details.getFrom_stn());
			ps.setString(6, details.getTo_stn());
			ps.setInt(7, details.getSeats());
			ps.setDouble(8, details.getAmount());

			int response = ps.executeUpdate();
			if (response > 0) {
				history = details;
				history.setTransId(transactionId);
			} else {
				throw new TrainException(ResponseCode.INTERNAL_SERVER_ERROR);
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new TrainException(e.getMessage());
		}
		return history;
	}

}
