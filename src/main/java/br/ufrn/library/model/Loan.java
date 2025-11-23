package br.ufrn.library.model;

import java.time.LocalDate;

public class Loan {

    private String id;
    private User user;
    private Book book;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private boolean isReturned;

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

    public boolean isOverdue(LocalDate currentDate) {
        if (currentDate == null) {
            throw new IllegalArgumentException("Current date cannot be null.");
        }
        return !isReturned && currentDate.isAfter(dueDate);
    }

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

    @Override
    public String toString() {
        return "Loan{" +
                "id='" + id + '\'' +
                ", userId='" + (user != null ? user.getId() : "null") + '\'' +
                ", bookIsbn='" + (book != null ? book.getIsbn() : "null") + '\'' +
                ", loanDate=" + loanDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                ", isReturned=" + isReturned +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Loan loan = (Loan) o;
        return id.equals(loan.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
