package br.ufrn.library.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String id;
    private String name;
    private List<Loan> loanHistory;

    public User(String id, String name) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be empty.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be empty.");
        }
        
        this.id = id;
        this.name = name;
        this.loanHistory = new ArrayList<>();
    }

    public String getId() {
        return id;
    }
    
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be empty.");
        }
        this.name = name;
    }


    public String getName() {
        return name;
    }


    public List<Loan> getLoanHistory() {
        return loanHistory;
    }

    public void addLoanToHistory(Loan loan) {
        this.loanHistory.add(loan);
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", loans=" + loanHistory.size() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}