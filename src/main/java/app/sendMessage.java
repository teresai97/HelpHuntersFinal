package app;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;

@SuppressWarnings("serial")
public class sendMessage extends HttpServlet {
    Connection connection;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        connection = ConnectionUtils.getConnection(config);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException  {
        res.setContentType("text/html");
        //String client = req.getParameter("login");
        HttpSession session = req.getSession(false);
        String login = null;
		int loginid = 0;
        if (session != null) {
            login = (String)session.getAttribute("login");
			loginid = (int)session.getAttribute("loginid");
			session.setAttribute("login", login);
			session.setAttribute("loginid", loginid);
            System.out.println("sendMessage logged");
            System.out.println("sendMessage login: " + login);
			System.out.println("sendMessage loginid: " + loginid);
        }
     Integer caregiverID = Integer.parseInt(req.getParameter("caregiverID"));
	 String subject = req.getParameter("subject");
	 String messages = req.getParameter("message");

	  messageData message = new messageData(
					loginid,
					caregiverID,
					subject,
                    messages
					);
					
    int n = messageData.sendMessage(connection, message);
	caregiverData caregiver = caregiverData.getCaregiver(connection, caregiverID);
	res.sendRedirect("caregiverProfile?firstname="+caregiver.firstname+"&lastname="+caregiver.lastname+"&caregiverID="+caregiverID+"&hourlyrate="+caregiver.hourlyrate+"&description="+caregiver.description+"");
       
    }
}