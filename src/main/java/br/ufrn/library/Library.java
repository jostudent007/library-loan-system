package br.ufrn.library;

import java.util.Scanner;

import br.ufrn.library.cli.BookConsoleHandler;
import br.ufrn.library.cli.LoanConsoleHandler;
import br.ufrn.library.cli.UserConsoleHandler;
import br.ufrn.library.repository.BookRepository;
import br.ufrn.library.repository.LoanRepository;
import br.ufrn.library.repository.UserRepository;
import br.ufrn.library.repository.impl.InMemoryBookRepository;
import br.ufrn.library.repository.impl.InMemoryLoanRepository;
import br.ufrn.library.repository.impl.InMemoryUserRepository;
import br.ufrn.library.service.BookService;
import br.ufrn.library.service.LoanService;
import br.ufrn.library.service.UserService;


public class Library {

    private static final Scanner scanner = new Scanner(System.in);
    
    private static BookService bookService;
    private static UserService userService;
    private static LoanService loanService;

    private static BookConsoleHandler bookHandler;
    private static UserConsoleHandler userHandler;
    private static LoanConsoleHandler loanHandler;

    public static void main(String[] args) {
        setupServices();
        setupHandlers();
        runMenuLoop();
        
        scanner.close();
        System.out.println("Sistema finalizado.");
    }

    private static void setupServices() {
        UserRepository userRepo = new InMemoryUserRepository();
        BookRepository bookRepo = new InMemoryBookRepository();
        LoanRepository loanRepo = new InMemoryLoanRepository();

        userService = new UserService(userRepo);
        bookService = new BookService(bookRepo);
        loanService = new LoanService(loanRepo, bookRepo, userRepo);
    }

    private static void setupHandlers() {
        bookHandler = new BookConsoleHandler(bookService, scanner);
        userHandler = new UserConsoleHandler(userService, scanner);
        loanHandler = new LoanConsoleHandler(loanService, scanner);
    }

    private static void runMenuLoop() {
        boolean running = true;
        while (running) {
            printMenu();
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                running = dispatchMenuChoice(choice);
            } catch (NumberFormatException e) {
                System.err.println("Erro: Por favor, digite um número válido.");
            }
            
            if (running) {
                System.out.println("\nPressione Enter para continuar...");
                scanner.nextLine();
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n--- Sistema de Biblioteca ---");
        System.out.println("1. Cadastrar Livro");
        System.out.println("2. Cadastrar Usuário");
        System.out.println("3. Realizar Empréstimo");
        System.out.println("4. Realizar Devolução");
        System.out.println("5. Listar Livros e Disponibilidade");
        System.out.println("6. Listar Usuários Cadastrados");
        System.out.println("7. Listar Empréstimos Ativos");
        System.out.println("8. Gerar Relatório de Empréstimos");
        System.out.println("9. Carregar Dados");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static boolean dispatchMenuChoice(int choice) {
        try {
            switch (choice) {
                case 1:
                    bookHandler.handleRegisterBook();
                    break;
                case 2:
                    userHandler.handleRegisterUser();
                    break;
                case 3:
                    loanHandler.handleCreateLoan();
                    break;
                case 4:
                    loanHandler.handleReturnLoan();
                    break;
                case 5:
                    bookHandler.handleListBookAvailability();
                    break;
                case 6:
                    userHandler.handleListAllUsers();
                    break;
                case 7:
                    loanHandler.handleListActiveLoans();
                    break;
                case 8:
                    loanHandler.handleLoanReport();
                    break;
                case 9:
                    DataLoader.seed(userService, bookService, loanService);
                    break;
                case 0:
                    return false;
                default:
                    System.err.println("Opção inválida. Tente novamente.");
            }
        } catch (Exception e) {
            System.err.println("\n--- ERRO NA OPERAÇÃO ---\n" + e.getMessage() + "\n");
        }
        return true;
    }
    
}