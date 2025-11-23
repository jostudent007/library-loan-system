package br.ufrn.library.model;

public abstract class Book {
    
    protected String title;
    protected String author;
    protected final String isbn;

    public Book(String title, String author, String isbn) {

        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty.");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Book title cannot be empty.");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Book author cannot be empty.");
        }

        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    public void updateDetails(String title, String author) {

        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Book title cannot be empty.");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Book author cannot be empty.");
        }
        
        this.title = title;
        this.author = author;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    
    public abstract boolean isAvailableForLoan();
    public abstract void registerLoan();
    public abstract void registerReturn();
}