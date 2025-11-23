package br.ufrn.library.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import br.ufrn.library.model.Loan;
import br.ufrn.library.repository.LoanRepository;

public class InMemoryLoanRepository implements LoanRepository {

    private static final Map<String, Loan> database = new ConcurrentHashMap<>();

    @Override
    public Loan save(Loan loan) {
        if (loan == null) {
            throw new IllegalArgumentException("Loan cannot be null.");
        }
        database.put(loan.getId(), loan);
        return loan;
    }

    @Override
    public Optional<Loan> findById(String id) {
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public List<Loan> findAll() {
        return new ArrayList<>(database.values());
    }

    @Override
    public List<Loan> findByUserId(String userId) {
        return database.values().stream()
                .filter(loan -> loan.getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Loan> findByBookIsbn(String isbn) {
        return database.values().stream()
                .filter(loan -> loan.getBook().getIsbn().equals(isbn))
                .collect(Collectors.toList());
    }

    @Override
    public List<Loan> findActiveByUserId(String userId) {
        return database.values().stream()
                .filter(loan -> loan.getUser().getId().equals(userId) && !loan.isReturned())
                .collect(Collectors.toList());
    }

    @Override
    public List<Loan> findAllActive() {
        return database.values().stream()
                .filter(loan -> !loan.isReturned())
                .collect(Collectors.toList());
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