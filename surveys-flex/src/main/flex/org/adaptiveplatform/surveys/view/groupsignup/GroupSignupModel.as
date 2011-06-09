package org.adaptiveplatform.surveys.view.groupsignup {
import mx.collections.ArrayCollection;
import mx.resources.IResourceManager;

import org.adaptiveplatform.surveys.application.BusinessExceptionHandler;
import org.adaptiveplatform.surveys.application.generated.StudentGroupDao;
import org.adaptiveplatform.surveys.application.generated.StudentGroupFacade;
import org.adaptiveplatform.surveys.dto.generated.GroupSignUpCommand;
import org.adaptiveplatform.surveys.dto.generated.StudentGroupDto;

public class GroupSignupModel {

    private var studentGroupFacade:StudentGroupFacade;
    private var groupDao:StudentGroupDao;
    // REMOVE ME
    private var resourceManager:IResourceManager;

    [Bindable]
    public var groups:ArrayCollection;

    public function GroupSignupModel(studentGroupFacade:StudentGroupFacade, groupDao:StudentGroupDao, resourceManager:IResourceManager) {
        this.studentGroupFacade=studentGroupFacade;
        this.groupDao=groupDao;
        this.resourceManager=resourceManager;
    }

    public function findGroups():void {
        groupDao.getAvailableGroups() //
            .onSuccess(function(result:ArrayCollection):void {
                groups=result;
            }).onFault(BusinessExceptionHandler.displayAlert());
    }

    public function signUp(group:StudentGroupDto):void {
        if (group != null) {
            var command:GroupSignUpCommand=new GroupSignUpCommand();
            command.groupId=group.id;
            studentGroupFacade.signUpAsStudent(command).onSuccess(function():void {
            }).onFault(BusinessExceptionHandler.displayAlert());
        }
    }
}
}