package registerUniqueDev;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import registerUniqueDev.utilities.ByCryptPasswordHashing;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.UUID;

@WebServlet(name = "LoginServlet", value = "/loginurl")
public class LoginServlet extends HttpServlet {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    Statement st = null;
    int rowCount;
    RequestDispatcher rd = null;
    ByCryptPasswordHashing hashingTool = new ByCryptPasswordHashing();
    private final String GET_USER_RECORD = "SELECT * FROM users where user_email=?";
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter pw = response.getWriter();
        response.setContentType("text/html");

        String uemail = request.getParameter("username");
        String upass = request.getParameter("password");

        if(!(uemail.length()>0)||(uemail.equals(" "))){
            request.setAttribute("status", "emptyusername");
            rd = request.getRequestDispatcher("login.jsp");
            rd.forward(request, response);
        }

        if(!(upass.length()>0)||(upass.equals(" "))){
            request.setAttribute("status", "emptypass");
            rd = request.getRequestDispatcher("login.jsp");
            rd.forward(request, response);
        }

        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/monkey", "postgres","root");
            if(conn!=null) {
                ps = conn.prepareStatement(GET_USER_RECORD);
                ps.setString(1, uemail);
            }
            if(ps!=null) {
                try {
                    rs = ps.executeQuery();
                }catch (Exception e){
                    e.printStackTrace();
                    System.err.println(e.getClass().getName() + " " + e.getMessage());
                    request.setAttribute("status", "psql");
                    rd = request.getRequestDispatcher("login.jsp");
                    rd.forward(request, response);
                }
            }
            if (rs.next()) {
                    if (hashingTool.checkPassword(upass, rs.getString(6))) {
                        HttpSession session = request.getSession();
                        session.setAttribute("name", UUID.randomUUID().toString());
                        session.setAttribute("username", rs.getString(2));
//                        request.setAttribute("email", uemail);
                        request.setAttribute("status", "correct");
                        rd = request.getRequestDispatcher("index.jsp");
                        rd.forward(request, response);
                    }else {
                        request.setAttribute("status", "NoMatch");
                        rd = request.getRequestDispatcher("login.jsp");
                        rd.forward(request, response);
                    }
            }else {
                request.setAttribute("status", "usernotavailable" );
                rd = request.getRequestDispatcher("registration.jsp");
                rd.forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + " " + e.getMessage());
        }finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (ps !=null){
                    ps.close();
                }
                if (rs !=null){
                    rs.close();
                }
            }catch (Exception e){
                e.printStackTrace();
                System.err.println(e.getClass().getName() + " " + e.getMessage());
            }
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
