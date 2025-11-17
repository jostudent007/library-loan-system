# 📚 Sistema de Gerenciamento de Biblioteca (Library Loan System)

![Java](https://img.shields.io/badge/Java-17%2B-blue?logo=java)
![Maven](https://img.shields.io/badge/Maven-3.8%2B-red?logo=apachemaven)
![License](https://img.shields.io/badge/License-MIT-green)

Projeto da disciplina de Boas Práticas de Programação. O objetivo é criar um sistema de gerenciamento de empréstimos de biblioteca, 
com foco na aplicação de princípios de design como **SOLID** e **Clean Code** para garantir um software manutenível, testável e desacoplado.

---

## 🧑‍💻 Equipe

* [Daniel Coelho dos Santos]
* [Joadson Ferreira do Nascimento]
* [Nathan Medeiros Clemente]

## ✨ Funcionalidades

O sistema implementa as seguintes funcionalidades:

* **Gestão de Usuários:** Cadastro de novos usuários.
* **Gestão de Acervo:** Cadastro de livros (Título, Autor, ISBN), com distinção entre:
    * **Livros Físicos:** Com controle de quantidade de cópias.
    * **Livros Digitais:** Sem controle de cópias.
* **Operações de Empréstimo:**
    * Realizar empréstimo de um livro para um usuário (com verificação de disponibilidade).
    * Realizar a devolução de um livro.
* **Consultas e Relatórios:**
    * Listar todos os livros do acervo e sua disponibilidade atual.
    * Gerar um relatório consolidado com o total de empréstimos para cada livro, em ordem decrescente.

## 🏛️ Arquitetura e Boas Práticas

O foco principal deste projeto foi a aplicação de boas práticas. A arquitetura foi desenhada para ser desacoplada, coesa e testável, baseando-se nos princípios **SOLID**.

### Camadas do Sistema

Utilizamos uma arquitetura em três camadas principais:

#### 1. Model (`/model`)

* Classes POJO (Plain Old Java Objects) que representam as entidades do sistema (Ex: `User.java`, `Book.java`, `Loan.java`).
* **Responsabilidade:** Apenas armazenar dados (estado). Não contêm lógica de negócio ou acesso a dados.

#### 2. Repository (`/repository`)

* Responsável pela **abstração da persistência** dos dados.
* **Boa Prática (Inversão de Dependência - 'D' do SOLID):** Usamos **Interfaces** (Ex: `UserRepository`) para definir o "contrato" (o que fazer) e
* classes de **Implementação** (Ex: `InMemoryUserRepository`) para definir o "trabalhador" (como fazer).
* Isso desacopla totalmente a lógica de negócio da forma de armazenamento. Poderíamos trocar o `Map` em memória por um banco de dados SQL real
* sem alterar **nenhuma linha** nas camadas de serviço.

#### 3. Service (`/service`)

* O **cérebro** da aplicação. Contém toda a lógica de negócio (Ex: `LoanService` verifica se um livro está disponível antes de pedir ao repositório para salvar um `Loan`).
* **Boa Prática (Injeção de Dependência):** Os Serviços dependem apenas das *interfaces* dos repositórios, que são "injetadas" em seus construtores (Injeção via Construtor).
* **Boa Prática (Responsabilidade Única - 'S' do SOLID):** Cada serviço tem uma responsabilidade clara (`UserService` cuida da lógica de usuário, `BookService` da de livros,
* e `LoanService` orquestra as operações entre eles).

---

## 🛠️ Tecnologias Utilizadas

* **Java 17 (ou superior)**
* **Maven:** Para gerenciamento de dependências e build do projeto.
* **Git & GitHub:** Para controle de versão e colaboração.

## 🚀 Compilando o Projeto

O projeto utiliza Maven. Para compilar e gerar o pacote:

1.  Certifique-se de ter o [Java JDK 17+](https://www.oracle.com/java/technologies/downloads/) e o [Apache Maven](https://maven.apache.org/download.cgi) instalados e configurados no seu PATH.
2.  Clone o repositório:
    ```bash
    git clone [https://github.com/dcoelhosantos/library-loan-system.git]
    ```
3.  Navegue até a pasta raiz do projeto:
    ```bash
    cd LIBRARY-LOAN-SYSTEM
    ```
4.  Execute o comando de build do Maven:
    ```bash
    mvn clean package
    ```
    Isso irá compilar o código, rodar os testes e criar um arquivo `.jar` no diretório `target/`.

## 🏃‍♀️ Executando o Sistema

Este projeto é uma aplicação de console (CLI).

1.  Após compilar o projeto com `mvn clean package`, você pode executá-lo diretamente.
2.  Use o comando `mvn exec:java` para rodar a classe principal:

    ```bash
    # Certifique-se de que sua classe principal se chama Main e está no pacote br.ufrn.library
    mvn exec:java -Dexec.mainClass="br.ufrn.library.Main"
    ```

3.  (Alternativa) Você também pode executar o arquivo `.jar` gerado:
    ```bash
    # O nome do .jar pode variar. Verifique o nome real na pasta target/
    java -jar target/library-loan-system-1.0-SNAPSHOT.jar
    ```
# library-loan-system
