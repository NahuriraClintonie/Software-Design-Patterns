package org.IntegratedHealthFinance.systems.views.authentication;

import java.io.IOException;

import javax.faces.application.FacesMessage;
// import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

import org.IntegratedHealthFinance.systems.controllers.Hyperlinks;
import org.IntegratedHealthFinance.systems.models.Members;
import org.IntegratedHealthFinance.systems.services.MemberService;
import org.IntegratedHealthFinance.systems.services.MemberServiceImp;
// import org.primefaces.PrimeFaces;

@ManagedBean(name = "memberForm")
@SessionScoped
public class MemberForm {
    FacesMessage message;
    private Members member;
    private MemberService mimp;

    public Members getMember() {
        return member;
    }

    public void setMember(Members member) {
        this.member = member;
    }

    public MemberForm() {
        this.member = new Members();
        this.mimp = new MemberServiceImp();
    }

    private void addFlashMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Flash flash = facesContext.getExternalContext().getFlash();
        flash.setKeepMessages(true);
        facesContext.addMessage("messages", new FacesMessage(severity, summary, detail));
    }

    public void doRegister() throws IOException {
        boolean emailExists = mimp.isEmailExists(member.getEmail());
        boolean phoneNumberExists = mimp.isPhoneNumberExists(member.getPhoneNumber());

        if (emailExists) {
            // Email already exists, display an error message
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Email already in use");
            FacesContext.getCurrentInstance().addMessage("messages", message);
            return; // Return without saving the member
        }

        if (phoneNumberExists) {
            // Phone number already exists, display an error message
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Phone number already in use");
            FacesContext.getCurrentInstance().addMessage("messages", message);
            return; // Return without saving the member
        }
        // Save the member to the database
        mimp.saveMember(member);
        member = new Members();

        addFlashMessage(FacesMessage.SEVERITY_INFO, "Success", "Registration Successful");

        // Redirect to the login page after the member is saved
        String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        FacesContext.getCurrentInstance().getExternalContext().redirect(path + Hyperlinks.login);
    }

    public void doAdminRegister() throws IOException{
        boolean emailExists = mimp.isEmailExists(member.getEmail());
        boolean phoneNumberExists = mimp.isPhoneNumberExists(member.getPhoneNumber());

        if (emailExists) {
            // Email already exists, display an error message
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Email already in use");
            FacesContext.getCurrentInstance().addMessage("messages", message);
            return; // Return without saving the member
        }

        if (phoneNumberExists) {
            // Phone number already exists, display an error message
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Phone number already in use");
            FacesContext.getCurrentInstance().addMessage("messages", message);
            return; // Return without saving the member
        }
        // Save the member to the database
        mimp.saveMember(member);
        member = new Members();

        addFlashMessage(FacesMessage.SEVERITY_INFO, "Success", "Member registered successfully");

        // Redirect to the login page after the member is saved
        String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        FacesContext.getCurrentInstance().getExternalContext().redirect(path + Hyperlinks.viewMembers);
    }
}
