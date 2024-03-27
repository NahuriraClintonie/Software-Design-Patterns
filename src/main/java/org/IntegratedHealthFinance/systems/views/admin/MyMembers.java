package org.IntegratedHealthFinance.systems.views.admin;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
// import javax.faces.context.Flash;
import javax.faces.context.Flash;

import org.IntegratedHealthFinance.Dao.SaccoDao;
import org.IntegratedHealthFinance.systems.controllers.Hyperlinks;
import org.IntegratedHealthFinance.systems.models.Members;
// import org.primefaces.PrimeFaces;

@ManagedBean(name = "myMembers")
@ViewScoped
public class MyMembers {
    private List<Members> members;
    private List<Members> approvedMembers;
    private Members selectedMember;
    FacesMessage message;

    public Members getSelectedMember() {
        return selectedMember;
    }

    public void setSelectedMember(Members selectedMember) {
        this.selectedMember = selectedMember;
    }

    public void initSelectedMember(Members selectedMember){
        setSelectedMember(selectedMember);
    }

    public MyMembers() {
        // Initialize the members list in the constructor
        SaccoDao saccoDao = new SaccoDao();
        members = saccoDao.getAllMembers();
        approvedMembers = saccoDao.getApprovedMembers();
    }

    public List<Members> getMembers() {
        return members;
    }

    public List<Members> getApprovedMembers() {
        return approvedMembers;
    }

    private void addFlashMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Flash flash = facesContext.getExternalContext().getFlash();
        flash.setKeepMessages(true);
        facesContext.addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public void approveUser(Members member) {
    SaccoDao saccoDao = new SaccoDao();
    saccoDao.updateMemberStatus(member);
    members.remove(member); // Remove the approved member from the list
    
    addFlashMessage(FacesMessage.SEVERITY_INFO, "Success", "Member approved successfully");

    String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
    try {
        FacesContext.getCurrentInstance().getExternalContext().redirect(path + Hyperlinks.saccoMembers);
    } catch (Exception e) {
        e.printStackTrace();
    }

}

public void rejectUser(Members member) {
    SaccoDao saccoDao = new SaccoDao();
    saccoDao.updateMemberStatusToRejected(member);
    members.remove(member); // Remove the rejected member from the list

    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Rejection", "Member rejected successfully");
    FacesContext.getCurrentInstance().addMessage("message", message);
}

}
