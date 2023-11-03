package Test;

import org.junit.Test;
import static org.junit.Assert.*;
import Objects.Simple;

// Reference for testing user input: https://www.baeldung.com/java-junit-testing-system-in#:~:text=We%20can%20utilize%20System.in,simulate%20user%20input%20during%20testing.&text=Inside%20the%20method%2C%20we%20create,the%20input%20for%20System.in.
public class DSMainTest {
    // JUnit test for object creation
    @Test
    public void testCreateSimple() {
        // Create Simple object
        Simple simple = new Simple(1, 2.1, 'c');
        // Check that object is not null
        assertNotNull(simple);

    }
}
