package org.adaptiveplatform.surveys.acceptance;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

@ContextConfiguration(locations = "classpath:/testConfigurationContext.xml")
public class SurveyAcceptanceTest extends AbstractTestNGSpringContextTests {
	// Evaluation Dao
	// - shouldFindResearchesByName
	// - shouldFindResearchesByTemplateId
	// - shouldListResearchesOfCurrentUserWithNoRestrictions
	// - shouldListResearchesOfCurrentUserWhenUsingNullQueryObject
	// - shouldGetResearchWithDetailsById
	// - shouldNotAllowGettingResearchesOfOtherUsers
	// - shouldListLatestEvaluationActivities
	// Evaluation facade should
	// - beAbleToSetACommentForQuestionInResearch
	// - beAbleToRememberUsedSearchPhrasesPerQuestion
	// EvaluationSystemTest
	// - shouldAllowToSetEvaluatorCommentsOnQuestions
	// - shouldFetchSubmittedSurveysForResearch
	// - shouldAllowToTagAnsweredQuestions
	// - shouldAllowEvaluatorToChangeTags
	// GivenResearchWithPublishedTemplateEvaluatorShould
	// - beAbleToFetchSubmittedSurveys
	// - notBeAbleToFetchStartedButNotYetSubmittedSurveys
	// - beAbleToFetchGroupNamesInResearch
	// HavingANewSurveyTemplateWithQuestionsEvaluatorShould
	// - beAbleToReadIt
	// - beAbleToQueryHisTemplatesWithoutReceivingDetails
	// - beAbleToRemoveIt
	// - notBeAbleToPublishItInAGroupHeDoesntBelongTo
	// - notBeAbleToCreateAnotherSurveyWithSameTitle
	// - beAbleToUpdateItLeavingNameUnchanged
	// - beAbleToUpdateChangingItsName
	// - notBeAbleToChangeTheNameWhenAnotherTemplateWithSuchNameExists
	// HavingAPublishedSurveyTemplateEvaluatorShould
	// - beAbleToQueryHisPublications
	// - beAbleToRemoveThePublication
	// - notBeAbleToRemoveSurveyTemplate
	// PublishedSurveysQueryingTest
	// - evaluatorShouldBeAbleToQueryPublishedTemplatesFromASingleGroup
	// - evaluatorShouldBeAbleToQueryPublishedTemplatesFromMultipleGroups
	// - evaluatorShouldBeAbleToQueryPublishedTemplatesFromAllHisGroups
	// - studentShouldBeAbleToQueryPublishedTemplatesFromASingleGroup
	// - studentShouldBeAbleToQueryPublishedTemplatesFromMultipleGroups
	// - studentShouldBeAbleToQueryPublishedTemplatesFromAllGroups
	// - studentShouldBeAbleToQueryPublishedTemplatesByGroupName
	// - studentShouldBeAbleToQueryPublishedTemplatesByTemplateName
	// PublishedSurveyTemplateStatusTest - havingTwoPublicationsWithSameGroupAndSurveyTemplate
	// - shouldCalculatePublishedSurveyTemplateStatusForConcretePublication
	// ResearchCreationTest
	// - cantCreateResearchWithoutSurveyTemplate
	// - cantUseOtherUsersSurveyTemplateForResearch
	// - shouldCreatePublicationsForGroupsListed
	// StudentGroupsSystemTest 
	// - shouldTeacherCreateUserGroup
	// - cantAllowNonTeacherToCreateGroup
	// - shouldAddGroupMembersUponCreation
	// - shouldRemoveGroupMembers
	// - cantTeacherRemoveHimselfFromTheGroup
	// - shouldAddGroupMembersAfterCreation
	// - shouldAllowOnlyGroupAdminsToSeeItsDetails
	// - shouldEvaluatorReadHisGroups
	// - shouldTeacherReadHisGroups
	// - shouldReadGroupsSortedByName
	// - cantAllowTeacherToReadGroupsHeIsNotAssignedTo
	// - cantAllowEvaluatorToReadGroupsHeIsNotAssignedTo
	// - shouldTeacherReadGroupsUnanonymized
	// - cantTeacherReadGroupsAsEvaluator
	// - shouldAdminRemoveGroupWithoutSurveyTemplatesAssigned
	// - cantRemoveGroupWithSurveyTemplatesAssigned
	// - cantRemoveTemplateFromGroupIfItHasBeenFilledByStudents
	// - shouldQueryGroupsByName
	// - cantCreateGroupWithPureWhitespaceName
	// - cantTeacherCreateGroupWithSameNameAsExistingOne
	// - shouldBeAbleToOpenGroupForStudentToSignUpThemselves
	// SurveyDaoTest
	// - shouldStudentReadHisFilledSurvey
	// - shouldStudentSeeSurveyTemplateDescription
	// - shouldEvaluatorReadFilledSurveyForHisPublication
	// - cantReadFilledSurveyIfNotOwnerNorEvaluatorWhoCreatedThePublication
	// - shouldEvaluatorReadFilledSurveysForPublishedTemplate
	// - surveyShouldBeAvailableForStudent
	// - shouldStudentSeeOnlyPublicationsFromHisGroups
	// - shouldStudentSeeStartedButNotYetSubmittedSurveys
	// SurveyTemplateCreationTest
	// - evaluatorShouldCreateSurveyTemplate
	// - evaluatorShouldBeAbleToProvideDescriptionForSurveyTemplate
}
