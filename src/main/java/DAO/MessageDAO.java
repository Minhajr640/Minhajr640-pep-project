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
    //should the param be message_text?
    //posting a message
    public Message postMessage(Message message) {
        String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES (?,?,?)";
        Connection connection = ConnectionUtil.getConnection();
        
        try {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, message.getPosted_by());
            ps.setString(1, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());
            ResultSet postedMessage = ps.getGeneratedKeys();

            if(postedMessage.next()) {
                int generated_message_id = postedMessage.getInt(1);
                int posted_by = postedMessage.getInt(2);
                String message_text = postedMessage.getString(3);
                long time_posted_epoch = postedMessage.getLong(4);

                return new Message(generated_message_id, posted_by, message_text, time_posted_epoch);
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    //returning all messages
    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "Select * From message ORDER BY time_posted_epoch DESC";
        List <Message> messages = new ArrayList<>();

        try {PreparedStatement ps = connection.prepareStatement(sql); 
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

    //returning a message by id.
    public Message getMessageById(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "Select * FROM message WHERE message_id = ? ";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, message_id);
            
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                Message message = new Message();
                message.setMessage_id(rs.getInt("message_id"));
                message.setPosted_by(rs.getInt("posted_by"));
                message.setMessage_text(rs.getString("message_text"));
                message.setTime_posted_epoch(rs.getLong("time_posted_epoch"));
                
                return message;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    //delete a message by id 
    public Message deleteMessagebyId(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        Message message = getMessageById(message_id);
        String sql = "DELETE FROM message WHERE message_id = ? ";

        
        try { PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, message_id);
        

        int rowsAffected = ps.executeUpdate();
        if(rowsAffected > 0) {
            return message;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    //updating message by id
    public Message updateMessageById(int message_id, String updatedMessage) {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "UPDATE message SET message_text = ? WHERE message_id = ? ";
        Message message = null;
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, updatedMessage);
            ps.setInt(2, message_id);
            
            int result = ps.executeUpdate();

            if(result > 0) {
                message = getMessageById(message_id);
                return message;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    //returning all messages by account_id
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

    //method to validate that messageId exists.
    public Boolean validateMessageID(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM message WHERE message_id = ?";

        try {PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1,message_id);

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
