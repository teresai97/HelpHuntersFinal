package app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

public class NoticeData {
    int noticeID;
    String subject;
    String message;
    Date datecreated;
    String reply;
    int administratorID;
    Date datereplied;
    Boolean replyhasbeenread;
    int classification;
    int employmentID;
    String caregiverfirstname;
    String caregiverlastname;
    String administratorfirstname;
    String administratorlastname;

    public NoticeData (int noticeID, String subject, String message, Date datecreated, String reply, int administratorID, Date datereplied, Boolean replyhasbeenread, int classification, int employmentID, String caregiverfirstname, String caregiverlastname, String administratorfirstname, String administratorlastname) {
        this.noticeID = noticeID;
        this.subject = subject;
        this.message = message;
        this.datecreated = datecreated;
        this.reply = reply;
        this.administratorID = administratorID;
        this.datereplied = datereplied;
        this.replyhasbeenread = replyhasbeenread;
        this.classification = classification;
        this.employmentID = employmentID;
        this.caregiverfirstname = caregiverfirstname;
        this.caregiverlastname = caregiverlastname;
        this.administratorfirstname = administratorfirstname;
        this.administratorlastname = administratorlastname;
    }

    public NoticeData (String subject, String message, int classification, int employmentID) {
        this.subject = subject;
        this.message = message;
        this.classification = classification;
        this.employmentID = employmentID;
    }

    public int getNoticeID() {
        return noticeID;
    }

    public void setNoticeID(int noticeID) {
        this.noticeID = noticeID;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(Date datecreated) {
        this.datecreated = datecreated;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public int getAdministratorID() {
        return administratorID;
    }

    public void setAdministratorID(int administratorID) {
        this.administratorID = administratorID;
    }

    public Date getDatereplied() {
        return datereplied;
    }

    public void setDatereplied(Date datereplied) {
        this.datereplied = datereplied;
    }

    public Boolean getReplyhasbeenread() {
        return replyhasbeenread;
    }

    public void setReplyhasbeenread(Boolean replyhasbeenread) {
        this.replyhasbeenread = replyhasbeenread;
    }

    public int getClassification() {
        return classification;
    }

    public void setClassification(int classification) {
        this.classification = classification;
    }

    public int getEmploymentID() {
        return employmentID;
    }

    public void setEmploymentID(int employmentID) {
        this.employmentID = employmentID;
    }

    public static int InsertClient (Connection con, NoticeData notice) {
        String sql = "INSERT INTO Notice (subject, message, datecreated, classification, employmentId)" +
                " VALUES (?, ?, CONVERT_TZ(now(),'+00:00','+02:00') , ?, ?)";
        System.out.println("Insert a new notice into de database: " + sql);
        int n = 0;
        try{
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, notice.subject);
            statement.setString(2, notice.message);
            statement.setInt(3, notice.classification);
            statement.setInt(4, notice.employmentID);
            n = statement.executeUpdate();
            statement.close();
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println(sql + " query sends this exception: " + e);
        }

        return n;
    }

    public static Vector<NoticeData> getNotices (Connection connection, int id) {
        Vector<NoticeData> v = new Vector<NoticeData>();
        String sql = "Select Notice.*, Caregiver.firstname AS caregiverfirstname, Caregiver.lastname AS caregiverlastname," +
                " Administrator.firstname AS administratorfirstname, Administrator.lastname AS administratorlastname " +
                "FROM EmploymentRecord, Caregiver, Notice, Administrator WHERE Notice.employmentID = EmploymentRecord.employmentID " +
                "AND EmploymentRecord.clientID = ? AND EmploymentRecord.caregiverID = Caregiver.caregiverID AND Notice.administratorID = " +
                "Administrator.administratorID";
        System.out.println("See all the notices the client has made: " + sql);
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1,id);
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                NoticeData notice = new NoticeData(
                        result.getInt("noticeID"),
                        result.getString("subject"),
                        result.getString("message"),
                        result.getDate("datecreated"),
                        result.getString("reply"),
                        result.getInt("administratorID"),
                        result.getDate("datereplied"),
                        result.getBoolean("replyhasbeenread"),
                        result.getInt("classification"),
                        result.getInt("employmentID"),
                        result.getString("caregiverfirstname"),
                        result.getString("caregiverlastname"),
                        result.getString("administratorfirstname"),
                        result.getString("administratorlastname")
                );
                v.addElement(notice);
            }
            result.close();
            statement.close();
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("You got this exception: " + e + " when executing " + sql);
        }

        return v;
    }
}


