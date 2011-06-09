package org.adaptiveplatform.surveys.application;

import static org.adaptiveplatform.surveys.test.Asserts.expectException;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.adaptiveplatform.surveys.domain.QuestionTemplate;
import org.adaptiveplatform.surveys.domain.Research;
import org.adaptiveplatform.surveys.domain.StudentGroup;
import org.adaptiveplatform.surveys.domain.SurveyPublication;
import org.adaptiveplatform.surveys.domain.SurveyTemplate;
import org.adaptiveplatform.surveys.dto.AddGroupToResearchCommand;
import org.adaptiveplatform.surveys.dto.PrepareResearchCommand;
import org.adaptiveplatform.surveys.dto.UserDto;
import org.adaptiveplatform.surveys.service.ResearchRepository;
import org.adaptiveplatform.surveys.service.StudentGroupRepository;
import org.adaptiveplatform.surveys.service.SurveyPublicationFactory;
import org.adaptiveplatform.surveys.service.SurveyPublicationRepository;
import org.adaptiveplatform.surveys.service.SurveyTemplateRepository;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;

import com.google.common.collect.Lists;

/**
 * 
 * @author Marcin Deryło
 */
public class ResearchCreationTest {

    @InjectMocks
    private EvaluationFacade facade;
    @Mock
    private SurveyTemplateRepository surveyTemplateRepository;
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private ResearchRepository researchRepository;
    @Mock
    private StudentGroupRepository groupRepository;
    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private SurveyPublicationFactory publicationFactory;
    @Mock
    private SurveyPublicationRepository publicationRepository;

    @Before
    public void init() {
        facade = new EvaluationFacadeImpl();

        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = EntityNotFoundException.class)
    public void cantCreateResearchWithoutSurveyTemplate() throws Exception {
        givenSurveyTemplateDoesNotExist(37L);

        // when
        facade.createResearch(cmd(37L));

        // then
        expectException();
    }

    @Test(expected = AccessDeniedException.class)
    public void cantUseOtherUsersSurveyTemplateForResearch() throws Exception {
        SurveyTemplate template = givenExistingSurveyTemplate(78L);

        given(template.getOwnerId()).willReturn(3576L);
        doThrow(new AccessDeniedException("mock exception")).when(authenticationService).userSecurityCheck(3576L);
        // when
        facade.createResearch(cmd(78L));

        // then
        expectException();

    }

    @Test
    public void shouldCreatePublicationsForGroupsListed() throws Exception {
        // given
        final SurveyTemplate template = givenExistingSurveyTemplate(135L);
        givenSurveyTemplateHasQuestions(template, 2);

        bypassingSecurityCheck();

        PrepareResearchCommand cmd = cmd(135L);
        cmd.setName("test research");
        cmd.setSurveyTemplateId(135L);
        cmd.getGroupsToAdd().add(new AddGroupToResearchCommand(1L));
        cmd.getGroupsToAdd().add(new AddGroupToResearchCommand(3L, new Date(12345L), new Date(56742L)));

        givenGroupExists(1L);
        givenGroupExists(3L);

        // when
        facade.createResearch(cmd);

        // then
        verify(publicationRepository, times(2)).persist(any(SurveyPublication.class));
        verify(researchRepository).persist(argThat(new BaseMatcher<Research>() {

            @Override
            public boolean matches(Object item) {
                Research research = (Research) item;
                return "test research".equals(research.getName()) && research.getQuestionEvaluations().size() == 2
                        && research.getPublications().size() == 2;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Research(test research, 2 questions)");
            }
        }));
    }

    private void bypassingSecurityCheck() {
        doNothing().when(authenticationService).userSecurityCheck(anyLong());
    }

    private SurveyTemplate givenExistingSurveyTemplate(Long id) {
        // given
        SurveyTemplate template = mock(SurveyTemplate.class);
        given(surveyTemplateRepository.getExisting(id)).willReturn(template);
        return template;
    }

    private PrepareResearchCommand cmd(long surveyTemplateId) {
        PrepareResearchCommand cmd = new PrepareResearchCommand();
        cmd.setSurveyTemplateId(surveyTemplateId);
        return cmd;
    }

    private void givenSurveyTemplateDoesNotExist(Long id) {
        // given
        given(surveyTemplateRepository.getExisting(id)).willThrow(new EntityNotFoundException());
    }

    private void givenSurveyTemplateHasQuestions(SurveyTemplate template, int questionsCount) {
        List<QuestionTemplate> questions = Lists.newArrayList();
        for (int i = 0; i < questionsCount; i++) {
            QuestionTemplate question = mock(QuestionTemplate.class);
            questions.add(question);
        }
        given(template.getQuestions()).willReturn(Lists.newArrayList(questions));
    }

    private void givenGroupExists(Long id) {
        StudentGroup group = mock(StudentGroup.class);
        given(group.getId()).willReturn(id);
        given(group.isAssignedAsEvaluator(any(UserDto.class))).willReturn(true);
        given(groupRepository.getExisting(id)).willReturn(group);
    }
}
