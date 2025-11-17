package br.ufrn.library.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.ufrn.library.exception.BookNotFoundException;
import br.ufrn.library.exception.NoCopiesAvailableException;
import br.ufrn.library.exception.UserNotFoundException;
import br.ufrn.library.model.Book;
import br.ufrn.library.model.Loan;
import br.ufrn.library.model.PhysicalBook;
import br.ufrn.library.model.User;
import br.ufrn.library.repository.BookRepository;
import br.ufrn.library.repository.LoanRepository;
import br.ufrn.library.repository.UserRepository;
import br.ufrn.library.repository.impl.InMemoryBookRepository;
import br.ufrn.library.repository.impl.InMemoryLoanRepository;
import br.ufrn.library.repository.impl.InMemoryUserRepository;

/**
 * Unit tests for LoanService.
 */
class LoanServiceTest {

    private LoanService loanService;
    private LoanRepository loanRepository;
    private BookRepository bookRepository;
    private UserRepository userRepository;

    private User testUser;
    private Book testBook;

    @BeforeEach
    void setUp() {
        // Initialize repositories
        loanRepository = new InMemoryLoanRepository();
        bookRepository = new InMemoryBookRepository();
        userRepository = new InMemoryUserRepository();

        // Initialize service
        loanService = new LoanService(loanRepository, bookRepository, userRepository);

        // Create test data
        testUser = new User("USER001", "John Doe");
        userRepository.save(testUser);

        testBook = new PhysicalBook("Clean Code", "Robert C. Martin", "978-0132350884", 5);
        bookRepository.save(testBook);
    }

    @Test
    void testCreateLoan_Success() {
        // Act
        Loan loan = loanService.createLoan("USER001", "978-0132350884");

        // Assert
        assertNotNull(loan);
        assertNotNull(loan.getId());
        assertEquals(testUser, loan.getUser());
        assertEquals(testBook, loan.getBook());
        assertEquals(LocalDate.now(), loan.getLoanDate());
        assertEquals(LocalDate.now().plusDays(14), loan.getDueDate());
        assertFalse(loan.isReturned());
        assertNull(loan.getReturnDate());
    }

    @Test
    void testCreateLoan_WithCustomPeriod() {
        // Act
        Loan loan = loanService.createLoan("USER001", "978-0132350884", LocalDate.now(), 7);

        // Assert
        assertNotNull(loan);
        assertEquals(LocalDate.now().plusDays(7), loan.getDueDate());
    }

