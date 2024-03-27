package org.IntegratedHealthFinance.systems.views.profile;

// import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.IntegratedHealthFinance.systems.controllers.Hyperlinks;
import org.IntegratedHealthFinance.systems.models.Members;

import java.io.IOException;

@ManagedBean(name = "profile")
@SessionScoped
public class Profile {
    private String firstName;
    private String lastName;
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    private String email;
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String phoneNumber;
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    private String address;
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String occupation;

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public Profile() throws IOException {
        Members loggedInUser = (Members)FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                .get("loggedInUser");
        
        if (loggedInUser != null) {
            firstName = loggedInUser.getFirstName();
            lastName = loggedInUser.getLastName();
            email = loggedInUser.getEmail();
            phoneNumber = loggedInUser.getPhoneNumber();
            address = loggedInUser.getAddress();
            occupation = loggedInUser.getOccupation();
        } else{
            String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
            FacesContext.getCurrentInstance().getExternalContext().redirect(path + Hyperlinks.login);
        }
        
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    

    public void logout() throws IOException {
        // Clear the session and redirect to the login page
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.invalidateSession();
        externalContext.redirect(externalContext.getRequestContextPath() + "/pages/auth/Login.xhtml");
    }

    public void viewProfile() throws IOException {
        // Redirect to the profile page if the session exists
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        if (externalContext.getSession(false) != null) {
            externalContext.redirect(externalContext.getRequestContextPath() + "/pages/transaction/Profile.xhtml");
        }
    }
}
