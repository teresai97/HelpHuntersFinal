package app;

import javax.servlet.http.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.google.gson.Gson;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ClientProfile extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Entro al perfil");

        HttpSession session = request.getSession(true);

        // Traer datos de perfil

        String name = (String)session.getAttribute("login");
        int id = (int)session.getAttribute("loginid");

        System.out.println("Nombre en sesión " + name);
        System.out.println("Nombre en sesión " + id);

        ResponseProfile res = null;

        //Necesito el nombre, apellido, email y género del cliente.

        //Necesito el número de veces que aparece su client id en la tabla EmploymentRecord
        //y la suma de todas las horas de los trabajos que ha contratado ese cliente.

        String stringCon = "jdbc:mysql://localhost/Caregivers?user=equipo&password=Tecnun2020";
        try {
            Connection con = DriverManager.getConnection(stringCon);
            ClientData client = ClientData.getInfo(con, id, false);

            int[] infoemployments = EmploymentRecordData.countEmployments(con, id);
            //int sum = EmploymentRecordData.addHours(con, id);
            System.out.println("name: " + client.firstname + " last name: " + client.lastname + " email: " +
                    client.email + " gender: " + client.gender);
            System.out.println("Number of employment offers: " + infoemployments);

            res = new ResponseProfile(client.firstname, client.lastname, client.email, client.gender, infoemployments[0], infoemployments[1]);

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Exception when connecting to Driver: " + e);

            //Mejorar la forma de cachar la excepción.
        }

        String json = new Gson().toJson(res);
        System.out.println("Wrote Json");

        response.getWriter().write(json);
        System.out.println("Terminé la petición voy a regresar la respuesta");

    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstname = request.getParameter("firstname");
		String lastname = request.getParameter( "lastname");
        String email = request.getParameter("email");
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");

        System.out.println("Datos Recibidos: " +  firstname + " " +  lastname +  " " + email + " " +  currentPassword + " " + newPassword);

        HttpSession session = request.getSession(true);
        int id = (int)session.getAttribute("loginid");

        String stringCon = "jdbc:mysql://localhost/Caregivers?user=equipo&password=Tecnun2020";
        Connection connection;
        try {

            System.out.println("id de sesion es: " + id);

            if(newPassword == null){
                System.out.println("No hay nueva contra");
            }else{
                System.out.println("Por alguna razón hay contra");

            }



            connection = DriverManager.getConnection(stringCon);

            ClientData client = ClientData.getInfo(connection, id, true);
            
            System.out.println("El cliente es " + client.password);
            if(newPassword != null){
                // TODO Terminar es funcionalidad
                System.out.println("El usuario está intentado cambiar la contraseña");
                if (client.password.equals(currentPassword) ) {
                    System.out.println(" Ok la contraseña coincide procedemos a cambiarla...");
                }else{
                    Response res = new Response(true,"Please validate your current they don't  match");
                    String json = new Gson().toJson(res);
                    response.getWriter().write(json);
                }
            }else{
                System.out.println("Solo vamos a cambiar los datos de firstname, lastname, email");
                client.setLastname(lastname);
                client.setFirstname(firstname);
                client.setEmail(email);
                client.setClientID(id);
                ClientData.updateClientData(connection, client, false);

                Response res = new Response(false, "Your data was updated");
                String json = new Gson().toJson(res);
                response.getWriter().write(json);
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            Response res = new Response(true,"Sorry, an unexpeted error have ocurred");
            String json = new Gson().toJson(res);
            response.getWriter().write(json);

        } 
    }

}