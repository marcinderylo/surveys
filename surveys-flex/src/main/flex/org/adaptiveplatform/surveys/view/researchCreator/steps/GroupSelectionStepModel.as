package org.adaptiveplatform.surveys.view.researchCreator.steps {
import mx.collections.ArrayCollection;
import mx.events.PropertyChangeEvent;

import org.adaptiveplatform.surveys.application.generated.StudentGroupDao;
import org.adaptiveplatform.surveys.dto.generated.GroupRoleEnum;
import org.adaptiveplatform.surveys.dto.generated.StudentGroupDto;
import org.adaptiveplatform.surveys.dto.generated.StudentGroupQuery;
import org.adaptiveplatform.surveys.view.researchCreator.ResearchCreatorStepModel;

[Bindable]
public class GroupSelectionStepModel implements ResearchCreatorStepModel {

    private const STEP_NUMBER:int = 2;
    public var valid:Boolean;

    private var groupDao:StudentGroupDao;

    public var studentGroupQuery:StudentGroupQuery;
    public var groupsList:ArrayCollection;
    public var selectedGroups:ArrayCollection = new ArrayCollection();

    public function GroupSelectionStepModel(groupDao:StudentGroupDao) {
        this.groupDao = groupDao;
        this.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, propertyChanged);
    }

    public function initialize():void {
        resetGroups();
    }

    private function propertyChanged(event:PropertyChangeEvent):void {
        valid = selectedGroups.length > 0;
    }

    public function findGroups():void {
        studentGroupQuery.runAs = GroupRoleEnum.EVALUATOR;
        groupDao.query(studentGroupQuery)//
                .onSuccess(function (result:ArrayCollection):void {
                    for each (var o:Object in result) {
                        groupsList.addItem({groupName:o.groupName, selected:false, data:o});
                    }
                });
    }

    public function resetGroups():void {
        studentGroupQuery = new StudentGroupQuery();
        groupsList = new ArrayCollection();
        findGroups();
    }

    public function selectGroup(group:StudentGroupDto, select:Boolean):void {
        var containsGroup:Boolean = selectedGroups.contains(group);
        if (select && !containsGroup) {
            selectedGroups.addItem(group);
        } else if (!select && containsGroup) {
            selectedGroups.removeItemAt(selectedGroups.getItemIndex(group));
        }
    }

    public function get stepNumber():int {
        return STEP_NUMBER;
    }
}
}


