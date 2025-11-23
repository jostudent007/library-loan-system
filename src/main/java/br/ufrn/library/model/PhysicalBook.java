package br.ufrn.library.model;

public class PhysicalBook extends Book {
    
    private int totalCopies;
    private int availableCopies;

    public PhysicalBook(String title, String author, String isbn, int totalCopies) {
        super(title, author, isbn);
        if (totalCopies < 0) {
            throw new IllegalArgumentException("Total copies cannot be negative.");
        }
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }

    @Override
    public boolean isAvailableForLoan() {
        return availableCopies > 0;
    }

    @Override
    public void registerLoan() {
        if (isAvailableForLoan()) {
            this.availableCopies--;
        } else {
            throw new IllegalStateException("No copies available to register loan for book: " + this.isbn);
        }
    }

    @Override
    public void registerReturn() {
        if (this.availableCopies < this.totalCopies) {
            this.availableCopies++;
        }
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setTotalCopies(int newTotalCopies) {
        if (newTotalCopies < 0) {
            throw new IllegalArgumentException("Total copies cannot be negative.");
        }

        int loanedCopies = this.totalCopies - this.availableCopies;

        if (newTotalCopies < loanedCopies) {
            throw new IllegalStateException(
                "Cannot set total copies to " + newTotalCopies + 
                ". There are currently " + loanedCopies + " copies on loan."
            );
        }

        this.totalCopies = newTotalCopies;
        this.availableCopies = newTotalCopies - loanedCopies;
    }
}