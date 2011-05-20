package org.adaptiveplatform.surveys.application;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.domain.Role;
import org.adaptiveplatform.surveys.dto.GroupRoleEnum;
import org.adaptiveplatform.surveys.dto.StudentGroupDto;
import org.adaptiveplatform.surveys.dto.StudentGroupQuery;
import org.adaptiveplatform.surveys.dto.UserDto;
import org.adaptiveplatform.surveys.exception.CantQueryGroupsAsAdministratorException;
import org.adaptiveplatform.surveys.exception.CantQueryGroupsAsEvaluatorException;
import org.adaptiveplatform.surveys.exception.CantQueryGroupsAsStudentException;
import org.adaptiveplatform.surveys.exception.NoSuchGroupException;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 
 * @author Marcin Deryło
 */
@Service("studentGroupDao")
@RemotingDestination
@Transactional(propagation = Propagation.SUPPORTS)
public class HibernateStudentGroupDao implements StudentGroupDao {

	@Resource
	private SessionFactory sf;
	@Resource
	private AuthenticationService authenticationService;

	@Override
	@Secured(Role.TEACHER)
	public StudentGroupDto getGroup(Long groupId) {

		UserDto caller = authenticationService.getCurrentUser();

		StudentGroupDto group = (StudentGroupDto) sf.getCurrentSession()
				.getNamedQuery(StudentGroupDto.Query.GET_STUDENT_GROUP).setParameter("groupId", groupId)
				.setParameter("userId", caller.getId()).uniqueResult();
		if (group == null) {
			throw new NoSuchGroupException(groupId);
		}

		return group;
	}

	@Override
	@Secured({ Role.TEACHER, Role.EVALUATOR })
	public List<StudentGroupDto> query(StudentGroupQuery query) {

		UserDto caller = authenticationService.getCurrentUser();

		Criteria criteria = sf.getCurrentSession().createCriteria(StudentGroupDto.class);
		switch (query.getRunAs()) {
		case EVALUATOR:
			if (!caller.getRoles().contains(Role.EVALUATOR)) {
				throw new CantQueryGroupsAsEvaluatorException();
			}
			criteria.createAlias("evaluators", "usr");
			criteria.add(Restrictions.eq("usr.id", caller.getId()));

			break;
		case GROUP_ADMINISTRATOR:
			if (!caller.getRoles().contains(Role.TEACHER)) {
				throw new CantQueryGroupsAsAdministratorException();
			}
			criteria.createAlias("administrators", "usr");
			criteria.add(Restrictions.eq("usr.id", caller.getId()));

			break;
		case STUDENT:
			if (!caller.getRoles().contains(Role.STUDENT)) {
				throw new CantQueryGroupsAsStudentException();
			}
			break;
		default:// FIXME turn into validation exception
			throw new IllegalArgumentException("Can only query groups as STUDENT, GROUP_ADMINSTRATOR or EVALUATOR");
		}
		if (StringUtils.hasText(query.getGroupNamePattern())) {
			criteria.add(Restrictions.ilike("groupName", query.getGroupNamePattern(), MatchMode.ANYWHERE));
		}
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.addOrder(Order.asc("groupName"));

		List<StudentGroupDto> groups = criteria.list();

		// need to anonymize groups for evaluators
		if (resultsShouldBeAnonymized(query)) {
			anonymizeGroups(groups);
		}

		return groups;
	}

	private void anonymizeGroups(List<StudentGroupDto> groups) {
		for (StudentGroupDto group : groups) {
			group.setStudents(Collections.EMPTY_SET);
			group.setEvaluators(Collections.EMPTY_SET);
		}
	}

	private boolean resultsShouldBeAnonymized(StudentGroupQuery query) {
		return Arrays.asList(GroupRoleEnum.EVALUATOR, GroupRoleEnum.STUDENT).contains(query.getRunAs());
	}

	// TODO change to Role.STUDENT?
	@Override
	@Secured(Role.USER)
	public List<StudentGroupDto> getAvailableGroups() {
		Criteria criteria = sf.getCurrentSession().createCriteria(StudentGroupDto.class);
		criteria.add(Restrictions.eq("studentsCanSignUp", Boolean.TRUE));
		Collection<Long> groupsAlreadyJoined = getGroupsUserIsStudentIn();
		if (!groupsAlreadyJoined.isEmpty()) {
			criteria.add(Restrictions.not(Restrictions.in("id", groupsAlreadyJoined)));
		}
		criteria.addOrder(Order.asc("groupName"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		final List<StudentGroupDto> groups = criteria.list();
		anonymizeGroups(groups);
		return groups;
	}

	private Collection<Long> getGroupsUserIsStudentIn() {
		final Criteria criteria = sf.getCurrentSession().createCriteria(StudentGroupDto.class);
		criteria.createAlias("students", "usr", Criteria.LEFT_JOIN);
		criteria.add(Restrictions.eq("usr.id", authenticationService.getCurrentUser().getId()));
		criteria.setProjection(Projections.property("id"));
		return criteria.list();
	}
}
