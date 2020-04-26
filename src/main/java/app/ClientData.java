package app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientData {

    int clientID;
    String firstname;
    String lastname;
    int gender;
    String email;
    String password;

    public ClientData(String firstname, String lastname, int gender, String email, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
        this.email = email;
        this.password = password;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static int RegisterClient (Connection con, ClientData client) throws Exception {
        String sql = "INSERT INTO Client (firstname, lastname, email, password, gender)" +
                " VALUES (?, ?, ?, ?, ?)";
        System.out.println("Insert a new client into the database: " + sql);
        int n = 0;
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, client.firstname);
            statement.setString(2, client.lastname);
            statement.setString(3, client.email);
            statement.setString(4, client.password);
            statement.setInt(5, client.gender);
            n = statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error in insertOrderDetail: " + sql + " Exception: " + e);

            //e.printStackTrace();
            throw new Exception("There is an error");
        }
        return n;
    }

    public static ClientData getInfo (Connection con, int id){
        String sql = "Select firstname, lastname, email, gender FROM Client WHERE clientID = ?";
        System.out.println("Conseguir información: " + sql);
        ClientData client = null;
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            if(result.next()){
                client = new ClientData(
                        result.getString("firstname"),
                        result.getString("lastname"),
                        result.getInt("gender"),
                        result.getString("email"),
                        null);
            }
            result.close();
            statement.close();
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error en getInfo con la excepción: " + e);
        }

        return client;
    }

}
