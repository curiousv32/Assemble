package com.example.assemble;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        NoteManagerTestWithSQLDB.class,
        NoteManagerTestWithStubDB.class,
        NoteTest.class,
        FlashCardManagerTest.class,
        UserManagerTestWithSQLDB.class,
        UserManagerTestWithStubDB.class
})
public class AllTests {
}
