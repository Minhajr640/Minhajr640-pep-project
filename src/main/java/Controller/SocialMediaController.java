package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Service.MessageService;
import SocialMediaExceptions.InvalidInputException;
import SocialMediaExceptions.MessageIdNotFoundException;
import SocialMediaExceptions.UserNotFoundException;
import Service.AccountService;
import Model.Message;
import Model.Account;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    
    MessageService messageService;
    AccountService accountService;

    public SocialMediaController() {
        this.messageService = new MessageService();
        this.accountService = new AccountService();
    }


     //Defining Endpoints
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postRegisterHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postMessagesHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessagesByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessagesByIdHandler);
        app.patch("/messages/{message_id}", this::patchMessagesByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByUserHandler);
        //app.start(8080);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postRegisterHandler(Context context) throws JsonProcessingException, InvalidInputException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account postedAccount;
        try {
            postedAccount = accountService.createAccount(account.getUsername(), account.getPassword());
            if (postedAccount != null) {
                context.json(postedAccount);
                context.status(200);
            } 
        } catch(InvalidInputException e) {
            context.status(400);
        }
    }

    private void postLoginHandler(Context context) throws JsonProcessingException, InvalidInputException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account authenticatedAcc;
        try {
            authenticatedAcc = accountService.loginAccount(account.getUsername(), account.getPassword());
            if(authenticatedAcc != null) {
                context.json(authenticatedAcc);
                context.status(200);
            }
        } catch (InvalidInputException e) {
            context.status(401);
            e.getMessage();
        }
    }

    private void postMessagesHandler(Context context) throws JsonProcessingException, InvalidInputException, UserNotFoundException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message postedmessage;
        try { 
            postedmessage = messageService.postMessage(message);
            if(postedmessage != null) {
                context.json(mapper.writeValueAsString(postedmessage));
                context.status(200);
            }   
        } catch (InvalidInputException | UserNotFoundException e) {
            context.result(e.getMessage());
            context.status(400);
        }
    }

    private void getAllMessagesHandler(Context context) {
        List<Message> messages = messageService.getAllMessages();
        context.json(messages);
    }

    private void getMessagesByIdHandler(Context context) throws MessageIdNotFoundException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message foundMessage;

        try {
            foundMessage = messageService.getMessageById(message.getMessage_id());
            if(foundMessage != null) {
                context.json(foundMessage);
                context.status(200);
            }
        } 
        catch(MessageIdNotFoundException e) {
             e.getMessage();
        }

    }

    private void deleteMessagesByIdHandler(Context context) {
        context.json(" ");
    }

    private void patchMessagesByIdHandler(Context context) {
        context.json(".  ");
    }

    private void getMessagesByUserHandler(Context context) {
        context.json(".  ");
    }

}