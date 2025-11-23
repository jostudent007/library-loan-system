package br.ufrn.library.model;

public class DigitalBook extends Book {
    
    public DigitalBook(String title, String author, String isbn) {
        super(title, author, isbn);
    }

    public void download() {
        System.out.println("Downloading digital book: " + getTitle());
    }

    @Override
    public boolean isAvailableForLoan() {
        return true;
    }

    @Override
    public void registerLoan() {}

    @Override
    public void registerReturn() {}
}