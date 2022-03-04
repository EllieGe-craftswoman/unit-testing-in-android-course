package com.techyourchance.testdoublesfundamentals.example6;


import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class FitnessTrackerTest {

    FitnessTracker SUT;

    @Before
    public void setUp(){
        SUT = new FitnessTracker();
    }

    /*Usage of Singletons will cause cross talk between tests
    * and they affect each  other
    * Mr Singleton is the worst enemy of testable code because interdependency
    * occurs with it
    *
    * A workaround was done in the below tests using:
    * int currentSteps = SUT.getTotalSteps();
    * to be able to count the difference between the new and old count
     */
    @Test
    public void step_totalIncremented() {
        int currentSteps = SUT.getTotalSteps();
        SUT.step();
        assertEquals(currentSteps + 1, SUT.getTotalSteps());
    }

    @Test
    public void runStep_totalIncrementedByCorrectRation() {
        int currentSteps = SUT.getTotalSteps();
        SUT.runStep();
        assertEquals(currentSteps + 2, SUT.getTotalSteps());
    }

}