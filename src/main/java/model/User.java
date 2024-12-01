package model;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Represents a user with a username and a hashed password.
 * Provides methods for password hashing and verification.
 */
public class User {
    private int id;
    private String username;
    private String hashedPassword;

    /**
     * Constructor to create a User with a username and a plain-text password.
     * The password is automatically hashed during construction.
     */
    public User(String username, String plainPassword) {
        this.username = username;
        this.hashedPassword = hashPassword(plainPassword);
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    /**
     * Hashes a plain-text password using BCrypt.
     *
     * @param plainPassword the plain-text password to hash
     * @return the hashed password
     */
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12)); // 12 rounds for secure hashing
    }

    /**
     * Verifies if a plain-text password matches the hashed password.
     *
     * @param plainPassword  the plain-text password to verify
     * @param hashedPassword the hashed password to compare against
     * @return true if the password matches, false otherwise
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
