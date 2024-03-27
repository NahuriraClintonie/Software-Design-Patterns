package org.IntegratedHealthFinance.systems.views.dashboard;

import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.IntegratedHealthFinance.Dao.SaccoDao;
import org.IntegratedHealthFinance.systems.controllers.Hyperlinks;
import org.IntegratedHealthFinance.systems.models.Members;
import org.IntegratedHealthFinance.systems.services.AdminService;
import org.IntegratedHealthFinance.systems.services.AdminServiceImp;

@ManagedBean(name = "adminDashboard")
@ViewScoped
public class AdminDashboard {
    private String firstName;
    private AdminService adminService;
    private int totalMembers;
    private long totalTransactions;
    private long totalDeposits;
    private int approvedMembers;
    private int pendingMembers;
    private long totalWithdrawals;
    private long internalTransferCount;

    public AdminService getAdminService() {
        return adminService;
    }

    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }
    

    public long getInternalTransferCount() {
        return internalTransferCount;
    }

    public void setInternalTransferCount(long internalTransferCount) {
        this.internalTransferCount = internalTransferCount;
    }

    public long getTotalWithdrawals() {
        return totalWithdrawals;
    }

    public void setTotalWithdrawals(long totalWithdrawals) {
        this.totalWithdrawals = totalWithdrawals;
    }

    public int getPendingMembers() {
        return pendingMembers;
    }

    public void setPendingMembers(int pendingMembers) {
        this.pendingMembers = pendingMembers;
    }

    public long getTotalDeposits() {
        return totalDeposits;
    }

    public void setTotalDeposits(long totalDeposits) {
        this.totalDeposits = totalDeposits;
    }

    public int getApprovedMembers() {
        return approvedMembers;
    }

    public void setApprovedMembers(int approvedMembers) {
        this.approvedMembers = approvedMembers;
    }

    public long getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(long totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public int getTotalMembers() {
        return totalMembers;
    }

    public void setTotalMembers(int totalMembers) {
        this.totalMembers = totalMembers;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public AdminDashboard() throws IOException{
        Members adminUser = (Members) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("adminUser");
        if(adminUser != null){
            firstName = adminUser.getFirstName();
            SaccoDao saccoDao = new SaccoDao();
            adminService = new AdminServiceImp(saccoDao);

            totalMembers = adminService.getTotalMemberCount();
            totalTransactions = adminService.getTotalTransactionCount();
            totalDeposits = adminService.getTotalDepositCount();
            totalWithdrawals = adminService.getTotalWithdrawalCount();
            internalTransferCount = adminService.getInternalTransferCount()/2;
            approvedMembers = adminService.getApprovedMemberCount();
            pendingMembers = adminService.getPendingMemberCount();
    }else{
        String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
            FacesContext.getCurrentInstance().getExternalContext().redirect(path + Hyperlinks.login);
    }
}

    
}
