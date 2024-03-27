package org.IntegratedHealthFinance.systems.services;

public interface AdminService {
    long getTotalTransactionCount();
    long getTotalDepositCount();
    long getTotalWithdrawalCount();
    long getInternalTransferCount();
    int getTotalMemberCount();
    int getApprovedMemberCount();
    int getPendingMemberCount();
}
