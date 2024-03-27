package org.IntegratedHealthFinance.systems.views.withdraw;

import java.io.IOException;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.IntegratedHealthFinance.Dao.SaccoDao;
import org.IntegratedHealthFinance.systems.models.Account;
import org.IntegratedHealthFinance.systems.models.Members;
import org.IntegratedHealthFinance.systems.services.AccountService;
import org.IntegratedHealthFinance.systems.services.AccountServiceImp;
import org.IntegratedHealthFinance.systems.services.MemberService;
import org.IntegratedHealthFinance.systems.services.MemberServiceImp;
import org.IntegratedHealthFinance.systems.services.TransactionService;
import org.IntegratedHealthFinance.systems.services.TransactionServiceImp;

@ManagedBean(name = "withdraw")
@SessionScoped
public class Withdraw {
    private double withdrawAmount;
    private String transferType;
    private String email;
    private int accountId;

    private MemberService memberService;

    public MemberService getMemberService() {
        return memberService;
    }

    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public double getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(double withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

    private AccountService accountService;

    public AccountService getAccountService() {
        return accountService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public TransactionService transactionService;

    public TransactionService getTransactionService() {
        return transactionService;
    }

    public void setTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public Withdraw() {
        // Initialize the SaccoDao
        SaccoDao saccoDao = new SaccoDao();

        transferType = "withdrawal";

        // Create the AccountService implementation with the SaccoDao instance
        accountService = new AccountServiceImp(saccoDao);

        // Create the TransactionService implementation with the SaccoDao instance
        transactionService = new TransactionServiceImp(saccoDao);

    }

    // private void addFlashMessage(FacesMessage.Severity severity, String summary, String detail) {
    //     FacesContext facesContext = FacesContext.getCurrentInstance();
    //     Flash flash = facesContext.getExternalContext().getFlash();
    //     flash.setKeepMessages(true);
    //     facesContext.addMessage(null, new FacesMessage(severity, summary, detail));
    // }

    public void makeWithdraw() {
        Members loggedInUser = (Members) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("loggedInUser");

        if (loggedInUser != null) {
            Account account = loggedInUser.getAccount();

            if (account != null) {
                transactionService.withdraw(account, withdrawAmount, new Date().toString());
                withdrawAmount = 0.0;

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Account not found", null));
            }
        } else {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getNumberOfWithdraws() {
        Members loggedInUser = (Members) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                .get("loggedInUser");
        if (loggedInUser != null) {
            SaccoDao saccoDao = new SaccoDao();
            return saccoDao.getWithdrawCountForUser(loggedInUser);
        } else {
            return 0;
        }
    }

    public void makeInternalTransfer() {
        Members loggedInUser = (Members) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                .get("loggedInUser");
    
        if (loggedInUser != null) {
            Account account = loggedInUser.getAccount();
    
            if (account != null) {
                // Fetch the recipient member by email
                memberService = new MemberServiceImp();
                Members recipientMember = memberService.getMemberByEmail(email);
    
                if (recipientMember != null && recipientMember.getAccount() != null) {
                    // Check if the email and account ID provided by the user match
                    if (!recipientMember.getEmail().equals(email) || recipientMember.getAccount().getAccountId() != accountId) {
                        FacesContext.getCurrentInstance().addMessage(null,
                                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Email and Account ID do not match."));
                    } else if (recipientMember.getAccount().getAccountId() == account.getAccountId()) {
                        // Check if the email matches the account ID provided by the user
                        FacesContext.getCurrentInstance().addMessage(null,
                                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Internal transaction cannot be made to your own account"));
                    } else {
                        // Perform the internal transfer
                        transactionService.internalTransfer(account, recipientMember.getAccount(), withdrawAmount,
                                new Date().toString());
                        withdrawAmount = 0.0;
    
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Recipient not found", null));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Account not found", null));
            }
        } else {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    
}
    
    
    

