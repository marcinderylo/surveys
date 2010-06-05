package org.adaptiveplatform.surveys.service;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.application.AuthenticationService;
import org.adaptiveplatform.surveys.domain.StudentGroup;
import org.adaptiveplatform.surveys.dto.UserDto;
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
                StudentGroup group = new StudentGroup(groupName, caller);
                repository.persist(group);
                return group;
        }
}
