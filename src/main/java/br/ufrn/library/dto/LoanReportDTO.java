package br.ufrn.library.dto;

import java.util.Map;
import br.ufrn.library.model.Book;

public class LoanReportDTO {

    private final long totalLoans;
    private final Map<Book, Long> loansPerBook;

    public LoanReportDTO(long totalLoans, Map<Book, Long> loansPerBook) {
        this.totalLoans = totalLoans;
        this.loansPerBook = loansPerBook;
    }

    public long getTotalLoans() { return totalLoans; }

    public Map<Book, Long> getLoansPerBook() { return loansPerBook; }
}