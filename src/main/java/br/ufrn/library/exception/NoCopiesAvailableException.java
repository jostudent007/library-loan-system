package br.ufrn.library.exception;

public class NoCopiesAvailableException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NoCopiesAvailableException(String message) {
        super(message);
    }
}
