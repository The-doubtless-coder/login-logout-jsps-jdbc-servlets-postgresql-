package registerUniqueDev.forgotpassword;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import registerUniqueDev.utilities.ByCryptPasswordHashing;

import java.io.IOException;
import java.sql.*;

@WebServlet(name = "ChangePasswordServlet", value = "/changepasswordurl")
public class ChangePasswordServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ByCryptPasswordHashing hashingTool = new ByCryptPasswordHashing();
        String GET_USER = "select * from users where user_email=?";
        String SIMPLER_UPDATE_QUERY = "UPDATE USERS SET password=? where user_email=?";
        HttpSession session = request.getSession();
        String userEmail = (String) session.getAttribute("user");
        RequestDispatcher rd;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String first_pass = request.getParameter("password").toLowerCase().trim();
        String second_pass = request.getParameter("confPassword").toLowerCase().trim();

        if(!(first_pass.length()>0)||!(second_pass.length()>0)){
            rd = request.getRequestDispatcher("newPassword.jsp");
            request.setAttribute("status", "empty_val");
            rd.forward(request, response);
            return;
        }
        if(!(first_pass.equals(second_pass))){
            rd = request.getRequestDispatcher("newPassword.jsp");
            request.setAttribute("status", "no_match");
            rd.forward(request, response);
            return;
        }
        try{
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/monkey", "postgres", "root");
            if(conn!=null){
                ps = conn.prepareStatement(GET_USER, ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
            }
            if(ps!=null){
                ps.setString(1, userEmail);
                rs = ps.executeQuery();
            }
            if(rs!=null) {
                if (rs.next()) {
                    System.out.println(rs.getInt(1) + rs.getString(2) + rs.getString(3));
                        rs.absolute(1);
                        rs.updateString("password", hashingTool.hashPassword(first_pass));
                        rs.updateRow();
                    rd = request.getRequestDispatcher("login.jsp");
                    request.setAttribute("status", "change_success");
                    rd.forward(request, response);
                }else {
                    rd = request.getRequestDispatcher("newPassword.jsp");
                    request.setAttribute("status", "no_rs");
                    rd.forward(request, response);
                }
            }else {
                rd = request.getRequestDispatcher("newPassword.jsp");
                request.setAttribute("status", "db_error");
                rd.forward(request, response);
            }
        }catch (SQLException | ClassNotFoundException s){
            s.printStackTrace();
            rd = request.getRequestDispatcher("newPassword.jsp");
            request.setAttribute("status", "db_error");
            rd.forward(request, response);
        } catch (Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName() + " " + e.getMessage());
        }finally {
            try{
                if(conn!=null){
                    conn.close();
                }
                if(ps!=null){
                    ps.close();
                }
                if(rs!=null){
                    rs.close();
                }
            }catch (SQLException s){
                s.printStackTrace();
            }
        }


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
doGet(request, response);
    }
}
