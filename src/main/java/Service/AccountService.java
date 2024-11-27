package Service;


import DAO.AccountDAO;
import Model.Account;
import SocialMediaExceptions.*;


public class AccountService {
    private AccountDAO accountDAO;

    //constructor 
    public AccountService() {
        accountDAO = new AccountDAO();
    }

    //AccountService method for creating an account
    //validateuser returns true if username already exists
    
    public Account createAccount(String username, String password) throws InvalidInputException {
        if(username.length() == 0 || password.length() <4 || accountDAO.validateUser(username)) {
            throw new InvalidInputException("Try Again!");
        } else{
            return accountDAO.createAccount(username, password);
        }
    }
    

    //AccountService method for logging into an account

    public Account loginAccount(String username, String password) throws InvalidInputException{
        Account account = accountDAO.loginAccount(username,password);

        if(account != null) {
            return account;
        } else {
            throw new InvalidInputException("Username and Password not found");
        }
    }


}
