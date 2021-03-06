
import java.util.Vector;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

public class adminCaregiverData {
    int    caregiverID;
    String firstname;
    String lastname;
    String email;
    String password;
    int gender;
    int available;
    float hourlyrate;
    String description;
    Date birthdate;
    int enrollmentstatus;

    adminCaregiverData (int caregiverID, String firstname, String lastname, String email, int gender,
                    int available, float hourlyrate, String description, Date birthdate, int enrollmentstatus) {
        this.caregiverID = caregiverID;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.gender = gender;
        this.available = available;
        this.hourlyrate = hourlyrate;
        this.description = description;
        this. birthdate = birthdate;
        this.enrollmentstatus = enrollmentstatus;
    }
    public Vector<adminCaregiverData> getList(Connection connection) {
        Vector<adminCaregiverData> vec = new Vector<adminCaregiverData>();
        String sql = "Select * FROM Caregiver ORDER BY caregiverID DESC";
        System.out.println("getList: " + sql);
        try {
            Statement statement=connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while(result.next()) {
                adminCaregiverData order = new adminCaregiverData(
                    Integer.parseInt(result.getString("caregiverID")),
                    result.getString("firstname"),
                    result.getString("lastname"),
                    result.getString("email"),
                    Integer.parseInt(result.getString("gender")),
                    Integer.parseInt(result.getString("available")),
                    Float.parseFloat(result.getString("hourlyrate")),
                    result.getString("description"),
                    result.getDate("birthdate"),
                    Integer.parseInt(result.getString("enrollmentstatus"))
                );
                vec.addElement(order);
            }
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Error in getList: " + sql + " Exception: " + e);
        }
        return vec;
    }

