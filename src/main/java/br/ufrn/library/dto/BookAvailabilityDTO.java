package br.ufrn.library.dto;

import br.ufrn.library.model.Book;
import br.ufrn.library.model.DigitalBook;
import br.ufrn.library.model.PhysicalBook;

public class BookAvailabilityDTO {

    private final String isbn;
    private final String title;
    private final String author;
    private final String type;
    private final String availability;

    public BookAvailabilityDTO(Book book) {
        this.isbn = book.getIsbn();
        this.title = book.getTitle();
        this.author = book.getAuthor();

        if (book instanceof PhysicalBook physicalBook) {
            this.type = "Físico";
            this.availability = physicalBook.getAvailableCopies() + " / " + physicalBook.getTotalCopies();
        } else if (book instanceof DigitalBook) {
            this.type = "Digital";
            this.availability = "Sempre disponível";
        } else {
            this.type = "Unknown";
            this.availability = "N/A";
        }
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getType() {
        return type;
    }

    public String getAvailability() {
        return availability;
    }
}