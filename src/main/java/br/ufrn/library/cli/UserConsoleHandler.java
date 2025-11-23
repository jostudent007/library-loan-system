package br.ufrn.library.cli;

import java.util.List;
import java.util.Scanner;

import br.ufrn.library.model.User;
import br.ufrn.library.service.UserService;

public class UserConsoleHandler {

    private final UserService userService;
    private final Scanner scanner;

    public UserConsoleHandler(UserService userService, Scanner scanner) {
        this.userService = userService;
        this.scanner = scanner;
    }

    public void handleRegisterUser() {
        System.out.println("\n--- 2. Cadastrar Usuário ---");
        System.out.print("ID do Usuário: ");
        String id = scanner.nextLine();
        System.out.print("Nome: ");
        String name = scanner.nextLine();

        userService.registerUser(id, name);
        System.out.println("Usuário cadastrado com sucesso!");
    }

    public void handleListAllUsers() {
        System.out.println("\n--- 6. Listar Usuários Cadastrados ---");
        List<User> users = userService.listAllUsers();

        if (users.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
            return;
        }

        for (User user : users) {
            System.out.printf("  -> ID: %s | Nome: %s | Empréstimos no Histórico: %d\n",
                    user.getId(),
                    user.getName(),
                    user.getLoanHistory().size());
        }
    }

}
