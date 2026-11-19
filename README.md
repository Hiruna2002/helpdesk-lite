# HelpDesk Lite – Java Backend Practical Assessment

## 1. Project Overview

HelpDesk Lite is a RESTful backend application developed for managing internal employee support tickets.

The application allows users to create support tickets, view and filter tickets, assign support agents, update ticket statuses, and track status changes through a history log.

This project was developed using Java 17, Spring Boot, Spring Data JPA, and MySQL, following a layered backend architecture.

## 2. Technologies Used

| Technology | Purpose |
|---|---|
| Java 17 | Programming language |
| Spring Boot | Backend application framework |
| Spring Web | REST API development |
| Spring Data JPA | Database operations |
| MySQL 8 | Relational database |
| Maven | Dependency management and build tool |
| Jakarta Bean Validation | Request validation |
| JUnit 5 & Mockito | Automated testing |
| Postman | API testing |

## 3. Prerequisites

Make sure the following software is installed:

- JDK 17
- Maven
- MySQL
- IntelliJ IDEA
- Postman (for API testing)

## 4. Project Architecture

The application follows a layered architecture to separate responsibilities and improve maintainability.

```text
src/main/java/com/helpdesklite/
│
├── controller/
│   └── TicketController.java
│
├── service/
│   └── TicketService.java
│
├── repository/
│   ├── TicketRepository.java
│   └── TicketStatusHistoryRepository.java
│
├── entity/
│   ├── Ticket.java
│   └── TicketStatusHistory.java
│
├── dto/
│   ├── CreateTicketRequest.java
│   ├── UpdateStatusRequest.java
│   ├── AssignTicketRequest.java
│   ├── TicketResponse.java
│   └── HistoryResponse.java
│
├── enums/
│   ├── Priority.java
│   └── TicketStatus.java
│
├── exception/
│   ├── ResourceNotFoundException.java
│   ├── InvalidStatusTransitionException.java
│   ├── ApiError.java
│   └── GlobalExceptionHandler.java
│
└── HelpdeskLiteApplication.java
```

### Layer Responsibilities

- **Controller:** Handles incoming HTTP requests and returns API responses.
- **Service:** Contains business logic, including ticket creation, assignment, and status workflow validation.
- **Repository:** Handles database operations using Spring Data JPA.
- **Entity:** Represents database tables.
- **DTO:** Transfers request and response data between the client and API.
- **Exception:** Handles application errors and returns structured error responses.

## 5. Database Configuration

### Database Tables

The application uses two main tables:

**1. tickets**

Stores support ticket information, including:

- Ticket ID
- Title
- Description
- Priority
- Status
- Requester name
- Requester email
- Assignee reference
- Created and updated timestamps
- Resolved timestamp

**2. ticket_status_history**

Stores the history of ticket status changes, including:

- History ID
- Related ticket ID (foreign key)
- Previous status
- New status
- Actor reference
- Change timestamp

A foreign key relationship connects each history record to its corresponding ticket.

An index is defined on `ticket_id` and `changed_at` to support ticket history queries.

Hibernate automatically creates or updates the database tables using `ddl-auto=update` for this assessment. For production applications, versioned database migrations are recommended.

## 6. Running the Application

### Step 1: Clone the Repository

```bash
git clone <YOUR_GITHUB_REPOSITORY_URL>
```

Navigate to the project directory:

```bash
cd helpdesk-lite
```

### Step 2: Run Tests

```bash
mvn clean test
```

### Step 3: Start the Application

```bash
mvn spring-boot:run
```

The application runs at:

`http://localhost:8080`

## 7. REST API Endpoints

Base URL:

`http://localhost:8080`

| HTTP Method | Endpoint | Description |
|---|---|---|
| POST | `/api/tickets` | Create a new ticket |
| GET | `/api/tickets` | Retrieve tickets with optional filters and pagination |
| GET | `/api/tickets/{id}` | Retrieve a ticket by ID |
| PATCH | `/api/tickets/{id}/status` | Update ticket status |
| PATCH | `/api/tickets/{id}/assignee` | Assign or reassign a support agent |
| GET | `/api/tickets/{id}/history` | Retrieve ticket status history |

