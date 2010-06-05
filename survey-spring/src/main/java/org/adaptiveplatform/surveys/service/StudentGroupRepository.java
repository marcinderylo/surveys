package org.adaptiveplatform.surveys.service;

import org.adaptiveplatform.surveys.domain.StudentGroup;

/**
 *
 * @author Marcin Deryło
 */
public interface StudentGroupRepository {

        void persist(StudentGroup group);

        StudentGroup get(Long groupId);

        void remove(Long groupId);

    StudentGroup getExisting(Long groupId);
}
