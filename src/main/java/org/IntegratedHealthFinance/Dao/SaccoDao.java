package org.IntegratedHealthFinance.Dao;

// import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.NoResultException;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.IntegratedHealthFinance.systems.config.SessionConfiguration;
import org.IntegratedHealthFinance.systems.enumerations.Status;
import org.IntegratedHealthFinance.systems.enumerations.TransactionType;
import org.IntegratedHealthFinance.systems.models.Account;
import org.IntegratedHealthFinance.systems.models.Members;
import org.IntegratedHealthFinance.systems.models.Transactions;


public class SaccoDao {

    public void saveMember(Members member) {
        Session session = SessionConfiguration.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(member);
        session.getTransaction().commit();
        session.close();
    }

    public Members getMemberByEmail(String email) {
        Session session = null;
        Members member = null;
        try {
            session = SessionConfiguration.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Members.class);
            criteria.add(Restrictions.eq("email", email));
            member = (Members) criteria.uniqueResult();
        } catch (NoResultException ex) {
            // If no member found, return null
            member = null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return member;
    }

    public List<Members> getAllMembers() {
        Session session = SessionConfiguration.getSessionFactory().openSession();
        session.beginTransaction();
    
        String hql = "FROM Members m WHERE m.status = :status";
        Query query = session.createQuery(hql);
        query.setParameter("status", Status.Pending);
        List<Members> membersList = query.list();
        session.getTransaction().commit();
        session.close();
    
        return membersList;
    }

    public List<Members> allMembers(){
        Session session = SessionConfiguration.getSessionFactory().openSession();
        session.beginTransaction();
    
        String hql = "FROM Members m";
        Query query = session.createQuery(hql);
        List<Members> membersList = query.list();
        session.getTransaction().commit();
        session.close();
    
        return membersList;
    }

    public List<Members> getApprovedMembers(){
        Session session = SessionConfiguration.getSessionFactory().openSession();
        session.beginTransaction();
    
        String hql = "FROM Members m WHERE m.status = :status";
        Query query = session.createQuery(hql);
        query.setParameter("status", Status.Approved);
        List<Members> membersList = query.list();
        session.getTransaction().commit();
        session.close();
    
        return membersList;
    }

