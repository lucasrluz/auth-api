# Auth API

![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)

Este projeto é uma API construída usando **Java, Spring Boot, PostgresSQL, JWT.**

A API foi desenvolvida para por em meu portifólio e mostrar os meus conhecimentos em Autenticação e Autorização com JWT.

## Seções

- [Instalação](#instalação)
- [Rodar Aplicação](#rodar-aplicação)
- [API Endpoints](#api-endpoints)
- [Autenticação](#autenticação)
- [Banco de Dados](#banco-de-dados)

## Instalação

1. Baixe o repositório:

```bash
git clone https://github.com/lucasrluz/auth-api
```

2. Instale as dependências do Maven

3. Instale o [PostgresSQL](https://www.postgresql.org/)

## Rodar Aplicação

1. Inicie a aplicação com o Maven
2. Acesse a rota: http://localhost:8080


## API Endpoints

```markdown
POST /api/v1/auth/signup - Cria uma conta no sistema.
POST /api/v1/auth/signin - Faz o login no sistema retornando um JWT.

PUT /api/v1/user - Edita as informações do usuário.
```

## Autenticação
Para acessar as rotas protegidas ```PUT /api/v1/user``` , crie um conta, faça o login e depois forneça o JWT nas rotas protegidas.

## Banco de Dados
Para conexão do Banco de Dados coloque no seu sistema as variáveis de ambiente correspondentes aos valores do [application.yml](https://github.com/lucasrluz/note/blob/main/src/main/resources/application.yml) ou coloque direto no arquivo substituindo os valores ```${VARIABLE}```.
