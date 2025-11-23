package br.ufrn.library.cli;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import br.ufrn.library.dto.LoanReportDTO;
import br.ufrn.library.model.Loan;
import br.ufrn.library.service.LoanService;

public class LoanConsoleHandler {

    private final LoanService loanService;
    private final Scanner scanner;

    public LoanConsoleHandler(LoanService loanService, Scanner scanner) {
        this.loanService = loanService;
        this.scanner = scanner;
    }

    public void handleCreateLoan() {
        System.out.println("\n--- 3. Realizar Empréstimo ---");
        System.out.print("ID do Empréstimo: ");
        String loanId = scanner.nextLine();
        System.out.print("ID do Usuário: ");
        String userId = scanner.nextLine();
        System.out.print("ISBN do Livro: ");
        String isbn = scanner.nextLine();

        loanService.createLoan(loanId, userId, isbn);
        System.out.println("Empréstimo realizado com sucesso!");
    }

    public void handleReturnLoan() {
        System.out.println("\n--- 4. Realizar Devolução ---");
        System.out.print("ID do Empréstimo: ");
        String loanId = scanner.nextLine();

        loanService.returnLoan(loanId, LocalDate.now());
        System.out.println("Devolução registrada com sucesso!");
    }

    public void handleListActiveLoans() {
        System.out.println("\n--- 8. Listar Empréstimos Ativos ---");
        List<Loan> activeLoans = loanService.getAllActiveLoans();

        if (activeLoans.isEmpty()) {
            System.out.println("Nenhum empréstimo ativo no momento.");
            return;
        }

        for (Loan loan : activeLoans) {
            System.out.printf("  -> ID: %s | Data: %s | Usuário: %s | Livro: %s\n",
                    loan.getId(),
                    loan.getLoanDate().toString(),
                    loan.getUser().getName(),
                    loan.getBook().getTitle());
        }
    }

    public void handleLoanReport() {
        System.out.println("\n--- 8. Relatório de Empréstimos ---");
        LoanReportDTO report = loanService.generateLoanReport();

        System.out.println("Total de Empréstimos no Sistema: " + report.getTotalLoans());
        System.out.println("Empréstimos por Livro (Descendente):");

        if (report.getLoansPerBook().isEmpty()) {
            System.out.println("Nenhum empréstimo foi realizado ainda.");
            return;
        }

        report.getLoansPerBook().forEach((book, count) -> {
            System.out.printf("  -> %s (ISBN: %s): %d empréstimo(s)\n",
                    book.getTitle(),
                    book.getIsbn(),
                    count);
        });
    }
}