### 7.1 Create a Ticket

**Endpoint**

`POST /api/tickets`

**Request Body**

```json
{
  "title": "Cannot log in",
  "description": "Employee portal rejects my credentials.",
  "priority": "HIGH",
  "requesterName": "Nimal Perera",
  "requesterEmail": "nimal@example.com"
}
```

**Expected Behavior**

- Creates a new support ticket.
- Validates the required fields and email address.
- Sets the initial status to `OPEN`.
- Stores the ticket in the database.

### 7.2 Retrieve All Tickets

**Endpoint**

`GET /api/tickets`

Retrieve all tickets.

Optional filters and pagination:

`GET /api/tickets?status=OPEN&priority=HIGH&page=0&size=20`

**Query Parameters**

| Parameter | Description | Default |
|---|---|---|
| status | Filter by ticket status | No filter |
| priority | Filter by ticket priority | No filter |
| page | Page number (zero-based) | 0 |
| size | Number of records per page | 20 |

The maximum page size is limited to 100 records.

### 7.3 Retrieve a Ticket by ID

**Endpoint**

`GET /api/tickets/{id}`

**Example**

`GET /api/tickets/1`

Returns the details of the requested ticket.

If the ticket does not exist, the API returns `404 Not Found`.

### 7.4 Update Ticket Status

**Endpoint**

`PATCH /api/tickets/{id}/status`

**Request Body**

```json
{
  "status": "IN_PROGRESS",
  "actorRef": "agent-100"
}
```

**Allowed Status Workflow**

```text
OPEN
  |
  v
IN_PROGRESS
  |
  v
RESOLVED
  |
  v
CLOSED
```

Only the following transitions are permitted:

- `OPEN` → `IN_PROGRESS`
- `IN_PROGRESS` → `RESOLVED`
- `RESOLVED` → `CLOSED`

Invalid status transitions are rejected with `409 Conflict`.

A ticket can only be closed after it has been resolved.

Every successful status change creates a corresponding history record. Updating the ticket status and inserting the history record are performed within a single database transaction.

The `actorRef` field identifies the user or support agent responsible for the change.

### 7.5 Assign or Reassign a Support Agent

**Endpoint**

`PATCH /api/tickets/{id}/assignee`

**Request Body**

```json
{
  "assigneeRef": "agent-100"
}
```

**Expected Behavior**

- Assigns the ticket to the specified support agent reference.
- Allows an existing assignment to be changed.
- Stores the agent reference with the ticket.

Agent accounts and authentication are outside the minimum scope of this assessment.

### 7.6 Retrieve Ticket Status History

**Endpoint**

`GET /api/tickets/{id}/history`

**Example**

`GET /api/tickets/1/history`

Returns the status change history associated with the specified ticket.

Each history record contains:

- Previous status
- New status
- Actor reference
- Change timestamp

## 8. Validation and Error Handling

The application validates incoming requests and handles common API errors through centralized exception handling.

| HTTP Status | Description |
|---|---|
| 400 Bad Request | Invalid request data, validation errors, malformed enum values, or invalid ID values |
| 404 Not Found | Requested ticket does not exist |
| 409 Conflict | Invalid ticket status transition |
| 500 Internal Server Error | Unexpected server-side error |

### Validation Rules

- Ticket title is required and must satisfy the configured length constraints.
- Description is required.
- Priority must be one of `LOW`, `MEDIUM`, or `HIGH`.
- Requester name is required.
- Requester email must be valid.
- Ticket IDs must be valid.
- Ticket status must follow the permitted workflow.

Error responses use a consistent API error structure. Internal implementation details and stack traces are not exposed to API clients.

## 9. Automated Testing

Automated tests are implemented using JUnit 5, Mockito, and Spring Boot testing support.

The tests cover the following scenarios:

| Test Scenario | Expected Result |
|---|---|
| Successful#   h e l p d e s k - l i t e  
 #   h e l p d e s k - l i t e  
 