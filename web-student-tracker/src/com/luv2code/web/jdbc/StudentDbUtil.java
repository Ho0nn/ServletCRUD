package com.luv2code.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class StudentDbUtil {
    private DataSource dataSource;

    public StudentDbUtil(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Student> getStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        Connection con = null;
        Statement stmt = null;
        ResultSet rslt = null;

        try {
            con = dataSource.getConnection();
            String sql = "SELECT * FROM student ORDER BY last_name";
            stmt = con.createStatement();
            rslt = stmt.executeQuery(sql);

            while (rslt.next()) {
                int id = rslt.getInt("id");
                String firstName = rslt.getString("first_name");
                String lastName = rslt.getString("last_name");
                String email = rslt.getString("email");
                Student tempStudent = new Student(id, firstName, lastName, email);
                students.add(tempStudent);
            }
            return students;
        } finally {
            close(con, stmt, rslt);
        }
    }

    private void close(Connection con, Statement stmt, ResultSet rslt) {
        try {
            if (rslt != null) {
                rslt.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addStudent(Student student) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = dataSource.getConnection();
            String sql = "INSERT INTO student (first_name, last_name, email) VALUES (?, ?, ?)";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getEmail());
            stmt.execute();
        } finally {
            close(con, stmt, null);
        }
    }

    public Student getStudent(String theStudentId) throws Exception {
        Student student = null;
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rslt = null;
        int studentId;

        try {
            studentId = Integer.parseInt(theStudentId);
            con = dataSource.getConnection();
            String sql = "SELECT * FROM student WHERE id=?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, studentId);
            rslt = stmt.executeQuery();

            if (rslt.next()) {
                String firstName = rslt.getString("first_name");
                String lastName = rslt.getString("last_name");
                String email = rslt.getString("email");
                student = new Student(studentId, firstName, lastName, email);
            }
            return student;
        } finally {
            close(con, stmt, rslt);
        }
    }

    public void updateStudent(Student theStudent) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = dataSource.getConnection();
            String sql = "UPDATE student SET first_name=?, last_name=?, email=? WHERE id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, theStudent.getFirstName());
            stmt.setString(2, theStudent.getLastName());
            stmt.setString(3, theStudent.getEmail());
            stmt.setInt(4, theStudent.getId());
            stmt.execute();
        } 
        finally {
            close(conn, stmt, null);
        }
    }

	public void deleteStudent(String theStudentId) throws Exception {
		  Connection conn = null;
	        PreparedStatement stmt = null;

	        try {
	        	int studentId = Integer.parseInt(theStudentId);
	            conn = dataSource.getConnection();
	            String sql = "DELETE from student WHERE id=?";
	            stmt = conn.prepareStatement(sql);
	            stmt.setInt(1,studentId);
	            stmt.execute();
	           
	        } 
	        finally {
	            close(conn, stmt, null);
	        }
	}
}
