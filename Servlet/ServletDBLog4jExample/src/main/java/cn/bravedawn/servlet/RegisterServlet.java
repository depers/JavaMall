package cn.bravedawn.servlet;

import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


@WebServlet(name = "register", urlPatterns = "/register")
public class RegisterServlet extends HttpServlet {


    static Logger log = Logger.getLogger(RegisterServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String country = request.getParameter("country");
        String errorMsg = null;

        if (email == null || email.equals("")) {
            errorMsg = "Email ID can't be null or empty.";
        }
        if (password == null || password.equals("")) {
            errorMsg = "Password can't be null or empty.";
        }
        if (name == null || name.equals("")) {
            errorMsg = "Name can't be null or empty.";
        }
        if (country == null || country.equals("")) {
            errorMsg = "Country can't be null or empty.";
        }

        if (errorMsg != null) {
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/register.html");
            PrintWriter out = response.getWriter();
            out.println("<font color=red>" + errorMsg + "</font>");
            rd.include(request, response);
        } else {
            Connection con = (Connection) getServletContext().getAttribute("DBConnection");
            PreparedStatement ps = null;

            try {
                ps = con.prepareStatement("insert into Users(name,email,country, password) values (?,?,?,?)");
                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, country);
                ps.setString(4, password);
                ps.execute();

                log.info("User registered with email=" + email);

                //forward to login page to login
                RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.html");
                PrintWriter out = response.getWriter();
                out.println("<font color=green>Registration successful, please login below.</font>");
                rd.include(request, response);
            } catch (SQLException e) {
                e.printStackTrace();
                log.error("Database connection problem");
                throw new ServletException("DB Connection problem.");
            } finally {
                try {
                    ps.close();
                } catch (SQLException e) {
                    log.error("SQLException in closing PreparedStatement");
                }
            }
        }

    }
}
