/**
 * Created with IntelliJ IDEA.
 * User: rafal
 * Date: 4/29/12
 * Time: 6:08 PM
 * To change this template use File | Settings | File Templates.
 */
package org.adaptiveplatform.surveys.view.researchCreator {
public interface ResearchCreatorStepModel {

    function get valid():Boolean;

    function get stepNumber():int;

    function initialize():void;
}
}
