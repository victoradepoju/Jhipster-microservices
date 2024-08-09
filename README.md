# JHipster Microservices Project

## Overview

This repository contains a JHipster-based microservices project consisting of a Gateway, a BookService microservice, and a JHipster Registry. 
JHipster is a free and open-source application generator used to quickly develop modern web applications and Microservices using Angular or React and the Spring Framework. This README provides detailed instructions for setting up, developing, and testing the project, including how to interact with the BookService API through the Gateway using Postman.

## Project Structure

- **registry**: Service discovery and configuration management.
- **gateway**: Reverse proxy and API gateway.
- **book-service**: A microservice that provides CRUD operations for managing books.

## Prerequisites

- **Java Development Kit (JDK)**: Version 17 or earlier
- **Maven**: For building and running Java applications
- **Docker**: For running PostgreSQL in a container
- **Postman**: For testing API endpoints
- **Node.js**: Version 16.0 or later, required for running frontend builds and JHipster CLI
- **npm**: Comes with Node.js, used for managing JavaScript packages
- **JHipster**: a development platform to quickly generate, develop, and deploy modern web applications + microservice architectures

### Installing JHipster

To install JHipster, run the following command:

```bash
npm install -g generator-jhipster
```

This will globally install the JHipster generator, allowing you to create JHipster projects using the command:

```bash
jhipster
```

For a more detailed breakdown of the installation process, [here](https://www.jhipster.tech/installation/) is a link to the official JHipster page on how to install JHipster and it's prerequisites.

## Setup

### 1. Clone the Repositories

#### JHipster Registry

```bash
mkdir registry
cd registry
git clone https://github.com/jhipster/jhipster-registry.git
cd jhipster-registry
```

#### Gateway Application

```bash
jhipster
```

During the prompts, select the following options:
- **What type of application would you like to create?**: Gateway application
- **What is your default Java package name**: // your choice
- **Would you like to use Maven or Gradle for building the backend**: Maven
- **As you are running a microservices architecture, on which port would you like your server to run? It should be unique to avoid port conflict**: 8088
- **Which service discovery server do you want to use**: JHipster Registry (legacy, uses Eureka, provides Spring Cloud Config support)
- **Which type of authentication would you like to use?**: JWT authentication (stateless, with a token)
- **Which tyoe of database would you like to use**: No database
- **Development Database**: No database
- **Production Database**: No database
- **Which framework would you like to use for the client?** No client
- **Other options**: Select according to your preference.


#### BookService Microservice

```bash
jhipster
```

During the prompts, select the following options:
- **What type of application would you like to create?**: Microservice application
- **What is your default Java package name**: // your choice
- **Would you like to use Maven or Gradle for building the backend**: Maven
- **Do you want to make it reactive with Spring WebFlux?** No
- **As you are running a microservices architecture, on which port would you like your server to run? It should be unique to avoid port conflict**: 8099
- **Which service discovery server do you want to use**: JHipster Registry (legacy, uses Eureka, provides Spring Cloud Config support)
- **Which type of authentication would you like to use?**: JWT authentication (stateless, with a token)
- **Do you want to generate a feign client?** // your choice
- **Which tyoe of database would you like to use**: SQL
- **Production Database**: Postgres
- **Development Database**: Postgres
- **Which framework would you like to use for the client?** No client
- **Other options**: Select according to your preference.

### 2. Build and Run JHipster Registry

```bash
./mvnw
```

After running, you should have something similar to the image below:

![Screenshot (234)](https://github.com/user-attachments/assets/ff3b3b1c-b09f-4607-827b-acf3824754a0)

### 3. Configure PostgreSQL

Launch your Docker desktop app, then run and start the PostgreSQL container:

```bash
docker-compose -f src/main/docker/postgresql.yml up -d
```
From IntelliJ, you can achieve this by clicking the green run button beside the 'postgresql' as shown below:

![Screenshot (233)](https://github.com/user-attachments/assets/8d19c862-e6ba-4574-8a3f-a7da05d98603)

### 4. Synchronize JWT Secrets

Ensure the base64 secret in `src/main/resources/config/application-dev.yml` of both the Gateway and BookService is the same to prevent `401 Unauthorized` errors.

## Development

### 1. Create the Book Entity

Generate the `Book` entity in the BookService using JHipster:

```bash
jhipster entity Book
```

Define the following fields:

- `title`: String, not null, unique
- `authorName`: String, not null
- `isbn`: String
- `archived`: Boolean
- `shareable`: Boolean, not null

Select the following prompts as it fits your preference.
At the end, JHipster will generate the entity, relationship (if any, none in this case), repository, service, and resource (REST API controller), DTO, tests etc.

### 2. Implement the Book Resource (generated)

The `BookResource` class provides CRUD operations for the `Book` entity.

## Running the Applications

### 1. Make sure the JHipster Registry is running

Remember, we achieved this using the command:
```bash
./mvnw
```

Open http://localhost:8761 on your browser of choice and you should be redirected to the sign in page. The default username and password is **admin**:

![Screenshot (235)](https://github.com/user-attachments/assets/a5bcbce3-3897-49ff-93dc-862827d7f3ab)

After signing in, the interface of the home page is shown below. Notice that the Instances Register is empty.

![Screenshot (236)](https://github.com/user-attachments/assets/d3aae2fe-6ecb-4811-8c13-eb9624c32770)

### 2. Run the Gateway Application

First, make sure you set the Active Profile to **dev** in the configuration.

![Screenshot (238)](https://github.com/user-attachments/assets/bc3b1582-05a6-4b06-96c3-a503b8e3b4aa)

Open the Gateway project in IntelliJ (or any preferred IDE) and run it.
Remember, the gateway and microservice apps use no front end framework (no client) so running them from the IDE (main) will only start the backend.

![Screenshot (239)](https://github.com/user-attachments/assets/ea465bfd-2568-4450-8330-7a25f3a95877)

Upon successful running, you should have:

![Screenshot (240)](https://github.com/user-attachments/assets/2598286c-233a-4be1-ad8b-f0d31a28d9b1)

Check to registry home page again and notice that it has included the Gateway as one of the registered instances:

![Screenshot (241)](https://github.com/user-attachments/assets/88634836-a65e-471e-9224-5a4047e3ee69)

### 3. Run the BookService Microservice

Make sure the docker container we created earlier is still running:

![Screenshot (242)](https://github.com/user-attachments/assets/edad4e8f-6be5-4573-b652-afe151f11da1)

Again, don't forget to set Active Profile to **dev**.
Open the BookService project in IntelliJ and start it the application like we did the gateway app

At the end, you should have:

![Screenshot (243)](https://github.com/user-attachments/assets/e2b12442-fc6b-4f22-91f4-9436fbcac306)

Notice that you now have 2 instances registered in the registry:

![Screenshot (244)](https://github.com/user-attachments/assets/ec4d3078-2b6e-4fc8-8d84-9ae9c1246a0c)

Pretty cool, right?

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

NOTE: the book microservice is accessed from the gateway through `/services/bookservice/**`

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
