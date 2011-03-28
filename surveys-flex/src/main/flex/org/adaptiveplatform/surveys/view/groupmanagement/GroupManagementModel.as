package org.adaptiveplatform.surveys.view.groupmanagement {
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
import org.adaptiveplatform.surveys.dto.generated.StudentGroupDto;
import org.adaptiveplatform.surveys.dto.generated.StudentGroupQuery;

public class GroupManagementModel {

    private var studentGroupFacade:StudentGroupFacade;
    private var groupDao:StudentGroupDao;
    private var resourceManager:IResourceManager;

    [Bindable]
    public var newGroupName:String;
    [Bindable]
    public var newGroupNameErrorString:String;

    [Bindable]
    public var groups:ArrayCollection;
    [Bindable]
    public var selectedGroup:StudentGroupDto;
    [Bindable]
    public var query:StudentGroupQuery=new StudentGroupQuery();

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
        var groupNameValidator:StringValidator=new StringValidator();
        groupNameValidator.required=true;
        groupNameValidator.minLength=5;
        var event:ValidationResultEvent=groupNameValidator.validate(newGroupName, true);

        for each (var result:ValidationResult in event.results) {
            if (result.isError) {
                return;
            }
        }

        var createGroupCommand:CreateStudentGroupCommand=new CreateStudentGroupCommand();
        createGroupCommand.groupName=newGroupName;
        createGroupCommand.addMemberCommands=new ArrayCollection();
        studentGroupFacade.createGroup(createGroupCommand) //
            .onSuccess(function(result:Object):void {
                newGroupName="";
                newGroupNameErrorString="";
                findGroups();
            }).onFault(BusinessExceptionHandler.displayAlert(resourceManager));
    }

    public function removeGroup():void {
        if (selectedGroup != null) {
            studentGroupFacade.removeGroup(selectedGroup.id) //
                .onSuccess(function(result:Object):void {
                    findGroups();
                }).onFault(BusinessExceptionHandler.displayAlert(resourceManager));
        }
    }

    public function resetGroup():void {
        groups=new ArrayCollection();
        query=new StudentGroupQuery();
        findGroups();
    }
}
}
