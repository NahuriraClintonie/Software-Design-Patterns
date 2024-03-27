package org.IntegratedHealthFinance.systems.services;

import java.util.List;

import org.IntegratedHealthFinance.systems.models.Members;
import org.mindrot.jbcrypt.BCrypt;
import org.IntegratedHealthFinance.Dao.SaccoDao;

public class MemberServiceImp implements MemberService{
    private final SaccoDao memberDao = new SaccoDao();
    @Override
    public void saveMember(Members member) {
        String hashedPassword = hashPassword(member.getPassword());
        member.setPassword(hashedPassword);
        memberDao.saveMember(member);
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public Members getMemberByEmail(String email) {
        return memberDao.getMemberByEmail(email);
    }

    @Override
    public List<Members> getAllMembers() {
        return memberDao.getAllMembers();
    }

    @Override
    public List<Members> getApprovedMembers() {
        return memberDao.getApprovedMembers();
    }

    @Override
    public boolean isEmailExists(String email) {
        return memberDao.isEmailExists(email);
    }

    @Override
    public boolean isPhoneNumberExists(String phoneNumber) {
        return memberDao.isPhoneNumberExists(phoneNumber);
    }
    
    @Override
    public void updateMember(Members member) {
        memberDao.updateMember(member);
    }
}
