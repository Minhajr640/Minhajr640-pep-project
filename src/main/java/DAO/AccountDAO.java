package DAO;

import Util.ConnectionUtil;
import Model.Account;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AccountDAO {

    /**
    * DAO layer method to create a new account.
    * @param username
    * @param password
    * @return new Account object if successful.
    */
    public Account createAccount(String username, String password) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO account(username, password) VALUES (?,?)";
            PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            Account account = new Account();
            ps.setString(1, username);
            ps.setString(2, password);
        
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
                if(rs.next()) {
                    account.setAccount_id(rs.getInt("account_id"));
                    account.setUsername(username);
                    account.setPassword(password);

                    return account;
                }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * DAO layer method to check if username and password finds any matches in the 
     * database for loggin in purposes.
     * @param username for account.
     * @param password for account.
     * @return existing Account object if successful.
    */
    public Account loginAccount(String username, String password){
        String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
        Connection connection = ConnectionUtil.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(sql); 
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery(); 
            if(rs.next()) {
                Account account = new Account();
                account.setAccount_id(rs.getInt("account_id"));
                account.setUsername(rs.getString("username"));
                account.setPassword(rs.getString("password"));
                return account;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * DAO layer method to verify if a username exists in the database.
     * @param username 
     * @return true if username exists and false if username doesn't exist.
    */
    public Boolean validateUser(String username) {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM account WHERE username = ? ";
        try { 
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;    
    }

    /**
     * DAO layer method to verify if a account ID exists in the database. 
     * @param account_id.
     * @return true if account_id exists and false if it doesn't exist.
    */
    public boolean validateUserbyId(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM account WHERE account_id = ? ";
        try { 
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, account_id);

            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }    
        return false;
    }
}
