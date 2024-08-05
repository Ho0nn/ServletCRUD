package com.luv2code.web.jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//Define datasource/connection pool for Resource injection
	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;
	
	
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out =response.getWriter();
		response.setContentType("text/plaim");
		
		Connection con=null;
		Statement stmt =null;
		ResultSet res=null;
		try {
			con =dataSource.getConnection();
			String sql= " select * from student";
			stmt = con.createStatement();
			res = stmt.executeQuery(sql);
			while(res.next()) {
				out.println();
				String email = res.getString("email");
				out.println(email);
			}
		}
		catch(Exception exc) {
			exc.getMessage();
		}
		
	}

}
