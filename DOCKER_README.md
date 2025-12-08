# 🐳 Docker - Guia de Execução

Este projeto está configurado para rodar com Docker e Docker Compose.

## 📋 Pré-requisitos

- **Docker** instalado ([Download](https://www.docker.com/get-started))
- **Docker Compose** (vem junto com Docker Desktop)

Verificar instalação:
```bash
docker --version
docker-compose --version
```

## 🚀 Como Rodar com Docker

### **Método 1: Docker Compose (Recomendado)**

Este método sobe a aplicação + MySQL automaticamente:

```bash
# Construir e iniciar todos os serviços
docker-compose up --build

# Ou em modo detached (background)
docker-compose up -d --build
```

A aplicação estará disponível em: `http://localhost:8080`

### **Método 2: Build e Run Manual**

```bash
# 1. Construir a imagem
docker build -t todo-app .

# 2. Subir o MySQL primeiro
docker run -d \
  --name todo-mysql \
  -e MYSQL_ROOT_PASSWORD=rootpassword \
  -e MYSQL_DATABASE=todocopia \
  -p 3306:3306 \
  mysql:8.0

# 3. Aguardar MySQL iniciar (30 segundos)
sleep 30

# 4. Rodar a aplicação
docker run -d \
  --name todo-app \
  -p 8080:8080 \
  --link todo-mysql:mysql \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/todocopia \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=rootpassword \
  todo-app
```

## 🛠️ Comandos Úteis

### Ver logs
```bash
# Logs de todos os serviços
docker-compose logs -f

# Logs apenas da aplicação
docker-compose logs -f app

# Logs apenas do MySQL
docker-compose logs -f mysql
```

### Parar os serviços
```bash
# Parar (mantém containers)
docker-compose stop

# Parar e remover containers
docker-compose down

# Parar, remover containers e volumes (apaga banco de dados!)
docker-compose down -v
```

### Reiniciar
```bash
docker-compose restart
```

### Rebuild após mudanças no código
```bash
docker-compose up --build
```

### Acessar o container
```bash
# Acessar container da aplicação
docker exec -it todo-app sh

# Acessar MySQL
docker exec -it todo-mysql mysql -uroot -prootpassword
```

## 📊 Estrutura Docker

```
├── Dockerfile              # Imagem da aplicação Spring Boot
├── docker-compose.yml      # Orquestração dos serviços
├── .dockerignore          # Arquivos ignorados no build
└── application-docker.properties  # Configurações para Docker
```

## ⚙️ Configurações

### Variáveis de Ambiente (docker-compose.yml)

- **MySQL**:
  - Root password: `rootpassword`
  - Database: `todocopia`
  - Porta: `3306`

- **Aplicação**:
  - Porta: `8080`
  - Profile: `docker`

### Alterar Credenciais

Edite o arquivo `docker-compose.yml`:

```yaml
mysql:
  environment:
    MYSQL_ROOT_PASSWORD: sua_senha_aqui
```

E atualize também na seção `app`:

```yaml
app:
  environment:
    SPRING_DATASOURCE_PASSWORD: sua_senha_aqui
```

## 🔍 Verificar Status

```bash
# Ver containers rodando
docker ps

# Ver todas as imagens
docker images

# Ver volumes
docker volume ls
```

## 🐛 Solução de Problemas

### Porta 8080 já em uso
```bash
# Parar processo na porta 8080 (Windows)
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Ou altere a porta no docker-compose.yml
ports:
  - "8081:8080"  # Acesse em localhost:8081
```

### Erro de conexão com MySQL
```bash
# Verificar se MySQL está rodando
docker-compose ps

# Ver logs do MySQL
docker-compose logs mysql

# Reiniciar MySQL
docker-compose restart mysql
```

### Rebuild completo
```bash
# Remover tudo e reconstruir
docker-compose down -v
docker-compose build --no-cache
docker-compose up
```

### Limpar Docker (remover tudo não usado)
```bash
# Remover containers parados
docker container prune

# Remover imagens não usadas
docker image prune

# Limpeza completa (cuidado!)
docker system prune -a
```

## 📝 Notas

- O banco de dados é persistido em um volume Docker (`mysql_data`)
- Mesmo parando os containers, os dados permanecem
- Para resetar o banco: `docker-compose down -v`
- A aplicação aguarda o MySQL ficar saudável antes de iniciar (healthcheck)

## 🎯 Próximos Passos

1. ✅ Instale Docker
2. ✅ Execute: `docker-compose up --build`
3. ✅ Acesse: `http://localhost:8080`
4. ✅ Cadastre-se e use a aplicação!

---

**Pronto! Agora você pode rodar tudo com Docker! 🐳**


