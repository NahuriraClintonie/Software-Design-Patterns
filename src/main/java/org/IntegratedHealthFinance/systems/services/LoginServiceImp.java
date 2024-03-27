package org.IntegratedHealthFinance.systems.services;

import org.IntegratedHealthFinance.systems.enumerations.Status;
import org.IntegratedHealthFinance.systems.models.Members;
import org.mindrot.jbcrypt.BCrypt;

public class LoginServiceImp implements LoginService {
    private MemberService memberService;

    public LoginServiceImp() {
        this.memberService = new MemberServiceImp();
    }

    @Override
    public boolean authenticate(String email, String password, Status status){
        Members member = memberService.getMemberByEmail(email);
        if (member != null) {
            if (BCrypt.checkpw(password, member.getPassword())) {
                if (member.getStatus().equals(status)) {
                    return true;
                }
            }
        }
        return false;
    }

     

}
