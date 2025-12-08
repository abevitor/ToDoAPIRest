# Dockerfile para aplicação Spring Boot
FROM maven:3.9-eclipse-temurin-17 AS build

# Define o diretório de trabalho
WORKDIR /app

# Copia o arquivo pom.xml primeiro (para cache de dependências)
COPY pom.xml .

# Baixa as dependências (cache se pom.xml não mudar)
RUN mvn dependency:go-offline -B

# Copia o código fonte
COPY src ./src

# Compila a aplicação
RUN mvn clean package -DskipTests

# Imagem final
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copia o JAR compilado
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta 8080
EXPOSE 8080

# Comando para executar a aplicação com profile docker
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "app.jar"]

