package org.adaptiveplatform.surveys.application;

import static org.adaptiveplatform.surveys.utils.Collections42.asLongs;
import static org.adaptiveplatform.surveys.utils.DateTimeUtils.asDateTime;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.domain.FilledSurvey;
import org.adaptiveplatform.surveys.domain.GroupRole;
import org.adaptiveplatform.surveys.domain.Role;
import org.adaptiveplatform.surveys.domain.StudentGroup;
import org.adaptiveplatform.surveys.domain.SurveyPublication;
import org.adaptiveplatform.surveys.domain.SurveyTemplate;
import org.adaptiveplatform.surveys.dto.AddGroupMemberCommand;
import org.adaptiveplatform.surveys.dto.ChangeGroupMembersCommand;
import org.adaptiveplatform.surveys.dto.ChangeSurveyPublicationCommand;
import org.adaptiveplatform.surveys.dto.CreateStudentGroupCommand;
import org.adaptiveplatform.surveys.dto.GroupSignUpCommand;
import org.adaptiveplatform.surveys.dto.PublishSurveyTemplateCommand;
import org.adaptiveplatform.surveys.dto.SetGroupSignUpModeCommand;
import org.adaptiveplatform.surveys.dto.UserDto;
import org.adaptiveplatform.surveys.exception.CantRemoveSelfFromGroupException;
import org.adaptiveplatform.surveys.exception.DeletingGroupWithPublishedTemplatesException;
import org.adaptiveplatform.surveys.exception.NotAllowedToChangeGroupMembershipException;
import org.adaptiveplatform.surveys.exception.NotAllowedToChangePublicationDetailsException;
import org.adaptiveplatform.surveys.exception.NotAllowedToDeleteGroupException;
import org.adaptiveplatform.surveys.exception.NotAllowedToModifyGroupSignUpsettingsException;
import org.adaptiveplatform.surveys.exception.NotAllowedToPublishTemplatesInGroupException;
import org.adaptiveplatform.surveys.exception.NotTemplateCreatorException;
import org.adaptiveplatform.surveys.exception.PublishedSurveyTemplateAlreadyFilledException;
import org.adaptiveplatform.surveys.exception.security.EmailNotRegisteredException;
import org.adaptiveplatform.surveys.service.StudentGroupFactory;
import org.adaptiveplatform.surveys.service.StudentGroupRepository;
import org.adaptiveplatform.surveys.service.SurveyPublicationFactory;
import org.adaptiveplatform.surveys.service.SurveyPublicationRepository;
import org.adaptiveplatform.surveys.service.SurveyRepository;
import org.adaptiveplatform.surveys.service.SurveyTemplateRepository;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Marcin Deryło
 */
@Transactional
@Service("studentGroupFacade")
@RemotingDestination
public class StudentGroupFacadeImpl implements StudentGroupFacade {

	@Resource
	private StudentGroupFactory groupFactory;
	@Resource
	private StudentGroupRepository groupRepository;
	@Resource
	private SurveyTemplateRepository templateRepository;
	@Resource
	private AuthenticationService authenticationService;
	@Resource
	private SurveyPublicationRepository publicationRepository;
	@Resource
	private SurveyRepository surveyRepository;
	@Resource
	private SurveyPublicationFactory publicationFactory;
	@Resource
	private UserDao userDao;

	@Override
	@Secured({ Role.TEACHER })
	public Long createGroup(CreateStudentGroupCommand command) {
		StudentGroup group = groupFactory.newGroup(command.getGroupName());
		group.setStudentsCanSignUp(command.isOpenForSignup());
		addGroupMembers(group, command.getAddMemberCommands());
		return group.getId();
	}

	@Override
	@Secured({ Role.EVALUATOR })
	public void assignSurveyTemplate(PublishSurveyTemplateCommand command) {
		StudentGroup group = groupRepository.getExisting(command.getGroupId());

		UserDto caller = authenticationService.getCurrentUser();

		if (!group.isAssignedAsEvaluator(caller)) {
			throw new NotAllowedToPublishTemplatesInGroupException(command.getGroupId());
		}
		// FIXME add tests for publishing multiple survey template at once
		Collection<Long> templateIDs = asLongs(command.getSurveyTemplateIds());
		for (Long surveyTemplateId : templateIDs) {
			SurveyTemplate template = templateRepository.getExisting(surveyTemplateId);

			if (!template.getOwnerId().equals(caller.getId())) {
				throw new NotTemplateCreatorException(surveyTemplateId);
			}
			SurveyPublication publication = publicationFactory.create(template, group, command.getStartingDate(),
					command.getExpirationDate());

			publicationRepository.persist(publication);
		}
	}

