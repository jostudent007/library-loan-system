package br.ufrn.library.service;

import java.util.List;
import java.util.stream.Collectors;

import br.ufrn.library.exception.BookNotFoundException;
import br.ufrn.library.model.Book;
import br.ufrn.library.model.DigitalBook;
import br.ufrn.library.model.PhysicalBook;
import br.ufrn.library.repository.BookRepository;
import br.ufrn.library.dto.BookAvailabilityDTO;

public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void registerDigitalBook(String title, String author, String isbn) {
        if (bookRepository.existsByIsbn(isbn)) {
            throw new IllegalArgumentException("A book with this ISBN already exists: " + isbn);
        }
        DigitalBook digitalBook = new DigitalBook(title, author, isbn);
        bookRepository.save(digitalBook);
    }

    public void registerPhysicalBook(String title, String author, String isbn, int totalCopies) {
        if (bookRepository.existsByIsbn(isbn)) {
            throw new IllegalArgumentException("A book with this ISBN already exists: " + isbn);
        }
        PhysicalBook physicalBook = new PhysicalBook(title, author, isbn, totalCopies);
        bookRepository.save(physicalBook);
    }

    public void updateDigitalBook(String isbn, String newTitle, String newAuthor) {
        Book bookToUpdate = findBookByIsbn(isbn);

        if (bookToUpdate instanceof DigitalBook digitalBook) {
            digitalBook.updateDetails(newTitle, newAuthor);
            bookRepository.save(digitalBook);
        } else {
            throw new IllegalArgumentException("Book with isbn: " + isbn + " is not a digital book.");
        }
    }

    public void updatePhysicalBook(String isbn, String newTitle, String newAuthor, int newTotalCopies) {
        Book bookToUpdate = findBookByIsbn(isbn);

        if (bookToUpdate instanceof PhysicalBook physicalBook) {
            physicalBook.updateDetails(newTitle, newAuthor);
            physicalBook.setTotalCopies(newTotalCopies);
            bookRepository.save(physicalBook);
        } else {
            throw new IllegalArgumentException("Book with isbn: " + isbn + " is not a physical book.");
        }
    }

    public Book findBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException("Book not found with isbn: " + isbn));
    }

    public List<Book> listAllBooks() {
        return bookRepository.findAll();
    }

    public List<BookAvailabilityDTO> getBookAvailabilityReport() {
        return bookRepository.findAll().stream()
                .map(BookAvailabilityDTO::new)
                .collect(Collectors.toList());
    }

    public void deleteBook(String isbn) {
        if (!bookRepository.existsByIsbn(isbn)) {
            throw new BookNotFoundException("Book not found with isbn: " + isbn);
        }
        bookRepository.deleteByIsbn(isbn);
    }
}