package org.IntegratedHealthFinance.systems.services;

import org.IntegratedHealthFinance.systems.enumerations.Status;

public interface LoginService{
    boolean authenticate(String email, String password, Status status);
}