package org.IntegratedHealthFinance.systems.navigation;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 * Contains the links to the different pages with in the application.
 * It is to help us navigate between the pages in the application easily.
 */
@ManagedBean(name = "navigation")
@ApplicationScoped // There should be only one instance of the class created for the entire
                   // application
public class Navigation {

    private final String dashboard = "/pages/dashboard/Dashboard.xhtml";

    private final String landing = "/pages/landing/Landing.xhtml";

    private final String register = "/pages/auth/Register.xhtml";

    private final String login = "/pages/auth/Login.xhtml";

    private final String apply = "/pages/auth/Apply.xhtml";

    private final String adminDashboard = "/pages/admin/adminDashboard.xhtml";

    private final String addMember = "/pages/admin/AddMember.xhtml";

    private final String viewMembers = "/pages/admin/ViewMembers.xhtml";

    private final String adminReports = "/pages/admin/AdminReports.xhtml";

    public String getDashboard() {
        return dashboard;
    }

    public String getLanding() {
        return landing;
    }

    public String getRegister() {
        return register;
    }

    public String getLogin() {
        return login;
    }

    public String getApply() {
        return apply;
    }

    public String getAdminDashboard() {
        return adminDashboard;
    }
    
    public String getAddMember() {
        return addMember;
    }

    public String getViewMembers() {
        return viewMembers;
    }

    public String getAdminReports() {
        return adminReports;
    }
}
