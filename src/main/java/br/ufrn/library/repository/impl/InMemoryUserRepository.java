package br.ufrn.library.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import br.ufrn.library.model.User;
import br.ufrn.library.repository.UserRepository;

public class InMemoryUserRepository implements UserRepository {

    private static final Map<String, User> database = new ConcurrentHashMap<>();

    @Override
    public User save(User user) {
        database.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(database.values());
    }

    @Override
    public boolean deleteById(String id) {
        return database.remove(id) != null;
    }
    
    @Override
    public boolean existsById(String id) {
        return database.containsKey(id);
    }
}