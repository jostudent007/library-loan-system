package br.ufrn.library;

import br.ufrn.library.service.BookService;
import br.ufrn.library.service.LoanService;
import br.ufrn.library.service.UserService;

public class DataLoader {

    public static void seed(UserService userService, BookService bookService, LoanService loanService) {
        System.out.println("Abastecendo sistema com dados iniciais...");
        
        try {
            userService.registerUser("u-001", "Alice Smith");
            userService.registerUser("u-002", "Bruno Costa");
            userService.registerUser("u-003", "Carla Dias");
            userService.registerUser("u-004", "Daniel Coelho");
            userService.registerUser("u-005", "Elisa Ferreira");
            userService.registerUser("u-006", "Joadson Ferreira");
            userService.registerUser("u-007", "Gabriela Lima");
            userService.registerUser("u-008", "Nathan Medeiros");
            userService.registerUser("u-009", "Iris Nogueira");
            userService.registerUser("u-010", "João Medeiros");

            bookService.registerPhysicalBook("O Senhor dos Anéis", "J.R.R. Tolkien", "978-1-01", 5);
            bookService.registerPhysicalBook("1984", "George Orwell", "978-1-02", 3);
            bookService.registerPhysicalBook("O Sol é para todos", "Harper Lee", "978-1-03", 2);
            bookService.registerPhysicalBook("Cem Anos de Solidão", "Gabriel García Márquez", "978-1-04", 4);
            bookService.registerPhysicalBook("A Revolução dos Bichos", "George Orwell", "978-1-05", 3);
            bookService.registerPhysicalBook("O Grande Gatsby", "F. Scott Fitzgerald", "978-1-06", 2);
            bookService.registerPhysicalBook("Dom Quixote", "Miguel de Cervantes", "978-1-07", 1);
            bookService.registerPhysicalBook("Crime e Castigo", "Fiódor Dostoiévski", "978-1-08", 2);
            bookService.registerPhysicalBook("Orgulho e Preconceito", "Jane Austen", "978-1-09", 3);
            bookService.registerPhysicalBook("Ulisses", "James Joyce", "978-1-10", 1);
            
            bookService.registerDigitalBook("Duna", "Frank Herbert", "978-2-01");
            bookService.registerDigitalBook("Fundação", "Isaac Asimov", "978-2-02");
            bookService.registerDigitalBook("O Guia do Mochileiro das Galáxias", "Douglas Adams", "978-2-03");
            bookService.registerDigitalBook("Neuromancer", "William Gibson", "978-2-04");
            bookService.registerDigitalBook("Sapiens", "Yuval Noah Harari", "978-2-05");
            bookService.registerDigitalBook("A Arte da Guerra", "Sun Tzu", "978-2-06");
            bookService.registerDigitalBook("O Príncipe", "Maquiavel", "978-2-07");
            bookService.registerDigitalBook("12 Regras para a Vida", "Jordan Peterson", "978-2-08");
            bookService.registerDigitalBook("O Poder do Hábito", "Charles Duhigg", "978-2-09");
            bookService.registerDigitalBook("Rápido e Devagar", "Daniel Kahneman", "978-2-10");

            loanService.createLoan("l-001", "u-001", "978-1-01"); 
            loanService.createLoan("l-002", "u-001", "978-2-01"); 
            loanService.createLoan("l-003", "u-001", "978-1-04");

            loanService.createLoan("l-004", "u-002", "978-1-02"); 
            loanService.createLoan("l-005", "u-002", "978-2-02");

            loanService.createLoan("l-006", "u-003", "978-1-02"); 
            loanService.createLoan("l-007", "u-003", "978-1-05"); 

            loanService.createLoan("l-008", "u-004", "978-2-05"); 

            loanService.createLoan("l-009", "u-005", "978-1-01"); 
            loanService.createLoan("l-010", "u-005", "978-1-09"); 

            loanService.createLoan("l-011", "u-006", "978-1-07");

            loanService.createLoan("l-012", "u-007", "978-1-08"); 

            loanService.createLoan("l-013", "u-008", "978-2-09"); 

            System.out.println("Dados carregados com sucesso!");
            System.out.println("---------------------------------");

        } catch (Exception e) {
            System.err.println("ERRO CRÍTICO AO ABASTECER DADOS: " + e.getMessage());
            System.err.println("O menu pode não funcionar como esperado.");
            System.out.println("---------------------------------");
        }
    }
}