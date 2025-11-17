package br.ufrn.library;

// 1. Importa os Modelos
import java.util.List;

import br.ufrn.library.exception.BookNotFoundException;
import br.ufrn.library.exception.NoCopiesAvailableException;
import br.ufrn.library.exception.UserNotFoundException;
import br.ufrn.library.model.Book;
import br.ufrn.library.model.Loan;
import br.ufrn.library.model.User;
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

    public static void main(String[] args) {

        // --- FASE 1: CONFIGURAÇÃO E INJEÇÃO DE DEPENDÊNCIA ---
        System.out.println("Iniciando o sistema da biblioteca...");

        // 1. Cria os "Trabalhadores" (Repositórios - Implementações)
        UserRepository userRepo = new InMemoryUserRepository();
        BookRepository bookRepo = new InMemoryBookRepository();
        LoanRepository loanRepo = new InMemoryLoanRepository();

        // 2. Cria os "Cérebros" (Serviços)
        UserService userService = new UserService(userRepo);
        BookService bookService = new BookService(bookRepo);
        LoanService loanService = new LoanService(loanRepo, bookRepo, userRepo);

        System.out.println("Sistema pronto. Serviços configurados.");

        // --- FASE 2: USO DA APLICAÇÃO (A UI) ---

        // --- Teste do UserService (código original) ---
        System.out.println("\n--- Testando UserService ---");
        try {
            System.out.println("Cadastrando usuário Joadson...");
            userService.registerUser("12345", "Joadson");

            System.out.println("Cadastrando usuário Paulo...");
            userService.registerUser("67890", "Paulo");

            System.out.println("Usuários cadastrados:");
            System.out.println(userService.listAllUsers());

            System.out.println("Buscando usuário Joadson:");
            User joadson = userService.findUserById("12345");
            System.out.println("Encontrado: " + joadson.getName());

        } catch (IllegalArgumentException e) {
            System.err.println("ERRO: " + e.getMessage());
        }

        // --- ATUALIZADO: Teste do BookService ---
        System.out.println("\n--- Testando BookService ---");
        try {
            // 1. Cadastro
            System.out.println("Cadastrando Livro Físico (ISBN 978-1)...");
            bookService.registerPhysicalBook("O Senhor dos Anéis", "J.R.R. Tolkien", "978-1", 5);

            System.out.println("Cadastrando Livro Digital (ISBN 123-456)...");
            bookService.registerDigitalBook("Duna", "Frank Herbert", "123-456");
            System.out.println("Livros cadastrados!");

            // 2. Listagem
            System.out.println("Listando todos os livros:");
            for (Book book : bookService.listAllBooks()) {
                System.out.println("  -> " + book.getTitle() + " (ISBN: " + book.getIsbn() + ")");
            }

            // 3. Atualização
            System.out.println("Atualizando 'Duna' para 'Duna (Ed. Especial)'...");
            bookService.updateDigitalBook("123-456", "Duna (Ed. Especial)", "Frank Herbert");
            Book duna = bookService.findBookByIsbn("123-456");
            System.out.println("Busca após atualização: " + duna.getTitle());

            // 4. Teste de Erro (Duplicado)
            System.out.println("Tentando cadastrar ISBN 978-1 de novo (deve falhar)...");
            bookService.registerPhysicalBook("Outro Livro", "Outro Autor", "978-1", 1);

        } catch (BookNotFoundException | IllegalArgumentException e) {
            // Pega erros de validação (como o duplicado)
            System.err.println("ERRO (Esperado no teste de duplicado): " + e.getMessage());
        }

        // 5. Teste de Erro (Não Encontrado)
        try {
            System.out.println("Tentando buscar ISBN '999' (deve falhar)...");
            bookService.findBookByIsbn("999");
        } catch (BookNotFoundException e) {
            System.err.println("ERRO (Esperado no teste de não encontrado): " + e.getMessage());
        }

        // --- Testando LoanService (Sistema de Empréstimo) ---
        System.out.println("\n--- Testando LoanService ---");
        try {
            // 1. Criar empréstimo
            System.out.println("Realizando empréstimo do livro '978-1' para o usuário Joadson...");
            Loan loan1 = loanService.createLoan("12345", "978-1");
            System.out.println("Empréstimo criado: " + loan1.getId());
            System.out.println("  -> Usuário: " + loan1.getUser().getName());
            System.out.println("  -> Livro: " + loan1.getBook().getTitle());
            System.out.println("  -> Data do empréstimo: " + loan1.getLoanDate());
            System.out.println("  -> Data de devolução: " + loan1.getDueDate());

            // 2. Criar mais um empréstimo
            System.out.println("\nRealizando empréstimo do livro '978-1' para o usuário Paulo...");
            Loan loan2 = loanService.createLoan("67890", "978-1");
            System.out.println("Empréstimo criado: " + loan2.getId());

            // 3. Listar empréstimos ativos
            System.out.println("\nListando todos os empréstimos ativos:");
            List<Loan> activeLoans = loanService.getAllActiveLoans();
            for (Loan loan : activeLoans) {
                System.out.println("  -> Empréstimo " + loan.getId() +
                        " | Usuário: " + loan.getUser().getName() +
                        " | Livro: " + loan.getBook().getTitle());
            }

            // 4. Listar empréstimos de um usuário específico
            System.out.println("\nListando empréstimos do usuário Joadson:");
            List<Loan> joadsonLoans = loanService.getLoansByUser("12345");
            System.out.println("  -> Total de empréstimos: " + joadsonLoans.size());

            // 5. Devolver um livro
            System.out.println("\nDevolvendo o livro do empréstimo " + loan1.getId() + "...");
            loanService.returnLoan(loan1.getId());
            System.out.println("Livro devolvido com sucesso!");
            System.out.println("  -> Status do empréstimo: " + (loan1.isReturned() ? "Devolvido" : "Ativo"));

            // 6. Listar empréstimos ativos após devolução
            System.out.println("\nListando empréstimos ativos após devolução:");
            activeLoans = loanService.getAllActiveLoans();
            System.out.println("  -> Total de empréstimos ativos: " + activeLoans.size());

            // 7. Verificar se um empréstimo está atrasado
            System.out.println("\nVerificando se o empréstimo " + loan2.getId() + " está atrasado:");
            boolean isOverdue = loanService.isLoanOverdue(loan2.getId());
            System.out.println("  -> Está atrasado? " + (isOverdue ? "Sim" : "Não"));

            // 8. Teste de erro - Usuário não encontrado
            System.out.println("\nTentando criar empréstimo para usuário inexistente (deve falhar)...");
            loanService.createLoan("USUARIO_INVALIDO", "978-1");

        } catch (UserNotFoundException e) {
            System.err.println("ERRO (Esperado - Usuário não encontrado): " + e.getMessage());
        } catch (BookNotFoundException e) {
            System.err.println("ERRO (Esperado - Livro não encontrado): " + e.getMessage());
        } catch (NoCopiesAvailableException e) {
            System.err.println("ERRO (Esperado - Sem cópias disponíveis): " + e.getMessage());
        } catch (Exception e) {
            System.err.println("ERRO no Sistema de Empréstimo: " + e.getMessage());
        }

        // 9. Teste de erro - Livro sem cópias disponíveis
        try {
            System.out.println("\nTentando emprestar todas as cópias disponíveis e depois mais uma (deve falhar)...");
            // Emprestar as 3 cópias restantes do livro 978-1 (já foram emprestadas 2,
            // restam 3)
            loanService.createLoan("12345", "978-1");
            loanService.createLoan("67890", "978-1");
            loanService.createLoan("12345", "978-1");
            // Tentar emprestar mais uma cópia (não há mais disponível)
            loanService.createLoan("67890", "978-1");

        } catch (NoCopiesAvailableException e) {
            System.err.println("ERRO (Esperado - Sem cópias disponíveis): " + e.getMessage());
        } catch (Exception e) {
            System.err.println("ERRO: " + e.getMessage());
        }

        System.out.println("\n--- Sistema finalizado ---");
    }
}