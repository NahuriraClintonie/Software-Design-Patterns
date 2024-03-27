package org.IntegratedHealthFinance.systems.views.transaction;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.IntegratedHealthFinance.Dao.SaccoDao;
import org.IntegratedHealthFinance.systems.models.Account;
import org.IntegratedHealthFinance.systems.models.Members;
import org.IntegratedHealthFinance.systems.models.Transactions;
import org.IntegratedHealthFinance.systems.services.AccountService;
import org.IntegratedHealthFinance.systems.services.AccountServiceImp;
import org.IntegratedHealthFinance.systems.services.TransactionService;
import org.IntegratedHealthFinance.systems.services.TransactionServiceImp;

@ManagedBean(name = "myTransactions")
@SessionScoped 
public class MyTransactions {
    private AccountService accountService;
    private TransactionService transactionService;
    private List<Transactions> sortedTransactions;

    public MyTransactions() {
        // Initialize the SaccoDao
        SaccoDao saccoDao = new SaccoDao();

        // Create the AccountService implementation with the SaccoDao instance
        accountService = new AccountServiceImp(saccoDao);

        // Create the TransactionService implementation with the SaccoDao instance
        transactionService = new TransactionServiceImp(saccoDao);
    }

    public List<Transactions> getTransactions() {
        // Clear the sortedTransactions list before fetching new transactions
        sortedTransactions = null;

        // Get the currently logged in user
        Members loggedInUser = (Members) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                .get("loggedInUser");

        if (loggedInUser != null) {
            // Get the account of the logged in user
            Account account = loggedInUser.getAccount();

            if (account != null) {
                // Get the transactions of the account
                List<Transactions> transactions = transactionService.getTransactionsForAccount(account);

                // Sort the transactions in descending order based on the transactionId
                Collections.sort(transactions, new Comparator<Transactions>() {
                    @Override
                    public int compare(Transactions t1, Transactions t2) {
                        // Sort in descending order by comparing the transactionId
                        Integer id1 = t1.getTransactionId();
                        Integer id2 = t2.getTransactionId();
                        return id2.compareTo(id1);
                    }
                });

                sortedTransactions = transactions;
            }
        }

        return sortedTransactions;
    }

    public double getAccountBalance() {
        // Get the currently logged in user
        Members loggedInUser = (Members) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("loggedInUser");

        if (loggedInUser != null) {
            // Get the account of the logged in user
            Account account = loggedInUser.getAccount();

            if (account != null) {
                // Get the account balance from the database using the AccountService
                return accountService.getAccountBalance(account.getAccountId());
            }
        }
        // Return 0 if the account or user is not found
        return 0.0;
    }

    public int getNumberOfTransactions() {
        Members loggedInUser = (Members) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                .get("loggedInUser");
        if (loggedInUser != null) {
            SaccoDao saccoDao = new SaccoDao();
            return saccoDao.getDepositCountForUser(loggedInUser) + saccoDao.getWithdrawCountForUser(loggedInUser)
                    + saccoDao.getInternalTransferCountForUser(loggedInUser);
        } else {
            return 0;
        }
    }
}
