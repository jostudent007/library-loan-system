package br.ufrn.library.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import br.ufrn.library.exception.BookNotFoundException;
import br.ufrn.library.model.Book;
import br.ufrn.library.model.PhysicalBook;
import br.ufrn.library.repository.BookRepository;
import br.ufrn.library.repository.impl.InMemoryBookRepository;

class BookServiceTest {

    private BookRepository bookRepository;
    private BookService bookService;

    @BeforeEach
    void setUp() {
        this.bookRepository = new InMemoryBookRepository();
        this.bookService = new BookService(this.bookRepository);
    }


    @Test
    @DisplayName("Deve registrar um livro físico com sucesso")
    void shouldRegisterPhysicalBookSuccessfully() {
    
        bookService.registerPhysicalBook("O Senhor dos Anéis", "J.R.R. Tolkien", "978-1", 5);

        assertTrue(bookRepository.existsByIsbn("978-1"));

        Book foundBook = bookService.findBookByIsbn("978-1");
        
        assertNotNull(foundBook);
        assertEquals("O Senhor dos Anéis", foundBook.getTitle());
        assertEquals("J.R.R. Tolkien", foundBook.getAuthor());
        
        assertTrue(foundBook instanceof PhysicalBook); 

        assertEquals(5, ((PhysicalBook) foundBook).getTotalCopies());
    }

    @Test
    @DisplayName("Não deve registrar um livro com ISBN duplicado")
    void shouldNotRegisterBookWithDuplicateIsbn() {
        
        bookService.registerDigitalBook("Duna", "Frank Herbert", "123-456");

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> {
                bookService.registerPhysicalBook("Outro Livro", "Outro Autor", "123-456", 10);
            }
        );

        assertEquals("A book with this ISBN already exists: 123-456", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar BookNotFoundException ao deletar livro inexistente")
    void shouldThrowExceptionWhenDeletingNonExistentBook() {
        assertThrows(
            BookNotFoundException.class,
            () -> {
                bookService.deleteBook("ISBN-QUE-NAO-EXISTE");
            }
        );
    }
}