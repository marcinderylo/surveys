package org.adaptiveplatform.surveys.service.internal;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.domain.UserAccount;
import org.adaptiveplatform.surveys.exception.security.NoSuchUserException;
import org.adaptiveplatform.surveys.service.UserAccountRepository;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Repository("userAccountRepository")
@Transactional(propagation = Propagation.REQUIRED)
public class HibernateUserAccountRepository implements UserAccountRepository {

    @Resource(name = "sessionFactory")
    private SessionFactory sf;

    @Override
    public void persist(UserAccount account) {
        sf.getCurrentSession().persist(account);
    }

    @Override
    public UserAccount get(Long userId) {
        return (UserAccount) sf.getCurrentSession().get(UserAccount.class,
                userId);
    }

    @Override
    public UserAccount get(String email) {
        Criteria criteria = sf.getCurrentSession().createCriteria(
                UserAccount.class);
        criteria.add(Restrictions.naturalId().set("email", email));
        return (UserAccount) criteria.uniqueResult();
    }

    @Override
    public UserAccount getExisting(Long userId) {
        UserAccount userAccount = get(userId);
        if (userAccount == null) {
            throw new NoSuchUserException(userId);
        }
        return userAccount;
    }

    @Override
    public UserAccount getExisting(String email) {
        UserAccount userAccount = get(email);
        if (userAccount == null) {
            throw new NoSuchUserException(email);
        }
        return userAccount;
    }
}
