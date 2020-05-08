import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;

@SuppressWarnings("serial")
public class caregiverUpdate extends HttpServlet {
    Connection connection;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        connection = ConnectionUtils.getConnection(config);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException  {
		res.setContentType("text/html");
        PrintWriter toClient = res.getWriter();
        //String caregiver = req.getParameter("login");
        HttpSession session = req.getSession(false);
		String login = null;
        int id = 0;
		String password = null;
        if (session != null) {
			login = (String)session.getAttribute("login");
            id = (int)session.getAttribute("id");
			password = (String)session.getAttribute("password");
            session.setAttribute("id", id);
            session.setAttribute("password", password);
            System.out.println("caregiver logged");
			System.out.println("caregivername login: " + login);
            System.out.println("caregiverid login: " + id);
            System.out.println("caregiverid password: " + password);
        }

        
        String newpassword=null;      

        if (password == req.getParameter(password) && req.getParameter(newpassword) != null){
            newpassword = req.getParameter(newpassword);
        } else {
            newpassword = password;
        }





		int count = Integer.parseInt(req.getParameter("caregiverCount"));
		System.out.println("caregiverCount:" + count);
		
		int provincetest = Integer.parseInt(req.getParameter("provinceID"));
		System.out.println("provinceID:" + provincetest);
		
		provinceData province = new provinceData(	
					id,
                    provincetest				
                );
				
				
		//int n = caregiverData.updateCaregiver(connection, update);
		
		if (count != 0) {
			int m = provinceData.updateCaregiverProvince(connection, province);
        } else {
            int m = provinceData.insertCaregiverProvince(connection, province);
        }
		
		res.sendRedirect("caregiverSettings?id=" + id + "&a=" + Math.random());
    }
}





