package DAO;

import Util.ConnectionUtil;
import Model.Account;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class AccountDAO {


    //Creating an account 
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

    //Authenticating user by username and password 
    public Account loginAccount(String username, String password){
        String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
        Connection connection = ConnectionUtil.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(sql); 
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet returnedAccount = ps.executeQuery(); 
            if(returnedAccount.next()) {
                Account account = new Account();
                account.setAccount_id(returnedAccount.getInt("account_id"));
                account.setUsername(returnedAccount.getString("username"));
                account.setPassword(returnedAccount.getString("password"));
                return account;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    //Checking if user exists using username returns true if account exists false if no users are returned from query.
    
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
            else {
                return false;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;    
    }

    //Checking if user exists using account_id. Returns true if user exists and null if no users are returned from query.

    public Boolean validateUser(int account_id) {
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
        return null;
    }
    

}
