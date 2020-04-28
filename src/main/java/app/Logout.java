package app;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

public class Logout extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String description = "You have successfully logged out from your account.";

        response.setContentType("text/html");
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        Response res = new Response(false, description);

        String json = new Gson().toJson(res);
        System.out.println("Wrote Json");

        response.getWriter().write(json);
        System.out.println("Terminé la petición voy a regresar la respuesta");

    }


}