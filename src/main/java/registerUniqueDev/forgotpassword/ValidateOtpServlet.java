package registerUniqueDev.forgotpassword;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "EnterOtpServlet", value = "/enterotpurl")
public class ValidateOtpServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = null;
        HttpSession session = request.getSession();
        String otpFromUser = request.getParameter("otp");
        String otpFromSession = (String) session.getAttribute("otp");

        if(!otpFromUser.equals(otpFromSession)){
            rd = request.getRequestDispatcher("EnterOtp.jsp");
            request.setAttribute("status","wrong_otp" );
            rd.forward(request, response);
        }

        response.sendRedirect("newPassword.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
doGet(request, response);
    }
}
