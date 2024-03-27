package org.IntegratedHealthFinance.systems.views.dashboard;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.IntegratedHealthFinance.systems.controllers.Hyperlinks;
import org.IntegratedHealthFinance.systems.models.Members;
import org.IntegratedHealthFinance.systems.services.MemberService;
import org.IntegratedHealthFinance.systems.services.MemberServiceImp;

@ManagedBean(name = "dashboard")
@SessionScoped
public class Dashboard {
    FacesMessage message;
    MemberService memberService = new MemberServiceImp();
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    private String firstName;
    private String lastName;
    private String email;
    private int accountId;
    private Members member = new Members();

    public Members getMember() {
        return member;
    }

    public void setMember(Members member) {
        this.member = member;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    private boolean editMode = false;

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

     // Method to toggle edit mode for a given field
     public void toggleEditMode(String field) {
        switch (field) {
            case "firstName":
                setEditMode(!isEditMode());
                break;
            case "lastName":
                setEditMode(!isEditMode());
                break;
            case "email":
                setEditMode(!isEditMode());
                break;
            case "phoneNumber":
                setEditMode(!isEditMode());
                break;
            case "address":
                setEditMode(!isEditMode());
                break;
            case "occupation":
                setEditMode(!isEditMode());
                break;
            default:
                break;
        }
    }

   

    public Dashboard() throws IOException {
        Members loggedInUser = (Members) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                .get("loggedInUser");
        if (loggedInUser != null) {
            firstName = loggedInUser.getFirstName();
            lastName = loggedInUser.getLastName();
            email = loggedInUser.getEmail();
            phoneNumber = loggedInUser.getPhoneNumber();
            address = loggedInUser.getAddress();
            occupation = loggedInUser.getOccupation();
            accountId = loggedInUser.getAccount().getAccountId();
        } else {
            // firstName = "Guest";
            String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
            FacesContext.getCurrentInstance().getExternalContext().redirect(path + Hyperlinks.login);
        }
    }

     // Method to update the user's profile
     public void updateProfile() {
        Members loggedInUser = (Members) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                .get("loggedInUser");
        if (loggedInUser != null) {
            // Check if the provided email is already taken
            boolean isEmailTaken = memberService.isEmailExists(email);
            boolean isPhoneNumberTaken = memberService.isPhoneNumberExists(phoneNumber);
            if (isEmailTaken && !email.equals(loggedInUser.getEmail())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Email Taken", "The email you entered is already taken. Please try another one."));
                System.out.println("Email taken");
                email = loggedInUser.getEmail();
                return;
            } else {
                System.out.println("Email not taken");
                if(isPhoneNumberTaken && !phoneNumber.equals(loggedInUser.getPhoneNumber())){
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Phone Number Taken", "The phone number you entered is already taken. Please try another one."));
                    System.out.println("Phone Number taken");
                    phoneNumber = loggedInUser.getPhoneNumber();
                    return;
                }else{
                    System.out.println("Phone Number not taken");
                
                // Update the user's information in the session
                loggedInUser.setFirstName(firstName);
                loggedInUser.setLastName(lastName);
                loggedInUser.setEmail(email);
                loggedInUser.setPhoneNumber(phoneNumber);
                loggedInUser.setAddress(address);
                loggedInUser.setOccupation(occupation);
    
                // Save the updated user information to the database using the MemberService
                memberService.updateMember(loggedInUser);
    
                // Display a success message if needed.
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Profile Updated", "Your profile has been updated successfully."));
    
                // Switch back to view mode
                setEditMode(false);
    
                }
            }
        }
    }
    

}
