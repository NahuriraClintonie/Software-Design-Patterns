package org.IntegratedHealthFinance.systems.services;

import java.io.IOException;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

import org.IntegratedHealthFinance.Dao.SaccoDao;
import org.IntegratedHealthFinance.systems.controllers.Hyperlinks;
import org.IntegratedHealthFinance.systems.enumerations.TransactionType;
import org.IntegratedHealthFinance.systems.models.Account;
import org.IntegratedHealthFinance.systems.models.Members;
import org.IntegratedHealthFinance.systems.models.Transactions;

public class TransactionServiceImp implements TransactionService {
    private SaccoDao saccoDao;
    public static final double minimumDepositAmount = 10000;
    public static final double minimumWithdrawAmount = 10000;
    public static final double minimumTransferAmount = 10000;

    public TransactionServiceImp(SaccoDao saccoDao) {
        this.saccoDao = saccoDao;
    }

    @Override
    public void deposit(Account account, double amount, String transactionDate) {
    if (amount < minimumDepositAmount) {
        FacesContext.getCurrentInstance().addMessage("messages",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Deposit Amount",
                            "Minimum deposit amount is " + minimumDepositAmount));
        
            return; // Transaction does not proceed
    }

        // Create a new transaction
        Transactions transaction = new Transactions();
        transaction.setTransactionType(TransactionType.Deposit);
        transaction.setTransactionAmount(amount);
        transaction.setTransactionDate(transactionDate);
        transaction.setAccount(account);

        // Update the account balance
        double newBalance = account.getBalance() + amount;
        account.setBalance(newBalance);

        // Save the transaction and update the account in the database
        saccoDao.saveTransaction(transaction);
        saccoDao.saveAccount(account);

        addFlashMessage(FacesMessage.SEVERITY_INFO, "Success", "Deposit made successfully");

                String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect(path + Hyperlinks.dashboard);
                } catch (IOException e) {
                    e.printStackTrace();
                }
    }

    private void addFlashMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Flash flash = facesContext.getExternalContext().getFlash();
        flash.setKeepMessages(true);
        facesContext.addMessage(null, new FacesMessage(severity, summary, detail));
    }

    @Override
    public void withdraw(Account account, double amount, String transactionDate) {
        // Check if the amount is greater than or equal to the minimum withdraw amount
        // (10000)
        if (amount < minimumWithdrawAmount) {
            FacesContext.getCurrentInstance().addMessage("messages",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Withdrawal Amount",
                            "Minimum withdraw amount is " + minimumWithdrawAmount));
            return; // Transaction does not proceed
        }
        // Create a new transaction
        Transactions transaction = new Transactions();
        transaction.setTransactionType(TransactionType.Withdrawal);
        transaction.setTransactionAmount(amount);
        transaction.setTransactionDate(transactionDate);
        transaction.setAccount(account);

        // Check if the account has sufficient balance for the withdrawal
        if (account.getBalance() >= amount) {
            // Update the account balance
            double newBalance = account.getBalance() - amount;
            account.setBalance(newBalance);

            // Save the transaction and update the account in the database
            saccoDao.saveTransaction(transaction);
            saccoDao.saveAccount(account);

            addFlashMessage(FacesMessage.SEVERITY_INFO, "Success", "Withdrawal made successfully");

            String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(path + Hyperlinks.dashboard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Insufficient balance, handle the situation as needed
            FacesContext.getCurrentInstance().addMessage("messages",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Insufficient Funds",
                            "You do not have enough balance to make this transfer."));
        }
    }

    
    @Override
    public Transactions getTransactionById(int transactionId) {
        return saccoDao.getTransactionById(transactionId);
    }

    @Override
    public List<Transactions> getTransactionsForAccount(Account account) {
        return saccoDao.getTransactionsForAccount(account);
    }

    @Override
    public int getDepositCountForUser(Members member) {
        SaccoDao saccoDao = new SaccoDao();
        return saccoDao.getDepositCountForUser(member);
    }

    @Override
    public int getWithdrawCountForUser(Members member) {
        SaccoDao saccoDao = new SaccoDao();
        return saccoDao.getWithdrawCountForUser(member);
    }

    @Override
    public int getInternalTransferCountForUser(Members member) {
        SaccoDao saccoDao = new SaccoDao();
        return saccoDao.getInternalTransferCountForUser(member);
    }

    @Override
    public void internalTransfer(Account senderAccount, Account recipientAccount, double amount,
            String transactionDate) {
        // Check if the sender's account has sufficient balance for the transfer

        // Check if the amount is greater than or equal to the minimum transfer amount
        // (10000)
        if (amount < minimumTransferAmount) {
            FacesContext.getCurrentInstance().addMessage("messages",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Transfer Amount",
                            "Minimum transfer amount is " + minimumTransferAmount));
            return; // Transaction does not proceed
        }

        if (senderAccount.getBalance() >= amount) {
            // Deduct the amount from the sender's account balance
            double newSenderBalance = senderAccount.getBalance() - amount;
            senderAccount.setBalance(newSenderBalance);

            // Add the amount to the recipient's account balance
            double newRecipientBalance = recipientAccount.getBalance() + amount;
            recipientAccount.setBalance(newRecipientBalance);

            // Create a new transaction for the sender
            Transactions senderTransaction = new Transactions();
            senderTransaction.setTransactionType(TransactionType.Internal_Transfer);
            senderTransaction.setTransactionAmount(-amount);
            senderTransaction.setTransactionDate(transactionDate);
            senderTransaction.setAccount(senderAccount);

            // Create a new transaction for the recipient
            Transactions recipientTransaction = new Transactions();
            recipientTransaction.setTransactionType(TransactionType.Internal_Transfer);
            recipientTransaction.setTransactionAmount(amount);
            recipientTransaction.setTransactionDate(transactionDate);
            recipientTransaction.setAccount(recipientAccount);

            // Save the transactions and update the accounts in the database
            saccoDao.saveTransaction(senderTransaction);
            saccoDao.saveTransaction(recipientTransaction);
            saccoDao.saveAccount(senderAccount);
            saccoDao.saveAccount(recipientAccount);

            addFlashMessage(FacesMessage.SEVERITY_INFO, "Success", "Internal transfer successful");
            String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(path + Hyperlinks.dashboard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            FacesContext.getCurrentInstance().addMessage("messages",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Insufficient Funds",
                            "You do not have enough balance to make this transfer."));

        }
    }
}
