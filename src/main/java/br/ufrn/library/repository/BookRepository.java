package br.ufrn.library.repository;

import java.util.List;
import java.util.Optional;

import br.ufrn.library.model.Book;

public interface BookRepository {
    Book save(Book book);
    Optional<Book> findByIsbn(String isbn);
    List<Book> findAll();
    boolean deleteByIsbn(String isbn);
    boolean existsByIsbn(String isbn); 
}