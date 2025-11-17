package br.ufrn.library.repository.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import br.ufrn.library.model.Loan;
import br.ufrn.library.repository.LoanRepository;

/**
 * Implementação em memória do LoanRepository.
 * Esta classe lida com o como salvar dados de empréstimo, usando um Map em
 * memória.
 * Implementa o contrato definido na interface LoanRepository.
 */
public class InMemoryLoanRepository implements LoanRepository {

    // Armazenamento: Chave = ID do empréstimo, Valor = objeto Loan
    private final Map<String, Loan> loans = new HashMap<>();

    @Override
    public Loan save(Loan loan) {
        if (loan == null) {
            throw new IllegalArgumentException("Empréstimo não pode ser nulo.");
        }
        loans.put(loan.getId(), loan);
        return loan;
    }

    @Override
    public Optional<Loan> findById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(loans.get(id));
    }

    @Override
    public List<Loan> findAll() {
        return new ArrayList<>(loans.values());
    }

    @Override
    public List<Loan> findByUserId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return loans.values().stream()
                .filter(loan -> loan.getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Loan> findByBookIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return loans.values().stream()
                .filter(loan -> loan.getBook().getIsbn().equals(isbn))
                .collect(Collectors.toList());
    }

    @Override
    public List<Loan> findActiveByUserId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return loans.values().stream()
                .filter(loan -> loan.getUser().getId().equals(userId) && !loan.isReturned())
                .collect(Collectors.toList());
    }

    @Override
    public List<Loan> findAllActive() {
        return loans.values().stream()
                .filter(loan -> !loan.isReturned())
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        return loans.remove(id) != null;
    }

    @Override
    public boolean existsById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        return loans.containsKey(id);
    }
}
