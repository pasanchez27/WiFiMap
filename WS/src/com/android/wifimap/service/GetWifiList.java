package com.android.wifimap.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.android.wifimap.service.dao.DataBase;
import com.google.gson.Gson;

@WebServlet("/GetWifi")
public class GetWifiList extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3835618569255042016L;
	Gson gson = new Gson();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		double latitude = Double.parseDouble(request.getParameter("lat"));
		double longitude = Double.parseDouble(request.getParameter("lon"));
		System.out.println("Obtener Redes cerca de: Latitude: " + latitude + " - Longitude" + longitude);
		
		PrintWriter pw = response.getWriter();
		response.setContentType("application/json");
		
		List<NetPoint> points = getNearConnections(latitude, longitude);
		pw.print(gson.toJson(points));
	}



	private List<NetPoint> getNearConnections(double latitude, double longitude) {
		List<NetPoint> list = new ArrayList<NetPoint>();
		try {
			list = DataBase.getNearestConnections(latitude, longitude);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
}
