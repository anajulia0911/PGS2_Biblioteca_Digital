# Biblioteca Digital

Ana Julia - 10438692
Caroline Begiato - 10419181
Julia - 10426655

## Descrição
Projeto desenvolvido em Java com Spring Boot para gerenciamento de uma Biblioteca Digital.

O sistema permite cadastrar, listar, editar e excluir livros, autores, categorias, leitores e empréstimos. Também possui integração com a API Open Library para buscar informações de livros pelo ISBN.

## Tecnologias utilizadas
- Java 17
- Spring Boot
- Spring Data JPA
- Hibernate
- PostgreSQL
- Thymeleaf
- Maven
- Open Library API

## Como executar o projeto

# 1. Clone o repositório
git clone https://github.com/anajulia0911/PGS2_Biblioteca_Digital

# 2. Entre na pasta do projeto
cd PGS2_Biblioteca_Digital

# 3. Configure o banco de dados no arquivo:
# src/main/resources/application.properties

# Exemplo de configuração:
spring.datasource.url=jdbc:postgresql://localhost:5432/biblioteca_digital
spring.datasource.username=postgres
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update

# 4. Execute o projeto
mvn spring-boot:run

# 5. Acesse no navegador
http://localhost:8080

## Funcionalidades
- Cadastro de livros
- Cadastro de autores
- Cadastro de categorias
- Cadastro de leitores
- Controle de empréstimos
- Busca de livros por ISBN
- Integração com Open Library
- Interface web com Thymeleaf
- API REST

## Principais endpoints

# Listar livros
GET /api/livros

# Buscar livro por ISBN na Open Library
GET /api/livros/preview/{isbn}

# Salvar livro usando ISBN
POST /api/livros/isbn/{isbn}

# Consulta agregada
GET /api/livros/agregado

# Excluir livro
DELETE /api/livros/{id}

## Estrutura do projeto

src/main/java
├── controller
├── service
├── repository
├── model
└── BibliotecaDigitalApplication.java

src/main/resources
├── templates
├── static
└── application.properties
## Diagrama de Classes

```plantuml
@startuml
package "br.mackenzie.bibliotecamack.model" {

    class Autor {
        - Long id
        - String nome
        - String dataNascimento
    }

    class Categoria {
        - Long id
        - String nome
        - String descricao
    }

    class Livro {
        - Long id
        - String titulo
        - String isbn
        - String editora
        - Autor autor
        - Categoria categoria
    }

    class Leitor {
        - Long id
        - String nome
        - String email
        - String registroAcademico
    }

    class Emprestimo {
        - Long id
        - Date dataEmprestimo
        - Date dataDevolucaoPrevista
        - int status
        - Leitor leitor
        - List<Livro> livros
    }
}

Autor "1" -- "0..*" Livro : possui >
Categoria "1" -- "0..*" Livro : classifica >
Leitor "1" -- "0..*" Emprestimo : solicita >
Emprestimo "0..*" -- "1..*" Livro : contém >

@enduml
```
# Diagrama de Sequência no README

```markdown
## Diagrama de Sequência

```plantuml
@startuml

actor Usuario
participant "LivroController" as Ctrl
participant "LivroService" as Serv
participant "OpenLibrary API" as ExtAPI
database "PostgreSQL" as DB

Usuario -> Ctrl : POST /api/livros/isbn/{isbn}

Ctrl -> Serv : salvarComOpenLibrary(isbn)

activate Serv

Serv -> ExtAPI : GET /api/books?bibkeys=ISBN:{isbn}&format=json

ExtAPI --> Serv : JSON Payload

Serv -> Serv : Parse JSON

Serv -> DB : save(novoLivro)

DB --> Serv : ID Gerado

Serv --> Ctrl : Livro salvo

deactivate Serv

Ctrl --> Usuario : HTTP 201 Created

@enduml
```
## Como enviar para o GitHub

# Inicializar o Git
git init

# Adicionar os arquivos
git add .

# Criar o primeiro commit
git commit -m "Primeiro commit - Biblioteca Digital"

# Conectar ao repositório remoto
git remote add origin https://github.com/anajulia0911/PGS2_Biblioteca_Digital
# Enviar para o GitHub
git branch -M main
git push -u origin main

## Autor
Projeto desenvolvido para a disciplina de Programação de Sistemas II.
