package ru.practise.pet_projects.todolistapp.handlers;

import java.util.Objects;

/**
 * The {@code User} class represents a user in the application with a login, password, and username.
 * It provides methods to access the user's credentials and overrides methods for equality and
 * hashing based on the user's properties.
 */
public class User {
    private final String login;
    private final String password;
    private final String username;

    /**
     * Constructs a new User instance with the specified {@code login}, {@code password}, and {@code username}.
     *
     * @param login    The login identifier for the user.
     * @param password The password for the user.
     * @param username The display name or username for the user.
     */
    public User(String login, String password, String username) {
        this.login = login;
        this.password = password;
        this.username = username;
    }

    /**
     * Returns the {@code password} of the user.
     *
     * @return The user's password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the {@code username} of the user.
     *
     * @return The user's username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Compares this User instance to the specified object for equality.
     * Two User instances are considered equal if their login, password, and username are the same.
     *
     * @param o The object to compare this User against.
     * @return true if the specified object is equal to this User; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(login, user.login) &&
                Objects.equals(password, user.password) &&
                Objects.equals(username, user.username);
    }

    /**
     * Returns a hash code value for this User instance.
     *
     * @return A hash code value for this User.
     */
    @Override
    public int hashCode() {
        return Objects.hash(login, password, username);
    }

    /**
     * Returns a string representation of the User instance.
     *
     * @return A string representation of the User, including login, password, and username.
     */
    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
