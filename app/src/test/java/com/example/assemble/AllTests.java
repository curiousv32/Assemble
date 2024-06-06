package com.example.assemble;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        NoteManagerTest.class,
        NoteTest.class
})
public class AllTests {
}