    @Test
    void testCreateLoan_UserNotFound() {
        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            loanService.createLoan("INVALID_USER", "978-0132350884");
        });
    }

    @Test
    void testCreateLoan_BookNotFound() {
        // Act & Assert
        assertThrows(BookNotFoundException.class, () -> {
            loanService.createLoan("USER001", "INVALID_ISBN");
        });
    }

    @Test
    void testCreateLoan_NoCopiesAvailable() {
        // Arrange - Borrow all available copies
        PhysicalBook limitedBook = new PhysicalBook("Limited Book", "Author", "978-1234567890", 1);
        bookRepository.save(limitedBook);
        loanService.createLoan("USER001", "978-1234567890");

        // Act & Assert
        assertThrows(NoCopiesAvailableException.class, () -> {
            loanService.createLoan("USER001", "978-1234567890");
        });
    }

    @Test
    void testCreateLoan_UpdatesBookAvailability() {
        // Arrange
        PhysicalBook physicalBook = (PhysicalBook) testBook;
        int initialAvailable = physicalBook.getAvailableCopies();

        // Act
        loanService.createLoan("USER001", "978-0132350884");

        // Assert
        assertEquals(initialAvailable - 1, physicalBook.getAvailableCopies());
    }

    @Test
    void testCreateLoan_AddsToUserHistory() {
        // Arrange
        int initialHistorySize = testUser.getLoanHistory().size();

        // Act
        loanService.createLoan("USER001", "978-0132350884");

        // Assert
        assertEquals(initialHistorySize + 1, testUser.getLoanHistory().size());
    }

    @Test
    void testReturnLoan_Success() {
        // Arrange
        Loan loan = loanService.createLoan("USER001", "978-0132350884");
        String loanId = loan.getId();

        // Act
        Loan returnedLoan = loanService.returnLoan(loanId);

        // Assert
        assertTrue(returnedLoan.isReturned());
        assertNotNull(returnedLoan.getReturnDate());
        assertEquals(LocalDate.now(), returnedLoan.getReturnDate());
    }

    @Test
    void testReturnLoan_WithCustomReturnDate() {
        // Arrange
        Loan loan = loanService.createLoan("USER001", "978-0132350884");
        LocalDate returnDate = LocalDate.now().plusDays(5);

        // Act
        Loan returnedLoan = loanService.returnLoan(loan.getId(), returnDate);

        // Assert
        assertEquals(returnDate, returnedLoan.getReturnDate());
    }

    @Test
    void testReturnLoan_LoanNotFound() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.returnLoan("INVALID_LOAN_ID");
        });
    }

    @Test
    void testReturnLoan_AlreadyReturned() {
        // Arrange
        Loan loan = loanService.createLoan("USER001", "978-0132350884");
        loanService.returnLoan(loan.getId());

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            loanService.returnLoan(loan.getId());
        });
    }

    @Test
    void testReturnLoan_UpdatesBookAvailability() {
        // Arrange
        Loan loan = loanService.createLoan("USER001", "978-0132350884");
        PhysicalBook physicalBook = (PhysicalBook) testBook;
        int availableBeforeReturn = physicalBook.getAvailableCopies();

        // Act
        loanService.returnLoan(loan.getId());

        // Assert
        assertEquals(availableBeforeReturn + 1, physicalBook.getAvailableCopies());
    }

    @Test
    void testFindLoanById_Success() {
        // Arrange
        Loan loan = loanService.createLoan("USER001", "978-0132350884");

        // Act
        Loan foundLoan = loanService.findLoanById(loan.getId());

        // Assert
        assertNotNull(foundLoan);
        assertEquals(loan.getId(), foundLoan.getId());
    }

    @Test
    void testFindLoanById_NotFound() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.findLoanById("INVALID_LOAN_ID");
        });
    }

    @Test
    void testGetLoansByUser() {
        // Arrange
        loanService.createLoan("USER001", "978-0132350884");
        loanService.createLoan("USER001", "978-0132350884");

        // Act
        List<Loan> loans = loanService.getLoansByUser("USER001");

        // Assert
        assertEquals(2, loans.size());
        assertTrue(loans.stream().allMatch(loan -> loan.getUser().getId().equals("USER001")));
    }

    @Test
    void testGetActiveLoansbyUser() {
        // Arrange
        Loan loan1 = loanService.createLoan("USER001", "978-0132350884");
        Loan loan2 = loanService.createLoan("USER001", "978-0132350884");
        loanService.returnLoan(loan1.getId());

        // Act
        List<Loan> activeLoans = loanService.getActiveLoansbyUser("USER001");

        // Assert
        assertEquals(1, activeLoans.size());
        assertFalse(activeLoans.get(0).isReturned());
        assertEquals(loan2.getId(), activeLoans.get(0).getId());
    }

    @Test
    void testGetLoansByBook() {
        // Arrange
        loanService.createLoan("USER001", "978-0132350884");
        loanService.createLoan("USER001", "978-0132350884");

        // Act
        List<Loan> loans = loanService.getLoansByBook("978-0132350884");

        // Assert
        assertEquals(2, loans.size());
        assertTrue(loans.stream().allMatch(loan -> loan.getBook().getIsbn().equals("978-0132350884")));
    }

    @Test
    void testGetAllActiveLoans() {
        // Arrange
        Loan loan1 = loanService.createLoan("USER001", "978-0132350884");
        Loan loan2 = loanService.createLoan("USER001", "978-0132350884");
        loanService.returnLoan(loan1.getId());

        // Act
        List<Loan> activeLoans = loanService.getAllActiveLoans();

        // Assert
        assertEquals(1, activeLoans.size());
        assertEquals(loan2.getId(), activeLoans.get(0).getId());
    }

    @Test
    void testGetAllLoans() {
        // Arrange
        Loan loan1 = loanService.createLoan("USER001", "978-0132350884");
        Loan loan2 = loanService.createLoan("USER001", "978-0132350884");

        // Act
        List<Loan> allLoans = loanService.getAllLoans();

        // Assert
        assertEquals(2, allLoans.size());
        assertTrue(allLoans.contains(loan1));
        assertTrue(allLoans.contains(loan2));
    }

    @Test
    void testGetOverdueLoans() {
        // Arrange
        LocalDate pastDate = LocalDate.now().minusDays(20);
        loanService.createLoan("USER001", "978-0132350884", pastDate, 14);
        loanService.createLoan("USER001", "978-0132350884"); // Not overdue

        // Act
        List<Loan> overdueLoans = loanService.getOverdueLoans();

        // Assert
        assertEquals(1, overdueLoans.size());
        assertTrue(overdueLoans.get(0).isOverdue(LocalDate.now()));
    }

    @Test
    void testGetOverdueLoans_WithSpecificDate() {
        // Arrange
        LocalDate testDate = LocalDate.of(2025, 1, 1);
        loanService.createLoan("USER001", "978-0132350884", testDate.minusDays(20), 14);

        // Act
        List<Loan> overdueLoans = loanService.getOverdueLoans(testDate);

        // Assert
        assertEquals(1, overdueLoans.size());
    }

    @Test
    void testIsLoanOverdue_True() {
        // Arrange
        LocalDate pastDate = LocalDate.now().minusDays(20);
        Loan loan = loanService.createLoan("USER001", "978-0132350884", pastDate, 14);

        // Act
        boolean isOverdue = loanService.isLoanOverdue(loan.getId());

        // Assert
        assertTrue(isOverdue);
    }

    @Test
    void testIsLoanOverdue_False() {
        // Arrange
        Loan loan = loanService.createLoan("USER001", "978-0132350884");

        // Act
        boolean isOverdue = loanService.isLoanOverdue(loan.getId());

        // Assert
        assertFalse(isOverdue);
    }

    @Test
    void testCreateLoan_WithNullUserId() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.createLoan(null, "978-0132350884");
        });
    }

    @Test
    void testCreateLoan_WithEmptyUserId() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.createLoan("", "978-0132350884");
        });
    }

    @Test
    void testCreateLoan_WithNullIsbn() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.createLoan("USER001", null);
        });
    }

    @Test
    void testCreateLoan_WithEmptyIsbn() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.createLoan("USER001", "");
        });
    }

    @Test
    void testReturnLoan_WithNullLoanId() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.returnLoan(null);
        });
    }

    @Test
    void testReturnLoan_WithEmptyLoanId() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.returnLoan("");
        });
    }

    @Test
    void testConstructor_WithNullLoanRepository() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new LoanService(null, bookRepository, userRepository);
        });
    }

    @Test
    void testConstructor_WithNullBookRepository() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new LoanService(loanRepository, null, userRepository);
        });
    }

    @Test
    void testConstructor_WithNullUserRepository() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new LoanService(loanRepository, bookRepository, null);
        });
    }
}
