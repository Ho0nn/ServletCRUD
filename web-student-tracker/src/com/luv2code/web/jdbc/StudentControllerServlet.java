package com.luv2code.web.jdbc;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private StudentDbUtil studentDbUtil;
    @Resource(name = "jdbc/web_student_tracker")
    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        super.init();

        try {
            studentDbUtil = new StudentDbUtil(dataSource);
        } catch (Exception exc) {
            throw new ServletException(exc);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            String command = request.getParameter("command");
            if (command == null) command = "LIST";

            switch (command) {
                case "LIST":
                    listStudents(request, response);
                    break;

                case "ADD":
                    addStudent(request, response);
                    break;

                case "LOAD":
                    loadStudent(request, response);
                    break;
                    
                case "UPDATE":
                    updateStudent(request, response);
                case "DELETE":
                    deleteStudent(request, response);
                    break;

                
                default:
                    listStudents(request, response);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
    
    

    private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String theStudentId = request.getParameter("studentId");
		
		studentDbUtil.deleteStudent(theStudentId);
		
		listStudents(request, response);
	}

	private void updateStudent(HttpServletRequest request, HttpServletResponse response)
    		throws Exception {

    		// read student info from form data
    		int id = Integer.parseInt(request.getParameter("studentId"));
    		String firstName = request.getParameter("firstName");
    		String lastName = request.getParameter("lastName");
    		String email = request.getParameter("email");
    		
    		// create a new student object
    		Student student = new Student(id, firstName, lastName, email);
    		
    		// perform update on database
    		studentDbUtil.updateStudent(student);
    		
    		// send them back to the "list students" page
    		listStudents(request, response);
    		
    	}

	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String studentId = request.getParameter("studentId");
        Student student = studentDbUtil.getStudent(studentId);
        request.setAttribute("THE_STUDENT", student);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp");
        dispatcher.forward(request, response);
    }

    private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        Student student = new Student(firstName, lastName, email);
        studentDbUtil.addStudent(student);
        listStudents(request, response);
    }

    private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Student> students = studentDbUtil.getStudents();
        request.setAttribute("STUDENT_LIST", students);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
        dispatcher.forward(request, response);
    }
}
