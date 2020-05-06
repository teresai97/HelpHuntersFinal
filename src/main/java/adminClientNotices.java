import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;

@SuppressWarnings("serial")
public class adminClientNotices extends HttpServlet {
    Connection connection;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        connection = ConnectionUtils.getConnection(config);
	}

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException  {
        res.setContentType("text/html");
        PrintWriter toClient = res.getWriter();

        //We have to request the session when we login
        // HttpSession session = req.getSession(true);
        // int administratorID = Integer.parseInt(session.getAttribute("id").toString());
        // String firstname = session.getAttribute("fname").toString();
        // String lastname = session.getAttribute("lname").toString();
        // String email = session.getAttribute("usr").toString();
        // String password = session.getAttribute("pw").toString();

        //For the AJAX to work we need a request handler.. we choose to get the 'clientID' parameter
        int clientID = Integer.parseInt(req.getParameter("clientID"));

        Vector<adminNoticeData> noticeList = adminNoticeData.getadminNoticeDataByClient(connection, clientID);
          toClient.print("[");
            for(int i = 0; i < noticeList.size(); i++){
              adminNoticeData notice = noticeList.elementAt(i);
              if (i!=0) {
                  toClient.print(",");
              }
              toClient.print("{");
              toClient.print("\"noticeID\":\"" + notice.noticeID + "\"");
              toClient.print(",\"clientID\":\"" + notice.clientID + "\"");
              toClient.print(",\"subject\":\"" + notice.subject + "\"");
              toClient.print(",\"message\":\"" + notice.message + "\"");
              toClient.print(",\"datecreated\":\"" + notice.datecreated + "\"");
              //toClient.print(",\"replyhasbeenread\":\"" + notice.replyhasbeenread + "\"");
              toClient.print("}");
          }
        toClient.print("]");


            }
	}
