package com.andrew.user;

public class UserService {

    private UserDAO dao = new UserDAO();

    public User register(String username, String password) throws Exception {

        if(username == null || username.isBlank())
            throw new IllegalArgumentException("Username required");

        if(password == null || password.length() < 8)
            throw new IllegalArgumentException("Password too short");

        User existing = dao.getUserByUsername(username);

        if(existing != null)
            throw new IllegalArgumentException("Username already exists");

        return dao.createUser(username, password);
    }

    public User login(String username, String password) throws Exception {

        User user = dao.getUserByUsername(username);

        if(user == null)
            throw new RuntimeException("User not found");

        if(!user.getPassword().equals(password))
            throw new RuntimeException("Invalid password");

        return user;
    }

}
