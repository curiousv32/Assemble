package com.example.assemble;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class AllTests {

    private static final Class[] TEST_CLASSES = {
            NoteManagerTest.class,
            NoteTest.class
    };

    @Test
    public void test_all() {
        Result result = JUnitCore.runClasses(TEST_CLASSES);

        if (result.wasSuccessful()) {
            System.out.println("All tests passed");
        } else {
            System.out.println(result.getFailureCount() + " tests failed");

            for (Failure failure : result.getFailures()) {
                System.out.println(
                        "Test failed: " + failure.getTestHeader() + " - " + failure.getMessage() + ":\n" +
                        failure.getDescription() + "\n" +
                        "Trace: " + failure.getTrace()
                );
            }

            fail();
        }
    }
}
