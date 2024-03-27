package org.IntegratedHealthFinance.systems.services;

import java.util.List;

import org.IntegratedHealthFinance.systems.models.Members;

public interface MemberService {
    void saveMember(Members member);
    Members getMemberByEmail(String email);
    List<Members> getAllMembers();
    List<Members> getApprovedMembers();
    boolean isEmailExists(String email);
    boolean isPhoneNumberExists(String phoneNumber);
    void updateMember(Members member);
}
