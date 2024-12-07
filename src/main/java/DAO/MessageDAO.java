package DAO;
import Util.ConnectionUtil;
import Model.Message;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    /** 
     * DAO layer method to post a new message.
     * @param Message object without message_id.
     * @return new message object with message_id generated by database if successful.
    */
    public Message postMessage(Message message) {
        String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES (?,?,?)";
        Connection connection = ConnectionUtil.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());

            ps.executeUpdate();
            ResultSet postedMessage = ps.getGeneratedKeys();
            if(postedMessage.next()) {
                int generated_message_id = (int) postedMessage.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(),
                message.getMessage_text(), message.getTime_posted_epoch());
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /** 
     * DAO layer method to get all messages.
     * @return list of all messages in database sorted in descending order if successful.
    */
    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "Select * From message ORDER BY time_posted_epoch DESC";
        List <Message> messages = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(sql); 

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Message message = new Message(rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }

    /**
     * DAO layer method to get a message by ID.
     * @param message_id.
     * @return message object with provided message_id if it exists.
     */
    public Message getMessageById(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        Message foundMessage = null;
        String sql = "Select * FROM message WHERE message_id = ? ";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, message_id);
            
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                foundMessage = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return foundMessage;
    }

    /**
     * DAO layer method to delete a message by ID.
     * @param message_id.
     * @return deleted message object if the message_id was found.
    */    
    public Message deleteMessagebyId(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        Message message = getMessageById(message_id);
        if(message == null) {
            return null;
        }
        String sql = "DELETE FROM message WHERE message_id = ? ";
        try { 
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, message.getMessage_id());
        
        int ra = ps.executeUpdate();
        if(ra <= 0) {
            return null;
        }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return message;
    }

    /**
     * DAO layer method to update a message when provided message ID and new message text.
     * @param message_id
     * @param message_text
     * @return a message object with provided message_id and updated text if successful.
    */
    public Message updateMessageById(int message_id, String message_text) {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "UPDATE message SET message_text = ? WHERE message_id = ? ";
        Message message2 = null;
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, message_text);
            ps.setInt(2, message_id);
            
            int ra = ps.executeUpdate();
            if(ra>0) {
                message2 = getMessageById(message_id);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return message2;
    }

    /**
     * DAO layer method to retrive all messages by account ID.
     * @param account_id.
     * @return list of messages from provided account_id if account_id exists.
    */
    public List<Message> getMessagesByAccount(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM message WHERE posted_by = ? ";
        List<Message> usersMessages = new ArrayList<>(); 
    
        try { PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, account_id);

        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            Message message = new Message(rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch"));
            usersMessages.add(message);
        }
        } catch(SQLException e) {
            e.getMessage();
        }
        return usersMessages;
    }

    /**
     * DAO layer method to validate if an message ID exists.
     * @param message_id.
     * @return true if message id exists and false if it's not found.
     */
    public Boolean validateMessageID(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM message WHERE message_id = ?";

        try {PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1,message_id);

        ResultSet rs = ps.executeQuery();
        if(!rs.next()) {
            return false;
        }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }
}
