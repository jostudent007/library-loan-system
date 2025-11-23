package br.ufrn.library.cli;

import java.util.Scanner;
import br.ufrn.library.dto.BookAvailabilityDTO;
import br.ufrn.library.service.BookService;

public class BookConsoleHandler {

    private final BookService bookService;
    private final Scanner scanner;

    public BookConsoleHandler(BookService bookService, Scanner scanner) {
        this.bookService = bookService;
        this.scanner = scanner;
    }

    public void handleRegisterBook() {
        System.out.println("\n--- 1. Cadastrar Livro ---");
        System.out.print("Tipo (1-Físico, 2-Digital): ");
        int type = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Título: ");
        String title = scanner.nextLine();
        System.out.print("Autor: ");
        String author = scanner.nextLine();
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();

        if (type == 1) {
            System.out.print("Quantidade de Cópias: ");
            int copies = Integer.parseInt(scanner.nextLine());
            bookService.registerPhysicalBook(title, author, isbn, copies);
            System.out.println("Livro Físico cadastrado com sucesso!");
        } else if (type == 2) {
            bookService.registerDigitalBook(title, author, isbn);
            System.out.println("Livro Digital cadastrado com sucesso!");
        } else {
            System.err.println("Tipo inválido.");
        }
    }

    public void handleListBookAvailability() {
        System.out.println("\n--- 5. Listar Livros e Disponibilidade ---");
        
        var dtos = bookService.getBookAvailabilityReport();
        if (dtos.isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
            return;
        }

        for (BookAvailabilityDTO dto : dtos) {
            System.out.printf("  -> %s (ISBN: %s) [%s] | %s\n",
                    dto.getTitle(),
                    dto.getIsbn(),
                    dto.getType(),
                    dto.getAvailability());
        }
    }
}