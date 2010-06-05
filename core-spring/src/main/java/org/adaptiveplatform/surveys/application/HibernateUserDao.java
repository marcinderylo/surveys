package org.adaptiveplatform.surveys.application;

import java.util.List;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.domain.Role;
import org.adaptiveplatform.surveys.dto.UserDto;
import org.adaptiveplatform.surveys.dto.UserQuery;
import org.adaptiveplatform.surveys.exception.security.NotAllowedToViewUserDetailsException;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.flex.remoting.RemotingExclude;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("userDao")
@RemotingDestination
@Transactional(propagation = Propagation.SUPPORTS)
public class HibernateUserDao implements UserDao {

    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;
    @Resource
    private AuthenticationService authentication;

    @Secured({Role.USER, Role.EVALUATOR, Role.ADMINISTRATOR, Role.TEACHER})
    @Override
    public UserDto getUser(Long id) {
        UserDto caller = authentication.getCurrentUser();
        if (isUserAdministrator(caller) || caller.getId().equals(id)) {
            Session session = sessionFactory.getCurrentSession();
            return (UserDto) session.get(UserDto.class, id);
        } else {
            throw new NotAllowedToViewUserDetailsException();
        }
    }

    @RemotingExclude
    @Override
    public UserDto getByEmail(String email) {
        // might want to apply security check #from getUser(Long)
        Criteria criteria = sessionFactory.getCurrentSession().
                createCriteria(UserDto.class);
        criteria.add(Restrictions.naturalId().set(UserDto.EMAIL,
                email));
        return (UserDto) criteria.uniqueResult();
    }

    @Secured({Role.ADMINISTRATOR, Role.TEACHER})
    @Override
    public List<UserDto> query(UserQuery query) {
        Criteria criteria = sessionFactory.getCurrentSession().
                createCriteria(UserDto.class);
        if (query != null
                && StringUtils.hasText(query.getNameContains())) {
            criteria.add(Restrictions.or( // username or email contains query
                    Restrictions.like(UserDto.USERNAME, "%" + query.
                    getNameContains() + "%"), //
                    Restrictions.like(UserDto.EMAIL, "%"
                    + query.getNameContains() + "%")));
        }
        criteria.setResultTransformer(
                DistinctRootEntityResultTransformer.INSTANCE);
        return criteria.list();
    }

    private boolean isUserAdministrator(UserDto user) {
        return user.getRoles().contains(Role.ADMINISTRATOR) || user.getRoles().
                contains(Role.TEACHER);
    }
}
