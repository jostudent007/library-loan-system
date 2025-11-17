package br.ufrn.library.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import br.ufrn.library.exception.BookNotFoundException;
import br.ufrn.library.exception.NoCopiesAvailableException;
import br.ufrn.library.exception.UserNotFoundException;
import br.ufrn.library.model.Book;
import br.ufrn.library.model.Loan;
import br.ufrn.library.model.User;
import br.ufrn.library.repository.BookRepository;
import br.ufrn.library.repository.LoanRepository;
import br.ufrn.library.repository.UserRepository;

/**
 * Camada de serviço para operações de Empréstimo.
 * Esta classe contém toda a lógica de negócio relacionada a empréstimos.
 * Depende das interfaces LoanRepository, BookRepository e UserRepository.
 */
public class LoanService {

    private static final int DEFAULT_LOAN_PERIOD_DAYS = 14; // Período padrão de empréstimo: 14 dias

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    /**
     * Construtor com injeção de dependências.
     * 
     * @param loanRepository O repositório de empréstimos
     * @param bookRepository O repositório de livros
     * @param userRepository O repositório de usuários
     */
    public LoanService(LoanRepository loanRepository, BookRepository bookRepository, UserRepository userRepository) {
        if (loanRepository == null) {
            throw new IllegalArgumentException("LoanRepository não pode ser nulo.");
        }
        if (bookRepository == null) {
            throw new IllegalArgumentException("BookRepository não pode ser nulo.");
        }
        if (userRepository == null) {
            throw new IllegalArgumentException("UserRepository não pode ser nulo.");
        }

        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    /**
     * Cria um novo empréstimo de um livro para um usuário.
     * 
     * @param userId O ID do usuário que está pegando o livro emprestado
     * @param isbn   O ISBN do livro a ser emprestado
     * @return O objeto Loan criado
     * @throws UserNotFoundException      Se o usuário não for encontrado
     * @throws BookNotFoundException      Se o livro não for encontrado
     * @throws NoCopiesAvailableException Se não houver cópias disponíveis para
     *                                    empréstimo
     */
    public Loan createLoan(String userId, String isbn) {
        return createLoan(userId, isbn, LocalDate.now(), DEFAULT_LOAN_PERIOD_DAYS);
    }

    /**
     * Cria um novo empréstimo com data e período específicos.
     * 
     * @param userId         O ID do usuário que está pegando o livro emprestado
     * @param isbn           O ISBN do livro a ser emprestado
     * @param loanDate       A data do empréstimo
     * @param loanPeriodDays O período do empréstimo em dias
     * @return O objeto Loan criado
     * @throws UserNotFoundException      Se o usuário não for encontrado
     * @throws BookNotFoundException      Se o livro não for encontrado
     * @throws NoCopiesAvailableException Se não houver cópias disponíveis para
     *                                    empréstimo
     */
    public Loan createLoan(String userId, String isbn, LocalDate loanDate, int loanPeriodDays) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID do usuário não pode ser nulo ou vazio.");
        }
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN não pode ser nulo ou vazio.");
        }
        if (loanDate == null) {
            throw new IllegalArgumentException("Data do empréstimo não pode ser nula.");
        }
        if (loanPeriodDays <= 0) {
            throw new IllegalArgumentException("Período do empréstimo deve ser positivo.");
        }

        // 1. Verifica se o usuário existe
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com ID: " + userId));

        // 2. Verifica se o livro existe
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException("Livro não encontrado com ISBN: " + isbn));

        // 3. Verifica se o livro está disponível para empréstimo
        if (!book.isAvailableForLoan()) {
            throw new NoCopiesAvailableException("Nenhuma cópia disponível para o livro: " + book.getTitle());
        }

        // 4. Calcula a data de devolução
        LocalDate dueDate = loanDate.plusDays(loanPeriodDays);

        // 5. Cria o empréstimo
        String loanId = generateLoanId();
        Loan loan = new Loan(loanId, user, book, loanDate, dueDate);

        // 6. Registra o empréstimo no livro
        book.registerLoan();

        // 7. Salva o estado do livro (cópias disponíveis atualizadas)
        bookRepository.save(book);

        // 8. Salva o empréstimo
        loanRepository.save(loan);

        // 9. Adiciona o empréstimo ao histórico do usuário
        user.addLoanToHistory(loan);
        userRepository.save(user);

        return loan;
    }

    /**
     * Processa a devolução de um livro emprestado.
     * 
     * @param loanId O ID do empréstimo a ser devolvido
     * @return O objeto Loan atualizado
     * @throws IllegalArgumentException Se o empréstimo não for encontrado ou já foi
     *                                  devolvido
     */
    public Loan returnLoan(String loanId) {
        return returnLoan(loanId, LocalDate.now());
    }

    /**
     * Processa a devolução de um livro emprestado com uma data específica.
     * 
     * @param loanId     O ID do empréstimo a ser devolvido
     * @param returnDate A data da devolução
     * @return O objeto Loan atualizado
     * @throws IllegalArgumentException Se o empréstimo não for encontrado ou já foi
     *                                  devolvido
     */
    public Loan returnLoan(String loanId, LocalDate returnDate) {
        if (loanId == null || loanId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID do empréstimo não pode ser nulo ou vazio.");
        }
        if (returnDate == null) {
            throw new IllegalArgumentException("Data de devolução não pode ser nula.");
        }

        // 1. Busca o empréstimo
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Empréstimo não encontrado com ID: " + loanId));

        // 2. Verifica se o empréstimo já foi devolvido
        if (loan.isReturned()) {
            throw new IllegalStateException("O empréstimo já foi devolvido.");
        }

        // 3. Marca o empréstimo como devolvido
        loan.markAsReturned(returnDate);

        // 4. Registra a devolução no livro
        Book book = loan.getBook();
        book.registerReturn();

        // 5. Salva o estado atualizado do livro
        bookRepository.save(book);

        // 6. Salva o empréstimo atualizado
        loanRepository.save(loan);

        return loan;
    }

    /**
     * Busca um empréstimo pelo seu ID.
     * 
     * @param loanId O ID do empréstimo
     * @return O objeto Loan
     * @throws IllegalArgumentException Se o empréstimo não for encontrado
     */
    public Loan findLoanById(String loanId) {
        if (loanId == null || loanId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID do empréstimo não pode ser nulo ou vazio.");
        }

        return loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Empréstimo não encontrado com ID: " + loanId));
    }

    /**
     * Obtém todos os empréstimos de um usuário específico.
     * 
     * @param userId O ID do usuário
     * @return Uma lista de empréstimos do usuário
     */
    public List<Loan> getLoansByUser(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID do usuário não pode ser nulo ou vazio.");
        }

        return loanRepository.findByUserId(userId);
    }

    /**
     * Obtém todos os empréstimos ativos (não devolvidos) de um usuário específico.
     * 
     * @param userId O ID do usuário
     * @return Uma lista de empréstimos ativos do usuário
     */
    public List<Loan> getActiveLoansbyUser(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID do usuário não pode ser nulo ou vazio.");
        }

        return loanRepository.findActiveByUserId(userId);
    }

    /**
     * Obtém todos os empréstimos de um livro específico.
     * 
     * @param isbn O ISBN do livro
     * @return Uma lista de empréstimos do livro
     */
    public List<Loan> getLoansByBook(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN não pode ser nulo ou vazio.");
        }

        return loanRepository.findByBookIsbn(isbn);
    }

    /**
     * Obtém todos os empréstimos ativos (não devolvidos) no sistema.
     * 
     * @return Uma lista de todos os empréstimos ativos
     */
    public List<Loan> getAllActiveLoans() {
        return loanRepository.findAllActive();
    }

    /**
     * Obtém todos os empréstimos no sistema.
     * 
     * @return Uma lista de todos os empréstimos
     */
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    /**
     * Obtém todos os empréstimos atrasados com base na data atual.
     * 
     * @return Uma lista de empréstimos atrasados
     */
    public List<Loan> getOverdueLoans() {
        return getOverdueLoans(LocalDate.now());
    }

    /**
     * Obtém todos os empréstimos atrasados com base em uma data específica.
     * 
     * @param currentDate A data para verificar
     * @return Uma lista de empréstimos atrasados
     */
    public List<Loan> getOverdueLoans(LocalDate currentDate) {
        if (currentDate == null) {
            throw new IllegalArgumentException("Data atual não pode ser nula.");
        }

        List<Loan> activeLoans = loanRepository.findAllActive();
        return activeLoans.stream()
                .filter(loan -> loan.isOverdue(currentDate))
                .toList();
    }

    /**
     * Verifica se um empréstimo específico está atrasado.
     * 
     * @param loanId O ID do empréstimo
     * @return true se o empréstimo estiver atrasado, false caso contrário
     */
    public boolean isLoanOverdue(String loanId) {
        if (loanId == null || loanId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID do empréstimo não pode ser nulo ou vazio.");
        }

        Loan loan = findLoanById(loanId);
        return loan.isOverdue(LocalDate.now());
    }

    /**
     * Gera um ID único para o empréstimo.
     * 
     * @return Um ID único para o empréstimo
     */
    private String generateLoanId() {
        return "LOAN-" + UUID.randomUUID().toString();
    }
}
