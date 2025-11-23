package br.ufrn.library.service;

import java.util.List;

import br.ufrn.library.model.User;
import br.ufrn.library.repository.UserRepository;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(String id, String name) {
        if (userRepository.existsById(id)) {
            throw new IllegalArgumentException("User with this ID already exists.");
        }
        User newUser = new User(id, name);
        return userRepository.save(newUser);
    }

    public User findUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
    }

    public List<User> listAllUsers() {
        return userRepository.findAll();
    }
    
    public User updateUser(String id, String newName) {
        User userToUpdate = findUserById(id);
        userToUpdate.setName(newName);
        
        return userRepository.save(userToUpdate);
    }
}