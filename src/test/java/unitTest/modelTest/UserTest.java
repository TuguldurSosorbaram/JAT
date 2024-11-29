package unitTest.modelTest;

import model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testConstructor() {
        // Arrange
        String username = "testUser";
        String password = "testPassword";

        // Act
        User user = new User(username, password);

        // Assert
        assertEquals(username, user.getUsername());
        assertEquals(password, user.getPassword());
    }
    @Test
    void testGetters() {
        // Arrange
        String username = "exampleUser";
        String password = "examplePass";
        User user = new User(username, password);

        // Act & Assert
        assertEquals("exampleUser", user.getUsername());
        assertEquals("examplePass", user.getPassword());
    }
}
