package org.IntegratedHealthFinance.systems.services;

import org.IntegratedHealthFinance.Dao.SaccoDao;
import org.IntegratedHealthFinance.systems.models.Account;
import org.IntegratedHealthFinance.systems.models.Members;

public class AccountServiceImp implements AccountService {
    private SaccoDao saccoDao;

    public AccountServiceImp(SaccoDao saccoDao) {
        this.saccoDao = saccoDao;
    }

    @Override
    public void createAccountForMember(Members member) {
        Account account = new Account();
        // Set any initial values for the account, such as account number, balance, etc.
        account.setBalance(0.0);
        member.setAccount(account);
        saccoDao.saveAccount(account);
    }

    @Override
    public Account getAccountByAccountId(int accountId) {
        return saccoDao.getAccountByAccountId(accountId);
    }

    @Override
    public Members getMemberByEmail(String email) {
        return saccoDao.getMemberByEmail(email);
    }

    @Override
    public double getAccountBalance(int accountId) {
        // Fetch the account from the database using the accountId
        Account account = saccoDao.getAccountByAccountId(accountId);

        if (account != null) {
            // Return the account balance
            return account.getBalance();
        } else {
            // Return 0 if the account is not found
            return 0.0;
        }
    }
}
