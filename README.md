# 📋 ToDo App - Gerenciador de Tarefas com Spring Boot

Este projeto é uma API REST desenvolvida com Spring Boot para gerenciamento de tarefas (ToDo). Ele permite que usuários se cadastrem, façam login e gerenciem suas próprias tarefas. A autenticação é feita via JWT (JSON Web Token).

## 🧰 Tecnologias Utilizadas

- Java 17
- Spring Boot 3
- Spring Data JPA
- Spring Security
- H2 Database (pode ser substituído por outro)
- JWT (JSON Web Token)
- Maven
- Thunder Client (para testes)
- BCrypt para criptografia de senhas

---

## 🚀 Funcionalidades

- Cadastro de usuários
- Login com autenticação JWT
- Criação, listagem, atualização e exclusão de tarefas
- Validação de credenciais com senha criptografada
- Cada usuário só acessa suas próprias tarefas
- Proteção de rotas via Spring Security

---

## 🔐 Autenticação JWT

Após o login, um token JWT é gerado e deve ser enviado nas requisições autenticadas via header:


---

## 📦 Endpoints da API

### 🔑 Autenticação

| Método | Endpoint          | Descrição        |
|--------|-------------------|------------------|
| POST   | `/auth/login`     | Login do usuário |

### 👤 Usuários

| Método | Endpoint         | Descrição                  |
|--------|------------------|----------------------------|
| POST   | `/usuarios`      | Cadastro de usuário        |
| GET    | `/usuarios/{id}` | Buscar usuário por ID      |
| DELETE | `/usuarios/{id}` | Deletar usuário            |

### ✅ Tarefas (Requer Token)

| Método | Endpoint             | Descrição                        |
|--------|----------------------|----------------------------------|
| GET    | `/tarefas`           | Listar tarefas do usuário logado|
| POST   | `/tarefas`           | Criar nova tarefa                |
| PUT    | `/tarefas/{id}`      | Atualizar tarefa existente       |
| DELETE | `/tarefas/{id}`      | Deletar tarefa                   |

---

## ⚙️ Como Rodar Localmente

1. Clone o repositório:
```bash
git clone https://github.com/seuusuario/todo-app.git