	@Override
	public void removeSurveyTemplate(Long publishedSurveyTemplateId) {
		final SurveyPublication publication = publicationRepository.getExisting(publishedSurveyTemplateId);

		List<FilledSurvey> filledSurveys = surveyRepository.list(publishedSurveyTemplateId);

		if (!filledSurveys.isEmpty()) {
			throw new PublishedSurveyTemplateAlreadyFilledException(publishedSurveyTemplateId);
		}
		publicationRepository.remove(publication);
	}

	@Override
	@Secured({ Role.TEACHER })
	public void changeGroupMembers(ChangeGroupMembersCommand command) {
		Long groupId = command.getGroupId();
		StudentGroup group = groupRepository.getExisting(groupId);

		UserDto caller = authenticationService.getCurrentUser();
		if (!group.isGroupAdministrator(caller)) {
			throw new NotAllowedToChangeGroupMembershipException(group.getName());
		}

		for (String email : command.getRemoveMembers()) {
			if (caller.getEmail().equals(email)) {
				throw new CantRemoveSelfFromGroupException(group.getName());
			}
			UserDto user = getExistingByEmail(email);

			group.removeMember(user);
		}
		addGroupMembers(group, command.getAddMembers());

	}

	private void addGroupMembers(StudentGroup group, List<AddGroupMemberCommand> newMembers) {
		if (newMembers != null) {
			for (AddGroupMemberCommand addMemberCmd : newMembers) {
				UserDto user = getExistingByEmail(addMemberCmd.getEmail());
				GroupRole role = GroupRole.getByGroupRoleEnum(addMemberCmd.getRole());
				group.addMember(user, role);
			}
		}
	}

	private UserDto getExistingByEmail(String email) {
		UserDto user = userDao.getByEmail(email);
		if (user == null) {
			throw new EmailNotRegisteredException(email);
		}
		return user;
	}

	@Override
	public void removeGroup(Long groupId) {
		StudentGroup group = groupRepository.getExisting(groupId);

		UserDto caller = authenticationService.getCurrentUser();
		if (!group.isGroupAdministrator(caller)) {
			throw new NotAllowedToDeleteGroupException(group.getName());
		}
		if (!publicationRepository.getByGroup(groupId).isEmpty()) {
			throw new DeletingGroupWithPublishedTemplatesException(group.getName());
		}
		groupRepository.remove(groupId);
	}

	@Override
	public void changeSurveyPublication(ChangeSurveyPublicationCommand command) {
		final SurveyPublication publication = publicationRepository.getExisting(command.getPublicationId());

		UserDto caller = authenticationService.getCurrentUser();
		StudentGroup group = publication.getGroup();
		SurveyTemplate template = publication.getSurveyTemplate();
		if (!group.isAssignedAsEvaluator(caller)) {
			throw new NotAllowedToChangePublicationDetailsException(publication.getId(), group.getName());
		}

		if (!template.getOwnerId().equals(caller.getId())) {
			throw new NotTemplateCreatorException(template.getId());
		}
		publication.enableFillingInPeriod(asDateTime(command.getStartingDate()),
				asDateTime(command.getExpirationDate()));
	}

	@Override
	@Secured({ Role.TEACHER })
	public void setGroupSignUpMode(SetGroupSignUpModeCommand command) {
		final StudentGroup group = groupRepository.getExisting(command.getGroupId());
		UserDto caller = authenticationService.getCurrentUser();
		if (!group.isGroupAdministrator(caller)) {
			throw new NotAllowedToModifyGroupSignUpsettingsException(group.getName());
		}
		group.setStudentsCanSignUp(command.isAllowStudentsToSignUp());
	}

	// TODO change to Role.STUDENT?
	@Override
	@Secured({ Role.USER })
	public void signUpAsStudent(GroupSignUpCommand command) {
		final StudentGroup group = groupRepository.getExisting(command.getGroupId());
		UserDto caller = authenticationService.getCurrentUser();
		group.signUpStudent(caller);
	}
}
