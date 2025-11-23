package br.ufrn.library.repository;

import java.util.List;
import java.util.Optional;

import br.ufrn.library.model.User;

public interface UserRepository {

    User save(User user);
    Optional<User> findById(String id);
    List<User> findAll();
    boolean deleteById(String id);
    boolean existsById(String id);
}