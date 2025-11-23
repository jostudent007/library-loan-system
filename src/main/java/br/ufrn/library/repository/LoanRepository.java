package br.ufrn.library.repository;

import java.util.List;
import java.util.Optional;
import br.ufrn.library.model.Loan;

public interface LoanRepository {
    Loan save(Loan loan);
    Optional<Loan> findById(String id);
    List<Loan> findAll();
    List<Loan> findByUserId(String userId);
    List<Loan> findByBookIsbn(String isbn);
    List<Loan> findActiveByUserId(String userId);
    List<Loan> findAllActive();
    boolean deleteById(String id);
    boolean existsById(String id);
}