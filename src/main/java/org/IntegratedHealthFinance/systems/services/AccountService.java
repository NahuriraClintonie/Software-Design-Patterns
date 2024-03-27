package org.IntegratedHealthFinance.systems.services;

import org.IntegratedHealthFinance.systems.models.Account;
import org.IntegratedHealthFinance.systems.models.Members;

public interface AccountService {
    void createAccountForMember(Members member);
    Account getAccountByAccountId(int accountId);
    Members getMemberByEmail(String email);
    double getAccountBalance(int accountId);
}
