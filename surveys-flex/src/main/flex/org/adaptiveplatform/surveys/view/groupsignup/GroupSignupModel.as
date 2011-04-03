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
    // REMOVEME
    private var resourceManager:IResourceManager;

    [Bindable]
    public var groups:ArrayCollection;
    [Bindable]
    public var selectedGroup:StudentGroupDto;

    public function GroupSignupModel(studentGroupFacade:StudentGroupFacade, groupDao:StudentGroupDao, resourceManager:IResourceManager) {
        this.studentGroupFacade=studentGroupFacade;
        this.groupDao=groupDao;
        this.resourceManager=resourceManager;
    }

    public function findGroups():void {
        groupDao.getAvailableGroups() //
            .onSuccess(function(result:ArrayCollection):void {
                selectedGroup=null;
                groups=result;
            }).onFault(BusinessExceptionHandler.displayAlert(resourceManager));
    }

    public function signUpToSelectedGroup():void {
        if (selectedGroup != null) {
            var command:GroupSignUpCommand=new GroupSignUpCommand();
            command.groupId=selectedGroup.id;
            studentGroupFacade.signUpAsStudent(command).onSuccess(function():void {
            }).onFault(BusinessExceptionHandler.displayAlert(resourceManager));
        }
    }
}
}