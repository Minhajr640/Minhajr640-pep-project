package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Service.MessageService;
import Service.AccountService;
import Model.Message;
import Model.Account;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
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


    /**Defining Endpoints. */
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
     * This is an handler for processing a post request to "/register".
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postRegisterHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account postedAccount;
            postedAccount = accountService.createAccount(account.getUsername(), account.getPassword());
            if (postedAccount != null) {
                context.json(postedAccount);
                context.status(200);
            } else {
            context.status(400);
            }
    }

    /**
     * This is an handler for processing a post request to "/login".
     */
    private void postLoginHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account authenticatedAcc;
        authenticatedAcc = accountService.loginAccount(account.getUsername(), account.getPassword());
        if(authenticatedAcc != null) {
            context.json(authenticatedAcc);
            context.status(200);
        } else {
            context.status(401);   
        }
    }

    /**
     * This is an handler for processing a post request to "/messages".
     * @param context
     * @throws JsonProcessingException
     */
    private void postMessagesHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message postedMessage = messageService.postMessage(message);
            if(postedMessage != null) {
                context.json(mapper.writeValueAsString(postedMessage));
                //context.status(400);
            } else {
            context.status(400);
            }
    }

    /**
     * This is an handler for processing a get request to "/messages".
     * @param context
     */
    private void getAllMessagesHandler(Context context) {
        List<Message> messages = messageService.getAllMessages();
        context.json(messages);
    }

    /**
     * This is an handler to process a get request to "/messages/{message_id}".
     * @param context
     * @throws JsonProcessingException
     */
    private void getMessagesByIdHandler(Context context) throws JsonProcessingException{
        int messageidd = Integer.parseInt(context.pathParam("message_id"));
        Message foundMessage;
        foundMessage = messageService.getMessageById(messageidd);
        if(foundMessage != null) {
            context.json(foundMessage);
            context.status(200);
        } else {
            context.status(200);
        }
    }

    /**
     * This is an handler to process a delete request to "/messages/{message_id}".
     * @param context
     * @throws JsonProcessingException
     */
    private void deleteMessagesByIdHandler(Context context) throws JsonProcessingException {
        int messageidd = Integer.parseInt(context.pathParam("message_id"));
        Message messageDeletion = messageService.deleteMessageByID(messageidd);
        if(messageDeletion != null) {
            context.json(messageDeletion);
            context.status(200);
        } else{
            context.status(200);
        }
    }

    /**
     * This is an handler to process a patch request to "/messages/{message_id}".
     * @param context
     * @throws JsonProcessingException
     */
    private void patchMessagesByIdHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(),Message.class);
        int messageidd = Integer.parseInt(context.pathParam("message_id"));
        Message messageUpdate = messageService.updateMessageById(messageidd, message.getMessage_text());
        if(messageUpdate == null) {
            context.status(400);
        } else {
            context.json(messageUpdate);
            context.status(200);
    }
    }

    /**
     * This is an handler to process a get request to "/accounts/{account_id}/messages". 
     * @param context
     */
    private void getMessagesByUserHandler(Context context) {
        context.json(messageService.getMessagesByAccount((Integer.valueOf(context.pathParam("account_id")))));
    }

}