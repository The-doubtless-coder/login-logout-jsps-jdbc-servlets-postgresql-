package registerUniqueDev.forgotpassword;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import registerUniqueDev.mailservice.JavaMailService;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Random;

@WebServlet(name = "ForgotPasswordServlet", value = "/forgotPasswordUrl")
public class ForgotPasswordServlet extends HttpServlet {
    RequestDispatcher rd = null;
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    private String otp = null;
    private final String FIND_USER = "select * from users where user_email=?";
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter pw = response.getWriter();
        response.setContentType("text/html");
        HttpSession session = request.getSession();

        String email = request.getParameter("email").trim();
        System.out.println(email);
        //todo: regex to validate email
        if(!(email.length()>0)){
            request.setAttribute("status", "empty");
            rd = request.getRequestDispatcher("forgotPassword.jsp");
            rd.forward(request, response);
        }
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/monkey", "postgres", "root");
            if (conn != null) {
                ps = conn.prepareStatement(FIND_USER);
            }
            if (ps != null) {
                ps.setString(1, email);
                rs = ps.executeQuery();
            }
            if (rs.next()) {
                System.out.println(".next not empty");
                Random random = new Random();
                otp = String.valueOf(random.nextInt(9999));
                    //todo: send otp to email
                    System.out.println(rs.getInt(1) + " " + rs.getString(2));
                    try {
                        JavaMailService.sendMail(email, "ABOUT YOUR FORGOTTEN PASSWORD", "Your One time password is: " + otp);
                    }catch (Exception e){
                        e.printStackTrace();
                        rd = request.getRequestDispatcher("forgotPassword.jsp");
                        request.setAttribute("status", "not_sent");
                        rd.forward(request, response);
                    }
                request.setAttribute("status", "mail_sent");
                    session.setAttribute("otp", otp);
                    session.setAttribute("user", email);
                    rd = request.getRequestDispatcher("EnterOtp.jsp");
                    rd.forward(request, response);
            } else {
                System.out.println("rs.next is empty! - false");
                request.setAttribute("status", "user_unavailable");
                rd = request.getRequestDispatcher("forgotPassword.jsp");
                rd.forward(request, response);}

        }catch (SQLException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName() + " " + e.getMessage());
        }finally {
            try{
                if(conn!=null){
                    conn.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
            try{
                if(ps!=null){
                    ps.close();
                }
            }catch (SQLException E){
                E.printStackTrace();
            }
            try{
                if(rs!=null){
                    rs.close();
                }
            }catch (SQLException f){
                f.printStackTrace();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
