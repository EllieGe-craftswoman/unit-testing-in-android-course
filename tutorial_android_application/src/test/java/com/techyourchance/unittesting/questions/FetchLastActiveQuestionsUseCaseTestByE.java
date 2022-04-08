package com.techyourchance.unittesting.questions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentCaptor.*;

import com.techyourchance.unittesting.networking.StackoverflowApi;
import com.techyourchance.unittesting.networking.questions.FetchLastActiveQuestionsEndpoint;
import com.techyourchance.unittesting.networking.questions.QuestionSchema;

import java.util.LinkedList;
import java.util.List;


@RunWith(MockitoJUnitRunner.class)
public class FetchLastActiveQuestionsUseCaseTestByE {

    // region helper fields
    private EndpointTd endpointTd;
    @Mock FetchLastActiveQuestionsUseCase.Listener mListener1;
    @Mock FetchLastActiveQuestionsUseCase.Listener mListener2;

    @Captor ArgumentCaptor<List<Question>> mQuestionsCaptor;
    // endregion helper fields

    FetchLastActiveQuestionsUseCase SUT;

    @Before
    public void setup() {
        endpointTd = new EndpointTd();
        SUT = new FetchLastActiveQuestionsUseCase(endpointTd);

    }

    @Test
    public void fetchLastActiveQuestionsAndNotify_success_listenersNotifiedWithCorrectData() {
        //Arrange
        success();
        SUT.registerListener(mListener1);
        SUT.registerListener(mListener2);
        //Act
        SUT.fetchLastActiveQuestionsAndNotify();
        //Assert
        verify(mListener1).onLastActiveQuestionsFetched(mQuestionsCaptor.capture());
        verify(mListener2).onLastActiveQuestionsFetched(mQuestionsCaptor.capture());
        List<List<Question>> questionsLists = mQuestionsCaptor.getAllValues();
        assertEquals(getExpectedQuestions(), questionsLists.get(0));
        assertEquals(getExpectedQuestions(), questionsLists.get(1));
    }

    @Test
    public void fetchLastActiveQuestionsAndNotify_failure_listenersNotified() {
        //Arrange
        failure();
        SUT.registerListener(mListener1);
        SUT.registerListener(mListener2);
        //Act
        SUT.fetchLastActiveQuestionsAndNotify();
        //Assert
        verify(mListener1).onLastActiveQuestionsFetchFailed();
        verify(mListener2).onLastActiveQuestionsFetchFailed();
    }

    // region helper methods
    private void success() {
        //no op
    }

    private void failure() {
        endpointTd.mFailure = true;
    }

    private List<Question> getExpectedQuestions() {
        List<Question> questions = new LinkedList<>();
        questions.add(new Question("d1","t1"));
        questions.add(new Question("d2","t2"));
        return questions;
    }
    // endregion helper methods

    // region helper helper classes
    private static  class EndpointTd extends FetchLastActiveQuestionsEndpoint {

        public boolean mFailure;

        public EndpointTd() {
            super(null);
        }

        @Override
        public void fetchLastActiveQuestions(Listener listener) {
            if(mFailure){
                listener.onQuestionsFetchFailed();
            } else {
                List<QuestionSchema> questionSchema = new LinkedList<>();
                questionSchema.add(new QuestionSchema("t1","d1", "b1"));
                questionSchema.add(new QuestionSchema("t2","d2", "b2"));
                listener.onQuestionsFetched(questionSchema);
            }
        }
    }
    // endregion helper helper classes

}
