package app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

public class EmploymentRecordData {
    int employmentID;
    int clientID;
    int caregiverID;
    int administratorID;
    Date startdate;
    Date enddate;
    int hoursperweek;
    int status;
    Date dateauthorized;
    String caregiverfirstname;
    String caregiverlastname;
    int caregivergender;
    double caregiverhourlyrate;

    public EmploymentRecordData(int clientID, int caregiverID, int administratorID, Date startdate, Date enddate, int hoursperweek, int status, Date dateauthorized) {
        this.clientID = clientID;
        this.caregiverID = caregiverID;
        this.administratorID = administratorID;
        this.startdate = startdate;
        this.enddate = enddate;
        this.hoursperweek = hoursperweek;
        this.status = status;
        this.dateauthorized = dateauthorized;
    }

    public EmploymentRecordData(int employmentID, int clientID, int caregiverID, int administratorID, Date startdate, Date enddate, int hoursperweek, int status, Date dateauthorized,
                                String caregiverfirstname, String caregiverlastname, int caregivergender, double caregiverhourlyrate) {
        this.employmentID = employmentID;
        this.clientID = clientID;
        this.caregiverID = caregiverID;
        this.administratorID = administratorID;
        this.startdate = startdate;
        this.enddate = enddate;
        this.hoursperweek = hoursperweek;
        this.status = status;
        this.dateauthorized = dateauthorized;
        this.caregiverfirstname = caregiverfirstname;
        this.caregiverlastname = caregiverlastname;
        this.caregivergender = caregivergender;
        this.caregiverhourlyrate = caregiverhourlyrate;
    }

    public int getEmploymentID() {
        return employmentID;
    }

    public void setEmploymentID(int employmentID) {
        this.employmentID = employmentID;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public int getCaregiverID() {
        return caregiverID;
    }

    public void setCaregiverID(int caregiverID) {
        this.caregiverID = caregiverID;
    }

    public int getAdministratorID() {
        return administratorID;
    }

    public void setAdministratorID(int administratorID) {
        this.administratorID = administratorID;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public int getHoursperweek() {
        return hoursperweek;
    }

    public void setHoursperweek(int hoursperweek) {
        this.hoursperweek = hoursperweek;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getDateauthorized() {
        return dateauthorized;
    }

    public void setDateauthorized(Date dateauthorized) {
        this.dateauthorized = dateauthorized;
    }

    public String getCaregiverfirstname() {
        return caregiverfirstname;
    }

    public void setCaregiverfirstname(String caregiverfirstname) {
        this.caregiverfirstname = caregiverfirstname;
    }

    public String getCaregiverlastname() {
        return caregiverlastname;
    }

    public void setCaregiverlastname(String caregiverlastname) {
        this.caregiverlastname = caregiverlastname;
    }

    public int getCaregivergender() {
        return caregivergender;
    }

    public void setCaregivergender(int caregivergender) {
        this.caregivergender = caregivergender;
    }

    public double getCaregiverhourlyrate() {
        return caregiverhourlyrate;
    }

    public void setCaregiverhourlyrate(double caregiverhourlyrate) {
        this.caregiverhourlyrate = caregiverhourlyrate;
    }

    public static int[] countEmployments (Connection con, int id) {
        int hours = 0;
        int employments = 0;
        String sql = "Select sum(hoursperweek) hours, count(hoursperweek) employments FROM EmploymentRecord WHERE clientID = ?";;
        System.out.println("Count the number of employment offers: " + sql);
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            if(result.next()){
                employments = result.getInt("employments");
                hours = result.getInt("hours");
                System.out.println("Se imprime employments: " + employments);
                System.out.println("Hours: " + hours);
            }
            result.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int[] resp = new int[2];
        resp[0] = employments;
        resp[1] =hours;

        return resp;
    }

    public static Vector<EmploymentRecordData> getEmployments (Connection connection, int id) {
        Vector<EmploymentRecordData> v = new Vector<EmploymentRecordData>();
        String sql = "Select EmploymentRecord.*, Caregiver.firstname, Caregiver.lastname,\n" +
                "Caregiver.gender, Caregiver.hourlyrate FROM EmploymentRecord,\n" +
                "Caregiver WHERE clientID = ? AND EmploymentRecord.caregiverID\n" +
                " = Caregiver.caregiverID";
        System.out.println("See all the job offers the client has made: " + sql);
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1,id);
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                EmploymentRecordData offer = new EmploymentRecordData(
                        result.getInt("employmentID"),
                        result.getInt("clientID"),
                        result.getInt("caregiverID"),
                        result.getInt("administratorID"),
                        result.getDate("startdate"),
                        result.getDate("enddate"),
                        result.getInt("hoursperweek"),
                        result.getInt("status"),
                        result.getDate("dateauthorized"),
                        result.getString("firstname"),
                        result.getString("lastname"),
                        result.getInt("gender"),
                        result.getDouble("hourlyrate")
                );
                v.addElement(offer);
            }
            result.close();
            statement.close();
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("You got this exception: " + sql);
        }

        return v;
    }

    public static int terminate (Connection con, int id) {
        int n = 0;
        String sql = "Update EmploymentRecord SET status = 6, enddate = CONVERT_TZ(now(),'+00:00','+02:00')\n" +
                "WHERE employmentID = ?";
        System.out.println("Status change in the database: " + sql);
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, id);
            n = statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Exception in " + sql + " : " + e);
        }

        return n;
    }

}
