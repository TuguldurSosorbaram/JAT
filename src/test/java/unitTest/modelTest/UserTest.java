package unitTest.modelTest;

import model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testHashPassword() {
        String plainPassword = "securePassword123";
        String hashedPassword = User.hashPassword(plainPassword);

        // Verify the hashed password is not null and not equal to the plain password
        assertNotNull(hashedPassword, "Hashed password should not be null");
        assertNotEquals(plainPassword, hashedPassword, "Hashed password should not match the plain password");
    }

    @Test
    void testCheckPasswordValid() {
        String plainPassword = "securePassword123";
        String hashedPassword = User.hashPassword(plainPassword);

        // Verify that the hashed password matches the plain password
        assertTrue(User.checkPassword(plainPassword, hashedPassword), "The plain password should match the hashed password");
    }

    @Test
    void testCheckPasswordInvalid() {
        String plainPassword = "securePassword123";
        String wrongPassword = "wrongPassword456";
        String hashedPassword = User.hashPassword(plainPassword);

        // Verify that a wrong password does not match the hashed password
        assertFalse(User.checkPassword(wrongPassword, hashedPassword), "The wrong password should not match the hashed password");
    }

    @Test
    void testUserConstructorHashing() {
        String plainPassword = "securePassword123";
        User user = new User("testUser", plainPassword);

        // Verify the username is correctly set
        assertEquals("testUser", user.getUsername(), "Username should match the input value");

        // Verify the hashed password is not null and not equal to the plain password
        assertNotNull(user.getHashedPassword(), "Hashed password should not be null");
        assertNotEquals(plainPassword, user.getHashedPassword(), "Hashed password should not match the plain password");

        // Verify that the hashed password matches the plain password
        assertTrue(User.checkPassword(plainPassword, user.getHashedPassword()), "The hashed password should match the plain password");
    }
}
