package model;
import org.mindrot.jbcrypt.BCrypt;

public class User {
    private int id;
    private String username;
    private String hashedPassword;

    // Constructor
    public User(String username, String plainPassword) {
        this.username = username;
        this.hashedPassword = hashPassword(plainPassword); // Automatically hash password
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

    // Static method to hash a password
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12)); // 12 rounds for hashing
    }

    // Static method to verify a password
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
