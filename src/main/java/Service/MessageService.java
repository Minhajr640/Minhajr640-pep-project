package Service;
import DAO.MessageDAO;
import DAO.AccountDAO;
import Model.Message;
import SocialMediaExceptions.InvalidInputException;
import SocialMediaExceptions.UserNotFoundException;
import SocialMediaExceptions.MessageIdNotFoundException;
import java.util.List;






public class MessageService {
    private MessageDAO messageDAO;

    private AccountDAO accountDAO;
    //constructor
    public MessageService() {
        messageDAO = new MessageDAO();
    }

    //Method to validate message_text length

    private Boolean validateMessageTextLength(String message_text) {
        int message_length = message_text.length();
        if(message_length > 0 && message_length <= 255) {
            return true;
        }
        else return false;
    }

    //Service Method for posting message.
    //validate user method checks if username exists.

    public Message postMessage(Message message) throws InvalidInputException, UserNotFoundException {
        // int messageLength = message.getMessage_text().length();
        if(accountDAO.validateUser(message.getPosted_by())) {
            if(validateMessageTextLength(message.getMessage_text())) {
                return messageDAO.postMessage(message);
            }
            else{
                throw new InvalidInputException("Message maximum limit:255 characters.");
            }
        } else {
            throw new UserNotFoundException("Must have an account to post a message.");
        }
    }

    public List<Message> getAllMessages() {
        List<Message> allMessages = messageDAO.getAllMessages();
        return allMessages;
    }

    //service method to retrieve message by id.
    //if statement validates message_id exists and returns true if it does.
    public Message getMessageById(int message_id) throws MessageIdNotFoundException{
        Message message;
        if(messageDAO.validateMessageID(message_id)) {
            message = messageDAO.getMessageById(message_id);
            return message;
        } 
        else {
            throw new MessageIdNotFoundException("MessageId not found");
        }
        
    }
    
    //service method to delete message by id.
    //validateMessageId returns true if message_id exists.
    public Message deleteMessageByID(int message_id) throws MessageIdNotFoundException {
        if(messageDAO.validateMessageID(message_id)) {
            return messageDAO.deleteMessagebyId(message_id);
        } else {
            throw new MessageIdNotFoundException("Message ID not Found");
        }
    }

    //service method to update message by id.
    //validate messageTextLength checks if text length is in bounds.
    public Message updateMessageById(int message_id, String message_text) throws InvalidInputException {
        if(validateMessageTextLength(message_text)) {
            return messageDAO.updateMessageById(message_id, message_text);
        } else {
            throw new InvalidInputException("Message maximum limit: 255 characters.");
        }
    }

    public List<Message> getMessagesByAccount(int account_id) throws UserNotFoundException {
        List<Message> allMessagesOfAccount;
        if(accountDAO.validateUser(account_id)) {
            allMessagesOfAccount = messageDAO.getMessagesByAccount(account_id);
            return allMessagesOfAccount;
        } else {
            throw new UserNotFoundException("Account Id does not exist.");
        }
    }

}
