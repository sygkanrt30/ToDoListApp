package ru.practise.pet_projects.todolistapp.handlers;


/**
 * The {@code User} class represents a user in the application with a login, password, and username.
 * It provides methods to access the user's credentials and overrides methods for equality and
 * hashing based on the user's properties.
 */
public record User(String login, String password, String username) {
}
