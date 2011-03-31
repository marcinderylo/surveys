package org.adaptiveplatform.surveys.view.groupmanagement {
import flash.utils.Dictionary;

import mx.collections.ArrayCollection;
import mx.events.ValidationResultEvent;
import mx.resources.IResourceManager;
import mx.resources.ResourceManager;
import mx.validators.StringValidator;
import mx.validators.ValidationResult;

import org.adaptiveplatform.surveys.application.BusinessExceptionHandler;
import org.adaptiveplatform.surveys.application.generated.StudentGroupDao;
import org.adaptiveplatform.surveys.application.generated.StudentGroupFacade;
import org.adaptiveplatform.surveys.dto.generated.CreateStudentGroupCommand;
import org.adaptiveplatform.surveys.dto.generated.GroupRoleEnum;
import org.adaptiveplatform.surveys.dto.generated.SetGroupSignUpModeCommand;
import org.adaptiveplatform.surveys.dto.generated.StudentGroupDto;
import org.adaptiveplatform.surveys.dto.generated.StudentGroupQuery;
import org.adaptiveplatform.surveys.utils.CollectionUtils;

public class GroupManagementModel {

    private var studentGroupFacade:StudentGroupFacade;
    private var groupDao:StudentGroupDao;
    private var resourceManager:IResourceManager;

    [Bindable]
    public var groups:ArrayCollection;
    [Bindable]
    public var selectedGroup:StudentGroupDto;
    [Bindable]
    public var query:StudentGroupQuery=new StudentGroupQuery();
    [Bindable]
    public var createGroupCommand:CreateStudentGroupCommand=new CreateStudentGroupCommand();

    public function GroupManagementModel(studentGroupFacade:StudentGroupFacade, groupDao:StudentGroupDao, resourceManager:IResourceManager) {
        this.studentGroupFacade=studentGroupFacade;
        this.groupDao=groupDao;
        this.resourceManager=resourceManager;
    }

    public function findGroups():void {
        query.runAs=GroupRoleEnum.GROUP_ADMINISTRATOR;
        groupDao.query(query) //
            .onSuccess(function(result:ArrayCollection):void {
                groups=result;
            }).onFault(BusinessExceptionHandler.displayAlert(resourceManager));
    }

    public function addGroup():void {
        createGroupCommand.validateProperties();
        if (createGroupCommand.valid) {
            studentGroupFacade.createGroup(createGroupCommand) //
                .onSuccess(function(result:Object):void {
                    createGroupCommand=new CreateStudentGroupCommand();
                    findGroups();
                }).onFault(BusinessExceptionHandler.displayAlert(resourceManager));
        }
    }

    public function resetGroups():void {
        query=new StudentGroupQuery();
        selectedGroup=null;
        findGroups();
    }

    public function changeSignupMode(signupMode:Boolean):void {
        var command:SetGroupSignUpModeCommand=new SetGroupSignUpModeCommand();
        command.groupId=selectedGroup.id;
        command.allowStudentsToSignUp=signupMode;
        studentGroupFacade.setGroupSignUpMode(command).onSuccess(function(ignore:*):void {
            resetGroups();
        }).onFault(BusinessExceptionHandler.displayAlert(resourceManager));
    }
}
}
