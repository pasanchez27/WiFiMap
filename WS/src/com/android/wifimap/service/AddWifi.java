package com.android.wifimap.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.android.wifimap.service.dao.DataBase;

@WebServlet("/AddWifi")
public class AddWifi extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4664253455468762821L;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		double latitude = Double.parseDouble(request.getParameter("lat"));
		double longitude = Double.parseDouble(request.getParameter("lon"));
		String place = request.getParameter("place");
		String netName = request.getParameter("name");
		String netPwd = request.getParameter("pwd");
		
		NetPoint point = new NetPoint(latitude, longitude, place, netName, netPwd);
		System.out.println("Agregar Red: " + latitude + " ; " + longitude + " ; " + place + " ; " + netName + " ; " + netPwd);
		
		saveWifi(point);
		
		PrintWriter pw = response.getWriter();
		response.setContentType("application/json");
		pw.print("OK");
	}

	private void saveWifi(NetPoint point) {
		try {
//			DataBase.createTable();
			DataBase.insertWithPreparedStatement(point);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
