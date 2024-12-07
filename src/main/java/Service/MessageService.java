package Service;

import DAO.MessageDAO;
import DAO.AccountDAO;
import Model.Message;
import java.util.List;

public class MessageService {

    /** Declaring Fields */
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    /**Constructor that instantiates fields.*/
    public MessageService() {
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }

    /**
     * Service layer method for processing post a new message request
     * validateUserbyId() is an accountDAO method that returns true if username exists and false if it doesn't.
     * @param message object.
     * @return a new message object using DAO layer method if successful.
    */
    public Message postMessage(Message message) {
        if(accountDAO.validateUserbyId(message.getPosted_by()) 
        && !message.getMessage_text().isBlank() 
        && message.getMessage_text().length() < 255) {
                return messageDAO.postMessage(message);
            }
        return null;
    }

    /**
     * Service layer method to process get all messages request.
     * @return list of all messages in database using DAO layer method if successful.
     */
    public List<Message> getAllMessages() {
        List<Message> allMessages = messageDAO.getAllMessages();
        return allMessages;
    }

    /**
     * Service layer method to retrieve a message by message id.
     * validateMessageId() is a messageDAO method that returns true if message id exists and false if it doesn't.
     * @param message_id.
     * @return a message object using DAO layer method if successful.
    */
    public Message getMessageById(int message_id) {
        Message message = null;
        if(messageDAO.validateMessageID(message_id)) {
            message = messageDAO.getMessageById(message_id);
        }
        return message;
    }
    
    /**
     * Service layer method to process deleting a message by id request.
     * validateMessageId() is a messageDAO method that returns true if message id exists and false if it doesn't.
     * @param message_id
     * @return a message object that was deleted from the database using DAO layer method if successful.
    */
    public Message deleteMessageByID(int message_id) {
        if(!messageDAO.validateMessageID(message_id)) {
            return null;
        }
        return messageDAO.deleteMessagebyId(message_id);
    }

    /** 
     * Service layer method to process updating a message given id and new message text.
     * validateMessageId() is a messageDAO method that returns true if message id exists and false if it doesn't.
     * @param message_id.
     * @param message_text - new message text for message.
     * @return a existing message object with new message_text using DAO layer method if successful.
    */
    public Message updateMessageById(int message_id, String message_text) {
        if(message_text.length() > 0 && message_text.length() <=255 && messageDAO.validateMessageID(message_id)) {
            messageDAO.updateMessageById(message_id, message_text);
            return messageDAO.getMessageById(message_id);
        } else {
            return null;
        }
    }

    /**
     * Service layer method to process getting all messages of an account request.
     * @param accont_id.
     * @return a list of messages by account_id using DAO layer method if successful.
    */
    public List<Message> getMessagesByAccount(int account_id) {
            return messageDAO.getMessagesByAccount(account_id);
    }
}