    public List<Integer> getAgeDistribution() {
        Session session = null;
        try {
            session = SessionConfiguration.getSessionFactory().openSession();
            
            // Query to get ages of approved members
            String hql = "SELECT YEAR(CURRENT_DATE()) - YEAR(m.dateOfBirth) FROM Members m WHERE m.status = :status";
            Query query = session.createQuery(hql);
            query.setParameter("status", Status.Approved);
            List<Integer> ages = query.list();
            
            // Categorize ages into different ranges (18-25, 25-32, 32-40, 40-50, 50+)
            List<Integer> ageDistribution = new ArrayList<>(5);
            int[] ranges = {25, 32, 40, 50}; // Age ranges
            for (int i = 0; i <= ranges.length; i++) {
                ageDistribution.add(0);
            }
            for (int age : ages) {
                if (age >= 18 && age <= ranges[0]) {
                    ageDistribution.set(0, ageDistribution.get(0) + 1);
                } else if (age <= ranges[1]) {
                    ageDistribution.set(1, ageDistribution.get(1) + 1);
                } else if (age <= ranges[2]) {
                    ageDistribution.set(2, ageDistribution.get(2) + 1);
                } else if (age <= ranges[3]) {
                    ageDistribution.set(3, ageDistribution.get(3) + 1);
                } else {
                    ageDistribution.set(4, ageDistribution.get(4) + 1);
                }
            }
            return ageDistribution;
        } catch (HibernateException e) {
            // Handle any exceptions if necessary
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    public int approvedMemberCount(){
        Session session = SessionConfiguration.getSessionFactory().openSession();
        session.beginTransaction();
    
        String hql = "SELECT COUNT(*) FROM Members m WHERE m.status = :status";
        Query query = session.createQuery(hql);
        query.setParameter("status", Status.Approved);
        Long count = (Long) query.uniqueResult();
        session.getTransaction().commit();
        session.close();
    
        return count.intValue();
    }

    public int pendingMemberCount(){
        Session session = SessionConfiguration.getSessionFactory().openSession();
        session.beginTransaction();
    
        String hql = "SELECT COUNT(*) FROM Members m WHERE m.status = :status";
        Query query = session.createQuery(hql);
        query.setParameter("status", Status.Pending);
        Long count = (Long) query.uniqueResult();
        session.getTransaction().commit();
        session.close();
    
        return count.intValue();
    }

    public void updateMemberStatus(Members member) {
        Session session = null;
        try {
            session = SessionConfiguration.getSessionFactory().openSession();
            session.beginTransaction();
    
            // Set the status to "Approved"
            member.setStatus(Status.Approved);
    
            // Creating a new account and setting the initial balance
            Account account = new Account();
            account.setBalance(0.0);
    
            // Set the account object in the member entity
            member.setAccount(account);
    
            // Save the member entity to update it in the database along with the associated account
            session.saveOrUpdate(member);
    
            session.getTransaction().commit();
        } catch (HibernateException e) {
            // Handle any exceptions if necessary
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    public void updateMemberStatusToRejected(Members member) {
        Session session = null;
        try {
            session = SessionConfiguration.getSessionFactory().openSession();
            session.beginTransaction();
            member.setStatus(Status.Rejected); // Set the status to "Rejected"
            session.update(member);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            // Handle any exceptions if necessary
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void updateMember(Members member){
        Session session = null;
        try {
            session = SessionConfiguration.getSessionFactory().openSession();
            session.beginTransaction();
            session.update(member);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            // Handle any exceptions if necessary
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public boolean isEmailExists(String email) {
        Session session = null;
        try {
            session = SessionConfiguration.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Members.class);
            criteria.add(Restrictions.eq("email", email));
            Members member = (Members) criteria.uniqueResult();
            return member != null;
        } catch (HibernateException ex) {
            // Handle any exceptions or log errors as needed
            ex.printStackTrace();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public boolean isPhoneNumberExists(String phoneNumber) {
        Session session = null;
        try {
            session = SessionConfiguration.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Members.class);
            criteria.add(Restrictions.eq("phoneNumber", phoneNumber));
            Members member = (Members) criteria.uniqueResult();
            return member != null;
        } catch (HibernateException ex) {
            // Handle any exceptions or log errors as needed
            ex.printStackTrace();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void saveAccount(Account account) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = SessionConfiguration.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.saveOrUpdate(account);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public int getDepositCountForUser(Members member) {
        Session session = null;
        try {
            session = SessionConfiguration.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Transactions.class);
            criteria.add(Restrictions.eq("account", member.getAccount()));
            criteria.add(Restrictions.eq("transactionType", TransactionType.Deposit)); // Specify "Deposit" as the transaction type
            criteria.setProjection(Projections.rowCount());
            Long count = (Long) criteria.uniqueResult();
            return count.intValue();
        } catch (HibernateException e) {
            // Handle any exceptions if necessary
            e.printStackTrace();
            return 0;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public int getWithdrawCountForUser(Members member) {
        Session session = null;
        try {
            session = SessionConfiguration.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Transactions.class);
            criteria.add(Restrictions.eq("account", member.getAccount()));
            criteria.add(Restrictions.eq("transactionType", TransactionType.Withdrawal)); // Specify "Deposit" as the transaction type
            criteria.setProjection(Projections.rowCount());
            Long count = (Long) criteria.uniqueResult();
            return count.intValue();
        } catch (HibernateException e) {
            // Handle any exceptions if necessary
            e.printStackTrace();
            return 0;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public int getInternalTransferCountForUser(Members member){
        Session session = null;
        try{
            session = SessionConfiguration.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Transactions.class);
            criteria.add(Restrictions.eq("account", member.getAccount()));
            criteria.add(Restrictions.eq("transactionType", TransactionType.Internal_Transfer));
            criteria.setProjection(Projections.rowCount());
            Long count = (Long) criteria.uniqueResult();
            return count.intValue();
        }catch (HibernateException e){
            e.printStackTrace();
            return 0;
        }
    }
    
    public Account getAccountByAccountId(int accountId) {
        Session session = null;
        try {
            session = SessionConfiguration.getSessionFactory().openSession();
            String hql = "FROM Account a WHERE a.accountId = :accountId";
            Query query = session.createQuery(hql);
            query.setParameter("accountId", accountId);
            return (Account) query.uniqueResult();
        } catch (NoResultException e) {
            // If no account found, return null or handle the situation as needed
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void saveTransaction(Transactions transaction) {
        Session session = null;
        Transaction tx = null;
        try {
            session = SessionConfiguration.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.save(transaction);
            tx.commit();
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            ex.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public Transactions getTransactionById(int transactionId) {
        Session session = null;
        try {
            session = SessionConfiguration.getSessionFactory().openSession();
            return (Transactions) session.get(Transactions.class, transactionId);
        } catch (Exception e) {
            // Handle any exceptions if necessary
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    public List<Transactions> getTransactionsForAccount(Account account) {
        Session session = null;
        try {
            session = SessionConfiguration.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Transactions.class);
            criteria.add(Restrictions.eq("account", account));
            return criteria.list();
        } catch (Exception e) {
            // Handle any exceptions if necessary
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public long getTotalTransactionCount() {
        Session session = null;
        try {
            session = SessionConfiguration.getSessionFactory().openSession();
            String hql = "SELECT COUNT(*) FROM Transactions";
            Query query = session.createQuery(hql);
            return (Long) query.uniqueResult();
        } catch (HibernateException e) {
            // Handle any exceptions if necessary
            e.printStackTrace();
            return 0;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<Transactions> getAllTransactions(){
        Session session = null;
        try{
            session = SessionConfiguration.getSessionFactory().openSession();
            String hql = "FROM Transactions";
            Query query = session.createQuery(hql);
            return query.list();
        }catch(HibernateException e){
            e.printStackTrace();
            return null;
        }finally{
            if(session != null){
                session.close();
            }
        }
    }

    public long getTotalDepositCount(){
        Session session = null;
        try{
            session = SessionConfiguration.getSessionFactory().openSession();
            String hql = "SELECT COUNT(*) FROM Transactions WHERE transactionType = :transactionType";
            Query query = session.createQuery(hql);
            query.setParameter("transactionType", TransactionType.Deposit);
            return (Long) query.uniqueResult();
        }catch(HibernateException e){
            e.printStackTrace();
            return 0;
        }finally{
            if(session != null){
                session.close();
            }
        }
    }

    public long getTotalWithdrawalCount(){
        Session session = null;
        try{
            session = SessionConfiguration.getSessionFactory().openSession();
            String hql = "SELECT COUNT(*) FROM Transactions WHERE transactionType = :transactionType";
            Query query = session.createQuery(hql);
            query.setParameter("transactionType", TransactionType.Withdrawal);
            return (Long) query.uniqueResult();
        }catch(HibernateException e){
            e.printStackTrace();
            return 0;
        }finally{
            if(session != null){
                session.close();
            }
        }
    }

    public long getInternalTransferCount(){
        Session session = null;
        try{
            session = SessionConfiguration.getSessionFactory().openSession();
            String hql = "SELECT COUNT(*) FROM Transactions WHERE transactionType = :transactionType";
            Query query = session.createQuery(hql);
            query.setParameter("transactionType", TransactionType.Internal_Transfer);
            return (Long) query.uniqueResult();
        }catch(HibernateException e){
            e.printStackTrace();
            return 0;
        }finally{
            if(session != null){
                session.close();
            }
        }
    }

    public int getTotalMemberCount() {
        Session session = null;
        try {
            session = SessionConfiguration.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Members.class);
            criteria.setProjection(Projections.rowCount());
            return ((Long) criteria.uniqueResult()).intValue();
        } catch (HibernateException e) {
            // Handle any exceptions if necessary
            e.printStackTrace();
            return 0;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
}
