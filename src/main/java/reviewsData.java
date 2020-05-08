import java.util.Vector;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

public class reviewsData {
	int    employmentID;
    int    clientID;
	String firstname;
	String lastname;
	int    caregiverID;
	int    numberofstars;
    String comment;
	Date   ratedate;
	int    average;
	
	
    reviewsData (int employmentID, String firstname, String lastname, int numberofstars, String comment, Date ratedate) {
		this.employmentID = employmentID;		
		this.firstname = firstname;
		this.lastname = lastname;
		this.numberofstars = numberofstars;
		this.comment = comment;
		this.ratedate = ratedate;
    }
	
	reviewsData (int employmentID, String firstname, String lastname, int clientID, int caregiverID) {
		this.employmentID = employmentID;		
		this.firstname = firstname;
		this.lastname = lastname;
		this.clientID = clientID;
        this.caregiverID = caregiverID;

    }
	
	reviewsData (String firstname, String lastname) {
		this.firstname = firstname;
		this.lastname = lastname;
    }
	
	reviewsData (int employmentID, int numberofstars, String comment) {
		this.employmentID = employmentID;
		this.numberofstars = numberofstars;
		this.comment = comment;
    }
	
	reviewsData (int average) {
		this.average = average;
    }	
	
	public static Vector<reviewsData> getCaregiverReviews(Connection connection, int id) {
		Vector<reviewsData> vec = new Vector<reviewsData>();
        String sql = "SELECT Client.firstname, Client.lastname, EmploymentRecord.employmentID, CaregiverRating.numberofstars, CaregiverRating.comment, CaregiverRating.ratedate";
        sql += " FROM CaregiverRating, Client, EmploymentRecord";
		sql += " WHERE Client.clientID = EmploymentRecord.clientID AND EmploymentRecord.employmentID = CaregiverRating.employmentID"; 
		sql += " AND EmploymentRecord.caregiverID = ?"; 
		System.out.println("getCaregiverReviews: " + sql);
		try {
			PreparedStatement pstmt=connection.prepareStatement(sql);
			pstmt.setInt(1, id);
			ResultSet result = pstmt.executeQuery();
			while(result.next()) {
				reviewsData review = new reviewsData(
					Integer.parseInt(result.getString("employmentID")),
					result.getString("firstname"),
					result.getString("lastname"),
					Integer.parseInt(result.getString("numberofstars")),
					result.getString("comment"),
					result.getDate("ratedate")
				);
				vec.addElement(review);
			}
		} catch(SQLException e) {
			e.printStackTrace();
			System.out.println("Error in getCaregiverReviews: " + sql + " Exception: " + e);
		}
		return vec;
    }
	
	public static Vector<reviewsData> getPendingReviews(Connection connection, int id) {
		Vector<reviewsData> vec = new Vector<reviewsData>();
		String sql = "SELECT Client.firstname, Client.lastname, Client.clientID, EmploymentRecord.employmentID, EmploymentRecord.caregiverID";
        sql += " FROM Client INNER JOIN EmploymentRecord ON Client.clientID = EmploymentRecord.clientID";
		sql += " WHERE EmploymentRecord.status=6 AND EmploymentRecord.caregiverID=? AND EmploymentRecord.employmentID NOT IN (SELECT employmentID FROM ClientRating)"; 

		System.out.println("getPendingReviews: " + sql);
		try {
			PreparedStatement pstmt=connection.prepareStatement(sql);
			pstmt.setInt(1, id);
			ResultSet result = pstmt.executeQuery();
			while(result.next()) {
				reviewsData pendingReview = new reviewsData(
					Integer.parseInt(result.getString("employmentID")),
					result.getString("firstname"),
					result.getString("lastname"),
					Integer.parseInt(result.getString("clientID")),				
					Integer.parseInt(result.getString("caregiverID"))
				);
				vec.addElement(pendingReview);
			}
		} catch(SQLException e) {
			e.printStackTrace();
			System.out.println("Error in getPendingReviews: " + sql + " Exception: " + e);
		}
		return vec;
    }
	
	public static int insertReview(Connection connection, reviewsData newReview) {
        String sql ="INSERT INTO ClientRating (employmentID, numberofstars , comment)"
            + "VALUES (?,?,?)";
        System.out.println("insertReview: " + sql);
        int n = 0;
		try {
            PreparedStatement ps= connection.prepareStatement(sql);
            ps.setInt(1, newReview.employmentID);
            ps.setInt(2, newReview.numberofstars);
            ps.setString(3, newReview.comment);
            
            n = ps.executeUpdate();
            ps.close();
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Error in insertReview: " + sql + " Exception: " + e);
        }
        return n;
    }	
	
	public static reviewsData getAveragePerClient(Connection connection, int clientid) { 
		String sql = "SELECT AVG(ClientRating.numberofstars) as average";
		sql += " FROM ClientRating, EmploymentRecord WHERE ClientRating.employmentID=EmploymentRecord.employmentID AND EmploymentRecord.clientID=?";

		System.out.println("getAveragePerClient: " + sql);
		reviewsData average = null;;
		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, clientid);
			ResultSet result = pstmt.executeQuery();
			if(result.next()) {
				average = new reviewsData(

					result.getInt("average")

				);
			}
			result.close();
			pstmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
			System.out.println("Error in AveragePerClient: " + sql + " Exception: " + e);
		}
		return average;
    } 

}