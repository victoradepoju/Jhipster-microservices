# JHipster Microservices Project

## Overview

This repository contains a JHipster-based microservices project consisting of a Gateway, a BookService microservice, and a JHipster Registry. This README provides detailed instructions for setting up, developing, and testing the project, including how to interact with the BookService API through the Gateway using Postman.

## Project Structure

- **JHipster Registry**: Service discovery and configuration management.
- **Gateway**: Reverse proxy and API gateway.
- **BookService**: A microservice that provides CRUD operations for managing books.

## Prerequisites

- **Java Development Kit (JDK)**: Version 11 or later
- **Maven**: For building and running Java applications
- **Docker**: For running PostgreSQL in a container
- **Postman**: For testing API endpoints

## Setup

### 1. Clone the Repositories

#### JHipster Registry

```bash
git clone https://github.com/jhipster/jhipster-registry.git
cd jhipster-registry
```

#### Gateway Application

```bash
jhipster
```

- Select **Microservices Gateway**
- Choose other options as prompted
- Set the server port to `8088` in `application-dev.yml`

#### BookService Microservice

```bash
jhipster
```

- Select **Microservice application**
- Choose PostgreSQL for both production and development databases
- Set the server port to `8099` in `application-dev.yml`
- Generate Docker configuration for PostgreSQL in `src/main/docker/postgresql.yml`

### 2. Build and Run JHipster Registry

```bash
./mvnw package
./mvnw
```

### 3. Configure PostgreSQL

Run the PostgreSQL container:

```bash
docker-compose -f src/main/docker/postgresql.yml up -d
```

### 4. Synchronize JWT Secrets

Ensure the base64 secret in `src/main/resources/config/application-dev.yml` of both the Gateway and BookService is the same to prevent `401 Unauthorized` errors.

## Development

### 1. Create the Book Entity

Generate the `Book` entity in the BookService using JHipster:

```bash
jhipster entity Book
```

Define the following fields:

- `id`: Long, auto-generated
- `title`: String, not null, unique
- `authorName`: String, not null
- `isbn`: String
- `archived`: Boolean
- `shareable`: Boolean, not null

### 2. Implement the Book Resource

The `BookResource` class provides CRUD operations for the `Book` entity.

## Running the Applications

### 1. Start the JHipster Registry

```bash
./mvnw
```

### 2. Run the Gateway Application

Open the Gateway project in IntelliJ and start it.

### 3. Run the BookService Microservice

Open the BookService project in IntelliJ and start it.

## Testing the API with Postman

### **1. Obtain a JWT Token**

**Method**: POST  
**URL**: `http://localhost:8088/api/authenticate`  
**Headers**:  
- `Content-Type`: `application/json`

**Body** (JSON):
```json
{
  "username": "admin",
  "password": "admin"
}
```

**Response**:
- **Status**: 200 OK
- **Body**: Contains JWT token:
```json
{
  "id_token": "your_jwt_token_here"
}
```

### **2. Create a New Book**

**Method**: POST  
**URL**: `http://localhost:8088/services/bookservice/api/books`  
**Headers**:  
- `Authorization`: `Bearer <your_jwt_token>`
- `Content-Type`: `application/json`

**Body** (JSON):
```json
{
  "title": "Sample Book Title",
  "authorName": "Sample Author",
  "isbn": "1234567890",
  "archived": false,
  "shareable": true
}
```

**Response**:
- **Status**: 201 Created
- **Body**: Returns the created `BookDTO`.

### **3. Update an Existing Book**

**Method**: PUT  
**URL**: `http://localhost:8088/services/bookservice/api/books/{id}`  
**Headers**:  
- `Authorization`: `Bearer <your_jwt_token>`
- `Content-Type`: `application/json`

**Body** (JSON):
```json
{
  "id": 1,
  "title": "Updated Book Title",
  "authorName": "Updated Author",
  "isbn": "0987654321",
  "archived": true,
  "shareable": false
}
```

**Response**:
- **Status**: 200 OK
- **Body**: Returns the updated `BookDTO`.

### **4. Partially Update a Book**

**Method**: PATCH  
**URL**: `http://localhost:8088/services/bookservice/api/books/{id}`  
**Headers**:  
- `Authorization`: `Bearer <your_jwt_token>`
- `Content-Type`: `application/json`

**Body** (JSON):
```json
{
  "isbn": "1111111111",
  "archived": true
}
```

**Response**:
- **Status**: 200 OK
- **Body**: Returns the partially updated `BookDTO`.

### **5. Retrieve All Books**

**Method**: GET  
**URL**: `http://localhost:8088/services/bookservice/api/books`  
**Headers**:  
- `Authorization`: `Bearer <your_jwt_token>`

**Response**:
- **Status**: 200 OK
- **Body**: Returns a list of `BookDTO` objects.

### **6. Retrieve a Specific Book**

**Method**: GET  
**URL**: `http://localhost:8088/services/bookservice/api/books/{id}`  
**Headers**:  
- `Authorization`: `Bearer <your_jwt_token>`

**Response**:
- **Status**: 200 OK
- **Body**: Returns the `BookDTO` for the specified ID.

### **7. Delete a Book**

**Method**: DELETE  
**URL**: `http://localhost:8088/services/bookservice/api/books/{id}`  
**Headers**:  
- `Authorization`: `Bearer <your_jwt_token>`

**Response**:
- **Status**: 204 No Content

## Conclusion

This README provides a detailed guide on setting up, developing, and testing the JHipster project with a focus on the BookService microservice. By following these instructions, you should be able to successfully build and interact with your JHipster applications.
