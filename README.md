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
- **Spring Data JPA** - ORM & Database interaction
- **Lombok** - Boilerplate code reduction
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

## Deployment
1. Build JAR file. You can `-Pskip-tests` to skip running of test
   ```shell
   mvn clean package
   mvn clean install
   ```
2. Run the spring application.
   ```shell
   mvn spring-boot:run -pl app
   ```

## Angular Frontend
The Angular frontend repository is hosted separately. You can find it here:  
👉 [Help Desk Angular Frontend Repository](https://github.com/ast2u/ecc-helpdesk-frontend/tree/main)