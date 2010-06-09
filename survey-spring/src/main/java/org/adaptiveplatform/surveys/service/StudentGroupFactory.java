package org.adaptiveplatform.surveys.service;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.application.AuthenticationService;
import org.adaptiveplatform.surveys.domain.StudentGroup;
import org.adaptiveplatform.surveys.dto.UserDto;
import org.adaptiveplatform.surveys.exception.GroupAlreadyExistsException;
import org.springframework.stereotype.Service;

/**
 *
 * @author Marcin Deryło
 */
@Service("studentGroupFactory")
public class StudentGroupFactory {

    @Resource
    private StudentGroupRepository repository;
    @Resource
    private AuthenticationService authentication;

    public StudentGroup newGroup(String groupName) {
        UserDto caller = authentication.getCurrentUser();

        String trimmedName = trimIfNotNull(groupName);

        checkGroupNameUniqueness(trimmedName);

        StudentGroup group = new StudentGroup(trimmedName, caller);
        repository.persist(group);
        return group;
    }

    private void checkGroupNameUniqueness(String trimmedName) {
        StudentGroup existingGroup = repository.getByName(trimmedName);
        if (existingGroup != null) {
            throw new GroupAlreadyExistsException(trimmedName);
        }
    }

    public static String trimIfNotNull(String input) {
        if (input == null) {
            return null;
        }
        return input.trim();
    }
}
