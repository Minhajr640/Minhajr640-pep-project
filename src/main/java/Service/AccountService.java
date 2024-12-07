package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    /** Declaring Fields.*/
    private AccountDAO accountDAO;

    /**Constructor that instantiates variable.*/ 
    public AccountService() {
        accountDAO = new AccountDAO();
    }

    /**
     * AccountService method to process user registration request.
     * validateUser() is an accountDAO method to check if username exists and returns true if it does
     * and false otherwise.
     * @param username
     * @param password
     * @return a newly created account with account_id if successful.
    */
    public Account createAccount(String username, String password) {
        if(username.length() == 0 || password.length() <4 || accountDAO.validateUser(username)) {
            return null;
        } else {
            return accountDAO.createAccount(username, password);
        }
    }
    
    /**
     * AccountService method to process user login request.
     * @param username.
     * @param password.
     * @return an existing account object if username and password match is found.
    */
    public Account loginAccount(String username, String password) {
        Account account = accountDAO.loginAccount(username,password);
        if(account != null) {
            return account;
        } else {
            return null;
        }
    }
}
