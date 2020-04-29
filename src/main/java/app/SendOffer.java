package app;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

public class SendOffer extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub

        System.out.println("Entro al perfil");

        HttpSession session = request.getSession(true);

        // Traer datos de perfil

        String name = (String)session.getAttribute("login");
        int id = (int)session.getAttribute("loginid");

        System.out.println("Nombre en sesión " + name);
        System.out.println("Nombre en sesión " + id);

        int  caregiverID =  Integer.parseInt(request.getParameter( "caregiverID"));
        String startdateString = request.getParameter("startdate");
        int hoursperweek = Integer.parseInt(request.getParameter("hoursperweek"));

        boolean error = false;
        String descriptionError = "";
        int n = 0;
        System.out.println("startdateString" + startdateString);

        // Check the startdateString

        if (startdateString != null) {
            startdateString.trim();
            if (startdateString.equals("") == true) {
                error = true;
                descriptionError = descriptionError.concat("The field containing the start date must not be empty. ");
            }
        } else {
            error = true;
            descriptionError = descriptionError.concat("The field containing the start date must not be empty. ");
        }

        // Now check the hoursperweek

        if (hoursperweek == 0) {
            error = true;
            descriptionError = descriptionError.concat("Please introduce a number to indicate the hours the caregiver will have to work per week. ");
        }

        Date startdate = null;

        try {
            startdate = new SimpleDateFormat("dd/MM/yyyy").parse(startdateString);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception when parsing to date: " + e);
        }

        if (startdate == null) {
            error = false;
            descriptionError = descriptionError.concat("There was a problem when parsing the start date from string to Date. ");
        }

        System.out.println(descriptionError);

        Response res = new Response(error, "");

        if (error == false) {
            String stringCon = "jdbc:mysql://localhost/Caregivers?user=equipo&password=Tecnun2020";
            try {
                Connection con = DriverManager.getConnection(stringCon);
                EmploymentRecordData newjob = new EmploymentRecordData(id, caregiverID, startdate, hoursperweek, 1);
                n = EmploymentRecordData.insertjob(con, newjob);
                if (n == 1) {
                    res.setDescription("Your job offer was sent successfully!");
                } else {
                    res.setDescription("There was a problem when executing the insert query.");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                System.out.println(throwables);
            }
        } else {
            res.setDescription(descriptionError);
        }

        System.out.println("Number of insertions in EmploymentRecord: " + n);

        String json = new Gson().toJson(res);
        System.out.println("Wrote Json");

        response.getWriter().write(json);
        System.out.println("Terminé la petición voy a regresar la respuesta");

    }
}
