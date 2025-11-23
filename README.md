# 📚 Sistema de Gerenciamento de Biblioteca (Library Loan System)

![Java](https://img.shields.io/badge/Java-21-blue?logo=java)
![Maven](https://img.shields.io/badge/Maven-3.8%2B-red?logo=apachemaven)
![License](https://img.shields.io/badge/License-MIT-green)

Projeto da disciplina de Boas Práticas de Programação. O objetivo é criar um sistema de gerenciamento de empréstimos de biblioteca,
com foco na aplicação de princípios de design como **SOLID** e **Clean Code** para garantir um software manutenível, testável e desacoplado.

---

## 🧑‍💻 Autor

* [Joadson Ferreira do Nascimento]

## ✨ Funcionalidades

O sistema implementa as seguintes funcionalidades através de um menu interativo no console:

* **Gestão de Usuários:**
    * Cadastro de novos usuários.
    * Listagem de todos os usuários cadastrados.
* **Gestão de Acervo:** Cadastro de livros (Título, Autor, ISBN), com distinção entre:
    * **Livros Físicos:** Com controle de quantidade de cópias.
    * **Livros Digitais:** Sem controle de cópias (sempre disponíveis).
* **Operações de Empréstimo:**
    * Realizar empréstimo de um livro para um usuário (com verificação de disponibilidade).
    * Realizar a devolução de um livro.
* **Consultas e Relatórios:**
    * **Listar Livros e Disponibilidade:** Mostra todos os livros do acervo e sua disponibilidade atual.
    * **Relatório Consolidado de Empréstimos:** Mostra o total de empréstimos para cada livro (ordem descendente) e o total geral.
    * **Listar Empréstimos Ativos:** Mostra todos os empréstimos que ainda não foram devolvidos.
* **Utilitários:**
    * **Carregar Dados:** "Semeia" (seed) o sistema com um conjunto de dados de teste (livros, usuários e empréstimos) para permitir o teste imediato das funcionalidades.


## 🏛️ Arquitetura e Boas Práticas

O foco principal deste projeto foi a aplicação de boas práticas. A arquitetura foi desenhada para ser desacoplada, coesa e testável, baseando-se nos princípios **SOLID**.

### Camadas do Sistema

Utilizamos uma arquitetura em camadas principais:

#### 1. Model (`/model`)

* Classes POJO (Plain Old Java Objects) que representam as entidades do sistema (Ex: `User.java`, `Book.java`, `Loan.java`).
* **Boa Prática (Encapsulamento):** As próprias classes modelo são responsáveis por garantir sua integridade. A lógica de validação (ex: `totalCopies` não pode ser negativo) está nos construtores e *setters*.

#### 2. Repository (`/repository`)

* Responsável pela **abstração da persistência** dos dados.
* **Boa Prática (Inversão de Dependência - 'D' do SOLID):** Usamos **Interfaces** (Ex: `UserRepository`) para definir o "contrato" (o que fazer) e classes de **Implementação** (Ex: `InMemoryUserRepository`) para definir o "trabalhador" (como fazer).
* Isso desacopla totalmente a lógica de negócio da forma de armazenamento.

#### 3. Service (`/service`)

* O **cérebro** da aplicação. Contém toda a lógica de negócio (Ex: `LoanService` verifica se um livro está disponível antes de pedir ao repositório para salvar um `Loan`).
* **Boa Prática (Injeção de Dependência):** Os Serviços dependem apenas das *interfaces* dos repositórios, que são "injetadas" em seus construtores.
* **Boa Prática (Responsabilidade Única - 'S' do SOLID):** Cada serviço tem uma responsabilidade clara (`UserService` cuida da lógica de usuário, `BookService` da de livros, e `LoanService` orquestra as operações entre eles).

#### 4. DTO (Data Transfer Object) (`/dto`)

* Classes "burras" usadas para **transferir dados** formatados ou agregados para a camada de visualização (Ex: `BookAvailabilityDTO`, `LoanReportDTO`), mantendo os relatórios limpos.

#### 5. View (CLI) (`/cli` e `Library.java`)

* A camada de **Visualização (View)** é composta pela classe `Library.java` (o "dispatcher" principal) e as classes no pacote `/cli`.
* **Boa Prática (Responsabilidade Única - 'S' do SOLID):** A lógica de interação com o console foi separada em classes `*ConsoleHandler` (Ex: `BookConsoleHandler`, `LoanConsoleHandler`).
* A `Library.java` é responsável apenas por inicializar o sistema e despachar as ações, enquanto os `Handlers` são responsáveis por coletar a entrada do usuário e formatar a saída.

---

## 🛠️ Tecnologias Utilizadas

* **Java 21 (ou 17+)**
* **Maven:** Para gerenciamento de dependências e build do projeto.
* **Git & GitHub:** Para controle de versão e colaboração.

## 🚀 Compilando o Projeto

O projeto utiliza Maven. Para compilar e gerar o pacote:

1.  Certifique-se de ter o [Java JDK 17+](https://www.oracle.com/java/technologies/downloads/) e o [Apache Maven](https://maven.apache.org/download.cgi) instalados e configurados no seu PATH.
2.  Clone o repositório:
    ```bash
    git clone https://github.com/dcoelhosantos/library-loan-system.git
    ```
3.  Navegue até a pasta raiz do projeto:
    ```bash
    cd library-loan-system
    ```
4.  Execute o comando de build do Maven:
    ```bash
    mvn clean package
    ```
    Isso irá compilar o código, rodar os testes e criar um arquivo `.jar` no diretório `target/`.

## 🏃‍♀️ Executando o Sistema

Este projeto é uma aplicação de console (CLI). A forma mais fácil de executar é via Maven:

1.  No terminal, na raiz do projeto (`library-loan-system`), execute:

    ```bash
    mvn exec:java -Dexec.mainClass="br.ufrn.library.Library"
    ```

2.  (Alternativa) Você também pode executar o arquivo `.jar` gerado:
    ```bash
    # O nome do .jar pode variar. Verifique o nome real na pasta target/
    # (Provavelmente será Library-1.0-SNAPSHOT.jar)
    java -jar target/Library-1.0-SNAPSHOT.jar
    ```

### Como Usar

Após iniciar, você verá um menu interativo.

**Importante:** Como não há banco de dados, o sistema começa vazio. **Use a Opção 9 ("Carregar Dados")** primeiro. Isso irá "semear" (seed) o sistema com 10 usuários, 20 livros e 13 empréstimos, permitindo que você teste imediatamente as funcionalidades de listagem e relatórios (Opções 5, 6, 7 e 8).
