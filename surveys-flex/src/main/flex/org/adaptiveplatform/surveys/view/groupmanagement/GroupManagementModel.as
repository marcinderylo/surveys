package org.adaptiveplatform.surveys.view.groupmanagement {
import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.resources.IResourceManager;

import org.adaptiveplatform.surveys.application.BusinessExceptionHandler;
import org.adaptiveplatform.surveys.application.generated.StudentGroupDao;
import org.adaptiveplatform.surveys.application.generated.StudentGroupFacade;
import org.adaptiveplatform.surveys.dto.generated.CreateStudentGroupCommand;
import org.adaptiveplatform.surveys.dto.generated.GroupRoleEnum;
import org.adaptiveplatform.surveys.dto.generated.SetGroupSignUpModeCommand;
import org.adaptiveplatform.surveys.dto.generated.StudentGroupDto;
import org.adaptiveplatform.surveys.dto.generated.StudentGroupQuery;
import org.adaptiveplatform.validation.CommandValidator;

public class GroupManagementModel {

    private var studentGroupFacade:StudentGroupFacade;
    private var groupDao:StudentGroupDao;

    [Bindable]
    public var groups:ArrayCollection;
    [Bindable]
    public var selectedGroup:StudentGroupDto;
    [Bindable]
    public var query:StudentGroupQuery = new StudentGroupQuery();

    [Bindable]
    public var createGroupCommand:CreateStudentGroupCommand;
    [Bindable]
    public var createGroupCommandValidator:CommandValidator = new CommandValidator(true);

    public function GroupManagementModel(studentGroupFacade:StudentGroupFacade, groupDao:StudentGroupDao) {
        this.studentGroupFacade = studentGroupFacade;
        this.groupDao = groupDao;
        createGroupCommand = new CreateStudentGroupCommand();
    }

    public function findGroups():void {
        query.runAs = GroupRoleEnum.GROUP_ADMINISTRATOR;
        groupDao.query(query)//
                .onSuccess(
                function(result:ArrayCollection):void {
                    groups = result;
                }).onFault(BusinessExceptionHandler.displayAlert());
    }

    public function createNewGroup():void {
        if (createGroupCommandValidator.validate()) {
            studentGroupFacade.createGroup(createGroupCommand).onSuccess(
                    function(result:Object):void {
                        createGroupCommand = new CreateStudentGroupCommand();
                        findGroups();
                    }).onFault(BusinessExceptionHandler.displayAlert());
        }
    }

    public function resetGroups():void {
        query = new StudentGroupQuery();
        selectedGroup = null;
        findGroups();
    }

    public function changeSignupMode(signupMode:Boolean):void {
        var command:SetGroupSignUpModeCommand = new SetGroupSignUpModeCommand();
        command.groupId = selectedGroup.id;
        command.allowStudentsToSignUp = signupMode;
        studentGroupFacade.setGroupSignUpMode(command).onSuccess(
                function(...ignore):void {
                    resetGroups();
                }).onFault(BusinessExceptionHandler.displayAlert());
    }

    [PostConstruct]
    public function initialize():void {
        BindingUtils.bindProperty(createGroupCommandValidator, "source", this, ["createGroupCommand"]);
    }
}
}
