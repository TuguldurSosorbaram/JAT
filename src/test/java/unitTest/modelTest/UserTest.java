package unitTest.modelTest;

import model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the User class, focusing on password hashing, validation, and constructor behavior.
 */
class UserTest {

    /**
     * Tests that the password hashing method produces a non-null and different value from the plain password.
     */
    @Test
    void testHashPassword() {
        String plainPassword = "securePassword123";
        String hashedPassword = User.hashPassword(plainPassword);

        // Verify the hashed password is not null and not equal to the plain password
        assertNotNull(hashedPassword, "Hashed password should not be null");
        assertNotEquals(plainPassword, hashedPassword, "Hashed password should not match the plain password");
    }

    /**
     * Tests that the password validation method correctly validates a valid password against its hashed equivalent.
     */
    @Test
    void testCheckPasswordValid() {
        String plainPassword = "securePassword123";
        String hashedPassword = User.hashPassword(plainPassword);

        // Verify that the hashed password matches the plain password
        assertTrue(User.checkPassword(plainPassword, hashedPassword), "The plain password should match the hashed password");
    }

    /**
     * Tests that the password validation method correctly rejects an invalid password.
     */
    @Test
    void testCheckPasswordInvalid() {
        String plainPassword = "securePassword123";
        String wrongPassword = "wrongPassword456";
        String hashedPassword = User.hashPassword(plainPassword);

        // Verify that a wrong password does not match the hashed password
        assertFalse(User.checkPassword(wrongPassword, hashedPassword), "The wrong password should not match the hashed password");
    }

    /**
     * Tests that the User constructor correctly hashes the password and sets the username.
     */
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
