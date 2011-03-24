package org.adaptiveplatform.surveys.application;

import java.util.List;

import org.adaptiveplatform.codegenerator.api.RemoteService;
import org.adaptiveplatform.surveys.dto.StudentGroupDto;
import org.adaptiveplatform.surveys.dto.StudentGroupQuery;


/**
 * 
 * @author Marcin Deryło
 */
@RemoteService
public interface StudentGroupDao {
    
	/**
	 * @return detailed view of a student group 
	 */
    StudentGroupDto getGroup(Long groupId);

    /**
     * @param query
     * @return list of student groups calling user has access to (as group
     *         administrator or evaluator).
     */
    List<StudentGroupDto> query(StudentGroupQuery query);

    /**
     * @return list of groups calling user can join
     */
    List<StudentGroupDto> getAvailableGroups();
}
