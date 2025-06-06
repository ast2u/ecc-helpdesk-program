# ecc-helpdesk-program

## Overview
The **HelpDesk Ticket System** is a multi-module application built using **Spring Boot** for the backend and **Angular** for the frontend.
The system is designed to manage employee support tickets, allowing administrators to create employees, assign roles, and oversee ticket progress.
Employees can create and update tickets, add remarks, and track their status.

## Tech Stack
### Backend (Spring Boot)
- **Spring Boot** - REST API development
- **Spring Security & JWT** - Authentication & Authorization
- **PostgreSQL** - Database management
- **Docker** - Where **PostgreSQL** is running on
- **Spring Data JPA** - ORM & Database interaction
- **Lombok** - Boilerplate code reduction
- **MapStruct** - Boilerplate code reduction mapper
- **Spring Validation** - Data validation

### Frontend (Angular)
- **Angular (Standalone)** - UI Framework
- **Bootstrap** - Styling & Components
- **@fortawesome/angular-fontawesome** - Icons
- **Angular Forms** - Reactive Forms for employee/ticket management
- **HTTP Client** - API Communication

## Features
### User Functionalities
- Profile Management

### Admin Functionalities
- Create, Read, Update, and Delete (CRUD) **Employees**
- Assign roles to employees
- CRUD **Employee Roles**
- View and manage help tickets
- Assign employees to tickets
- Add remarks to tickets

### Employee Functionalities
- CRUD **Tickets**
- Add remarks to tickets
- Track ticket status

## Data Model
- `Employee`
- `EmployeeRole`
- `HelpTicket`
- `Remarks`

## Authentication & Authorization
- **JWT Authentication**: Secures API endpoints
- **Role-Based Access Control (RBAC)**: Admins have full access, while employees have limited permissions
- **Password Encryption**: Ensures secure storage of passwords

## API Documentation
The API follows **RESTful principles**. Example endpoints:
- `POST /login` - Authenticate user & receive JWT
- `GET /api/employees` - Fetch all employees (Admin only)
- `POST /api/tickets` - Create a new help ticket (Employee only)
- `PUT /api/tickets/update/{id}` - Update ticket details
- `POST /api/tickets/{id}/remarks/add` - Add a remark to a ticket

## Running The Project
You must have Docker Desktop installed and any IDE that you are comfortable with. You will need maven installed to run the
commands or use an IDE like IntelliJ that has already built in maven or gradle so you don't need to download it.
1. Run the docker-compose.yml to deploy the database
   ``` shell
   docker-compose up -d
   ```
2. Build JAR file. You can `-Pskip-tests` to skip running of test
   ```shell
   mvn clean package
   mvn clean install
   ```
3. Run the spring application.
   ```shell
   mvn spring-boot:run -pl app
   ```
---
To check the database that are running just simply execute:
``` shell
docker exec -it my-postgres psql -U postgres
```
This will run a psql command within the postgres database. And then run this command to list all the database.
```
postgres-# \l
```

## Angular Frontend
The Angular frontend repository is hosted separately. You can find it here:  
👉 [Help Desk Angular Frontend Repository](https://github.com/ast2u/ecc-helpdesk-frontend/tree/main)