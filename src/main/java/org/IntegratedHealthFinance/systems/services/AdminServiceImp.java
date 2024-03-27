package org.IntegratedHealthFinance.systems.services;

import org.IntegratedHealthFinance.Dao.SaccoDao;

public class AdminServiceImp implements AdminService {
    private SaccoDao saccoDao;

    public AdminServiceImp(SaccoDao saccoDao){
        this.saccoDao = saccoDao;
    }

    @Override
    public long getTotalTransactionCount() {
        return saccoDao.getTotalTransactionCount();
    }

    @Override
    public long getTotalDepositCount(){
        return saccoDao.getTotalDepositCount();
    }

    @Override
    public long getTotalWithdrawalCount() {
        return saccoDao.getTotalWithdrawalCount();
    }

    @Override
    public long getInternalTransferCount() {
        return saccoDao.getInternalTransferCount();
    }

    @Override
    public int getTotalMemberCount() {
        return saccoDao.getTotalMemberCount();
    }

    @Override
    public int getApprovedMemberCount() {
        return saccoDao.approvedMemberCount();
    }

    @Override
    public int getPendingMemberCount() {
        return saccoDao.pendingMemberCount();
    }
}