    public adminCaregiverData getCaregiver(Connection connection, int caregiverID) {
        adminCaregiverData caregiverData = null;
        String sql = "Select * FROM Caregiver WHERE caregiverID = ?";
        System.out.println("getList: " + sql);
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, caregiverID);
            ResultSet result = pstmt.executeQuery();
            if(result.next()) {
                 caregiverData = new adminCaregiverData(
                 caregiverID,
                 result.getString("firstname"),
                 result.getString("lastname"),
                 result.getString("email"),
                 Integer.parseInt(result.getString("gender")),
                 Integer.parseInt(result.getString("available")),
                 Float.parseFloat(result.getString("hourlyrate")),
                 result.getString("description"),
                 result.getDate("birthdate"),
                 Integer.parseInt(result.getString("enrollmentstatus"))
                );
            }
            result.close();
            pstmt.close();
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Error in getHeader: " + sql + " Exception: " + e);
        }
        return caregiverData;
    }

    public static int deleteCaregiver(Connection connection, int caregiverID) {
        String sql ="DELETE FROM Caregiver WHERE caregiverID = ?";
        System.out.println("update: " + sql);
        int n = 0;
        try {
            PreparedStatement stmtUpdate= connection.prepareStatement(sql);
            stmtUpdate.setInt(1, caregiverID);
            n = stmtUpdate.executeUpdate();
            stmtUpdate.close();
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Error in update: " + sql + " Exception: " + e);
        }
        return n;
    }


    /*public static adminCaregiverData getadminCaregiverData(Connection connection, int id) {
        String sql = "Select  FROM ";
        sql += " WHERE s.CustomerID = Customers.CustomerID AND ID=?";
        System.out.println("get: " + sql);
        adminCaregiverData order = null;;
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet result = pstmt.executeQuery();
            if(result.next()) {
                order = new adminCaregiverData(
                    Integer.parseInt(result.getString("ID")),
                    result.getString("CustomerId"),
                    null,
                    result.getDate("Date")
                );
            }
            result.close();
            pstmt.close();
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Error in getHeader: " + sql + " Exception: " + e);
        }
        return order;
    }
    public static int updateHeader(Connection connection, adminCaregiverData order) {
        String sql ="UPDATE s "
            + "SET CustomerId = ?,"
            + " Date = ?"
            + " WHERE Id = ?";
        System.out.println("update: " + sql);
        int n = 0;
        try {
            PreparedStatement stmtUpdate= connection.prepareStatement(sql);
            stmtUpdate.setString(1,order.customerId);
            stmtUpdate.setDate(2,order.orderDate);
            stmtUpdate.setInt(3,order.orderId);
            n = stmtUpdate.executeUpdate();
            stmtUpdate.close();
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Error in update: " + sql + " Exception: " + e);
        }
        return n;
    }
    public static Vector<DetailadminCaregiverData> getDetail(Connection connection, int id) {
        Vector<DetailadminCaregiverData> vec = new Vector<DetailadminCaregiverData>();
        String sql = "Select ID, [ Details].ProductID, ProductName, [ Details].UnitPrice as UnitPrice, Quantity, Discount FROM [ Details], Products";
        sql += " WHERE [ Details].ProductID = Products.ProductID AND ID = ?";
        System.out.println("getDetail: " + sql);
        try {
            PreparedStatement statement= connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                DetailadminCaregiverData orderDetail = new DetailadminCaregiverData(
                    Integer.parseInt(result.getString("OrderID")),
                    result.getString("ProductID"),
                    result.getString("ProductName"),
                    Float.parseFloat(result.getString("UnitPrice")),
                    Float.parseFloat(result.getString("Quantity")),
                    Float.parseFloat(result.getString("Discount"))
                );
                vec.addElement(orderDetail);
            }
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Error in getOrderDetail: " + sql + " Exception: " + e);
        }
        return vec;
    }

    public static Vector<OrderDetailadminCaregiverData> getSimpleOrderDetail(Connection connection, int id) {
        Vector<OrderDetailadminCaregiverData> vec = new Vector<OrderDetailadminCaregiverData>();
        String sql = "Select OrderID, ProductID, UnitPrice, Quantity, Discount FROM OrderDetailsOld";
        sql += " WHERE OrderID = ?";
        //System.out.println("getSimpleOrderDetail: " + sql);
        try {
            PreparedStatement statement= connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                OrderDetailadminCaregiverData orderDetail = new OrderDetailadminCaregiverData(
                    Integer.parseInt(result.getString("OrderID")),
                    result.getString("ProductID"),
                    "",
                    Float.parseFloat(result.getString("UnitPrice")),
                    Float.parseFloat(result.getString("Quantity")),
                    Float.parseFloat(result.getString("Discount"))
                );
                vec.addElement(orderDetail);
            }
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Error in getSimpleOrderDetail: " + sql + " Exception: " + e);
        }
        return vec;
    }
    public static int updateUnitPriceOrderDetail(Connection connection, OrderDetailadminCaregiverData orderDetail) {
        String sql ="UPDATE [Order Details] "
            + "SET UnitPrice = ?"
            + " WHERE OrderID = ?";
        System.out.println("updateUnitPriceOrderDetail: " + sql);
        int n = 0;
        try {
            PreparedStatement stmtUpdate= connection.prepareStatement(sql);
            stmtUpdate.setFloat(1,orderDetail.unitPrice);
            stmtUpdate.setInt(2,orderDetail.orderId);
            n = stmtUpdate.executeUpdate();
            stmtUpdate.close();
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Error in updateUnitPriceOrderDetail: " + sql + " Exception: " + e);
        }
        return n;
    }
    public static int insertOrderDetail(Connection connection, OrderDetailadminCaregiverData orderDetail) {
        String sql ="INSERT INTO [Order Details] (OrderID, ProductID, UnitPrice, Quantity, Discount) "
            + "VALUES (?, ?, ?, ?, ?)";
        System.out.println("updateProduct: " + sql);
        int n = 0;
        try {
            PreparedStatement stmtUpdate= connection.prepareStatement(sql);
            stmtUpdate.setInt(1,orderDetail.orderId);
            stmtUpdate.setString(2,orderDetail.productId);
            stmtUpdate.setFloat(3,orderDetail.unitPrice);
            stmtUpdate.setFloat(4,orderDetail.quantity);
            stmtUpdate.setFloat(5,orderDetail.discount);
            n = stmtUpdate.executeUpdate();
            stmtUpdate.close();
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Error in insertOrderDetail: " + sql + " Exception: " + e);
        }
        return n;
    }

}

class OrderDetailadminCaregiverData {
    int    orderId;
    String    productId;
    String productName;
    float    unitPrice;
    float    quantity;
    float    discount;

    OrderDetailadminCaregiverData (int orderId, String productId, String productName, float unitPrice, float quantity, float discount) {
        this.orderId    = orderId;
        this.productId   = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.discount = discount;
    }*/
}
