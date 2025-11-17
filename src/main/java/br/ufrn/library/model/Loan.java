package br.ufrn.library.model;

import java.time.LocalDate;

/**
 * Represents a Loan in the library system.
 * This is a data class (POJO) that holds loan information.
 */
public class Loan {

    private String id; // Unique loan ID
    private User user; // The user who borrowed the book
    private Book book; // The book that was borrowed
    private LocalDate loanDate; // Date when the loan was made
    private LocalDate dueDate; // Date when the book should be returned
    private LocalDate returnDate; // Date when the book was actually returned (null if not returned yet)
    private boolean isReturned; // Status of the loan

    /**
     * Constructor for creating a new loan.
     * 
     * @param id The unique identifier for this loan
     * @param user The user borrowing the book
     * @param book The book being borrowed
     * @param loanDate The date of the loan
     * @param dueDate The date the book should be returned
     */
    public Loan(String id, User user, Book book, LocalDate loanDate, LocalDate dueDate) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Loan ID cannot be null or empty.");
        }
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null.");
        }
        if (loanDate == null) {
            throw new IllegalArgumentException("Loan date cannot be null.");
        }
        if (dueDate == null) {
            throw new IllegalArgumentException("Due date cannot be null.");
        }
        if (dueDate.isBefore(loanDate)) {
            throw new IllegalArgumentException("Due date cannot be before loan date.");
        }

        this.id = id;
        this.user = user;
        this.book = book;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = null;
        this.isReturned = false;
    }

    /**
     * Marks the loan as returned.
     * 
     * @param returnDate The date when the book was returned
     */
    public void markAsReturned(LocalDate returnDate) {
        if (returnDate == null) {
            throw new IllegalArgumentException("Return date cannot be null.");
        }
        if (returnDate.isBefore(loanDate)) {
            throw new IllegalArgumentException("Return date cannot be before loan date.");
        }
        if (this.isReturned) {
            throw new IllegalStateException("Loan has already been returned.");
        }

        this.returnDate = returnDate;
        this.isReturned = true;
    }

    /**
     * Checks if the loan is overdue based on the current date.
     * 
     * @param currentDate The date to check against
     * @return true if the loan is overdue and not yet returned
     */
    public boolean isOverdue(LocalDate currentDate) {
        if (currentDate == null) {
            throw new IllegalArgumentException("Current date cannot be null.");
        }
        return !isReturned && currentDate.isAfter(dueDate);
    }

    // --- Getters ---

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Book getBook() {
        return book;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public boolean isReturned() {
        return isReturned;
    }

    // --- Object methods ---

    @Override
    public String toString() {
        return "Loan{" +
                "id='" + id + '\'' +
                ", user=" + user.getName() +
                ", book=" + book.getTitle() +
                ", loanDate=" + loanDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                ", isReturned=" + isReturned +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Loan loan = (Loan) o;
        return id.equals(loan.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
