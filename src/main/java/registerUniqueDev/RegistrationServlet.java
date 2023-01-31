package registerUniqueDev;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.postgresql.util.PSQLException;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@WebServlet(name = "RegistrationServlet", value = "/registerurl")
public class RegistrationServlet extends HttpServlet {
    private final String REGISTER_USER = "insert into users (username, user_email, tel_number, created_on, password) " +
            "values (?,?,?,?,?)";
    private DateTimeFormatter timeFormatter;
    PreparedStatement ps = null;
    Statement st = null;
    ResultSet rs = null;
    Connection conn = null;
    RequestDispatcher rd = null;
    int rowCount;
    String hashedPassword;
    ByCryptPasswordHashing hashingTool = new ByCryptPasswordHashing();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter pw = response.getWriter();
        response.setContentType("text/html");
        String uname = request.getParameter("name");
        String uemail = request.getParameter("email");
        String uphone = request.getParameter("contact");
        String upass = request.getParameter("pass");
        String repeatPwd = request.getParameter("re_pass");
        LocalDateTime dateTime = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(dateTime);

        if(!(uname.length()>0)||(uname.equals(" "))){
            request.setAttribute("status", "emptyname");
            rd = request.getRequestDispatcher("registration.jsp");
            rd.forward(request, response);
        }
        if(!(uemail.length()>0)||(uemail.equals(" "))){
            request.setAttribute("status", "emptyemail");
            rd = request.getRequestDispatcher("registration.jsp");
            rd.forward(request, response);
        }
        if(!(uphone.length()>0)||(uphone.equals(" "))){
            request.setAttribute("status", "emptyphone");
            rd = request.getRequestDispatcher("registration.jsp");
            rd.forward(request, response);
        }
        if(!(upass.length()>0)||(upass.equals(" "))){
            request.setAttribute("status", "emptypass");
            rd = request.getRequestDispatcher("registration.jsp");
            rd.forward(request, response);
        }
        if(!(repeatPwd.length()>0)||(repeatPwd.equals(" "))){
            request.setAttribute("status", "emptyrepeatPwd");
            rd = request.getRequestDispatcher("registration.jsp");
            rd.forward(request, response);
        }

        try{
        if (!(upass.equals(repeatPwd))) {
            throw new ServletException("Passwords do not match!");
        }}catch (Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName() + " " + e.getMessage());
            request.setAttribute("status", "NoMatch");
            request.getRequestDispatcher("registration.jsp").forward(request, response);
        }

        hashedPassword = hashingTool.hashPassword(upass);
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/monkey",
                    "postgres", "root");
            if (conn != null) {
                ps = conn.prepareStatement(REGISTER_USER);
            }
            if (ps != null) {
                ps.setString(1, uname);
                ps.setString(2, uemail);
                ps.setString(3, uphone);
                ps.setTimestamp(4, Timestamp.valueOf(dateTime));
                ps.setString(5, hashedPassword);
                try {
                    rowCount = ps.executeUpdate();
                    rs = ps.getGeneratedKeys();
                }catch (PSQLException pe){
                    pe.printStackTrace();
                    request.setAttribute("status", "psql");
                    request.getRequestDispatcher("registration.jsp").forward(request,response);
                }
                System.out.println(rs);
            }
            rd = request.getRequestDispatcher("registration.jsp");
            if (rowCount > 0) {
                request.setAttribute("status", "succeeded");
            } else {
                request.setAttribute("status", "failed");
            }
            rd.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + " " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName() + " " + e.getMessage());
            }
        }
        System.out.println("connection made to db successfully");
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
