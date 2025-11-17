package br.ufrn.library.repository;

import java.util.List;
import java.util.Optional;

import br.ufrn.library.model.Loan;

/**
 * O Contrato (Interface) para operações de persistência de Empréstimos.
 * Define quais operações podem ser realizadas nos dados de Empréstimo,
 * sem especificar como elas são implementadas.
 */
public interface LoanRepository {

    /**
     * Salva um novo empréstimo ou atualiza um existente.
     * 
     * @param loan O objeto de empréstimo a ser salvo
     * @return O empréstimo salvo
     */
    Loan save(Loan loan);

    /**
     * Busca um empréstimo pelo seu ID único.
     * 
     * @param id O identificador único do empréstimo
     * @return Um Optional contendo o Loan se encontrado, ou Optional.empty() se não
     */
    Optional<Loan> findById(String id);

    /**
     * Recupera todos os empréstimos no sistema.
     * 
     * @return Uma lista de todos os empréstimos
     */
    List<Loan> findAll();

    /**
     * Busca todos os empréstimos de um usuário específico.
     * 
     * @param userId O identificador único do usuário
     * @return Uma lista de empréstimos do usuário especificado
     */
    List<Loan> findByUserId(String userId);

    /**
     * Busca todos os empréstimos de um livro específico.
     * 
     * @param isbn O ISBN do livro
     * @return Uma lista de empréstimos do livro especificado
     */
    List<Loan> findByBookIsbn(String isbn);

    /**
     * Busca todos os empréstimos ativos (não devolvidos) de um usuário específico.
     * 
     * @param userId O identificador único do usuário
     * @return Uma lista de empréstimos ativos do usuário especificado
     */
    List<Loan> findActiveByUserId(String userId);

    /**
     * Busca todos os empréstimos ativos (não devolvidos) no sistema.
     * 
     * @return Uma lista de todos os empréstimos ativos
     */
    List<Loan> findAllActive();

    /**
     * Remove um empréstimo pelo seu ID único.
     * 
     * @param id O identificador único do empréstimo a ser removido
     * @return true se o empréstimo foi encontrado e removido, false caso contrário
     */
    boolean deleteById(String id);

    /**
     * Verifica se um empréstimo existe pelo seu ID.
     * 
     * @param id O identificador único do empréstimo
     * @return true se um empréstimo com este ID existe, false caso contrário
     */
    boolean existsById(String id);
}
