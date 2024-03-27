package org.IntegratedHealthFinance.systems.services;

import java.util.List;

import org.IntegratedHealthFinance.systems.models.Account;
import org.IntegratedHealthFinance.systems.models.Members;
// import models.org.IntegratedHealthFinance.systems.Members;
import org.IntegratedHealthFinance.systems.models.Transactions;

public interface TransactionService {
    // Perform a deposit transaction
    void deposit(Account account, double amount, String transactionDate);

    // Perform a withdrawal transaction
    void withdraw(Account account, double amount, String transactionDate);

    // Get transaction by transaction ID
    Transactions getTransactionById(int transactionId);

    // Get all transactions for an account
    List<Transactions> getTransactionsForAccount(Account account);

    // Get the count of deposits for a specific user
    int getDepositCountForUser(Members member);

    // Get the count of deposits for a specific user
    int getWithdrawCountForUser(Members member);

    void internalTransfer(Account senderAccount, Account recipientAccount, double amount, String transactionDate);

    int getInternalTransferCountForUser(Members member);

    // List<Transactions> getTransactionsOverTime(String filter);
}
