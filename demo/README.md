# GrievanceTrack — Citizen Complaint Tracking with SLA Escalation

## Project Description
GrievanceTrack is a simple Spring Boot REST API for managing municipal citizen complaints. Citizens can register grievances for roads, water, or garbage issues. The system routes each grievance to the correct department based on its category, tracks the complaint through status stages, and automatically escalates unresolved cases when the SLA expires.

## Technologies Used
- Java 17
- Spring Boot 4.1.1
- Spring Web
- Spring Data JPA
- MySQL
- Jakarta Bean Validation
- Gradle

## Database Setup
1. Open MySQL Workbench.
2. Create a database named `grievance_track_db`.
3. Ensure the user has access to the database and set a password.

## Configure MySQL
Update the database credentials in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/grievance_track_db?createDatabaseIfNotExist=true&serverTimezone=UTC
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:your_password}
```

You can provide a password at runtime with environment variables:

```bash
set DB_USERNAME=root
set DB_PASSWORD=your_password
```

## Run the Application
From the project root:

```bash
./gradlew bootRun
```

On Windows:

```bash
gradlew.bat bootRun
```

## Database Tables
The application creates these tables automatically through JPA:
- departments
- categories
- grievances
- escalations

## Entity Relationships
- Department to Category: one-to-many
- Category to Grievance: one-to-many
- Department to Grievance: one-to-many
- Grievance to Escalation: one-to-one

## API Endpoints
### Department APIs
- POST /api/departments
- GET /api/departments
- GET /api/departments/{id}
- PUT /api/departments/{id}
- DELETE /api/departments/{id}

### Category APIs
- POST /api/categories
- GET /api/categories
- GET /api/categories/{id}
- PUT /api/categories/{id}
- DELETE /api/categories/{id}

### Grievance APIs
- POST /api/grievances
- GET /api/grievances
- GET /api/grievances/{id}
- PUT /api/grievances/{id}/status
- PUT /api/grievances/{id}/rating
- DELETE /api/grievances/{id}

### Escalation APIs
- GET /api/escalations
- GET /api/escalations/{id}
- GET /api/grievances/{grievanceId}/escalation

## Example Requests
### Create Department
```json
{
  "name": "Public Works Department"
}
```

### Create Category
```json
{
  "name": "Road",
  "slaDays": 3,
  "departmentId": 1
}
```

### Create Grievance
```json
{
  "categoryId": 1,
  "description": "Large pothole near the main road",
  "location": "Anna Nagar"
}
```

### Update Status
```json
{
  "status": "ASSIGNED"
}
```

### Submit Rating
```json
{
  "rating": 5,
  "ratingComment": "The road was repaired quickly."
}
```

## Sample Response
### Grievance Created
```json
{
  "id": 1,
  "description": "Large pothole near the main road",
  "location": "Anna Nagar",
  "status": "OPEN",
  "category": {
    "id": 1,
    "name": "Road",
    "slaDays": 3
  },
  "department": {
    "id": 1,
    "name": "Public Works Department"
  },
  "createdAt": "2026-09-28T11:30:00",
  "updatedAt": "2026-09-28T11:30:00"
}
```

## Business Rules
- Each category has a fixed SLA in days.
- Department is assigned automatically based on category.
- Grievance status follows: OPEN → ASSIGNED → IN_PROGRESS → RESOLVED → CLOSED.
- If SLA expires before closure, the grievance is escalated to a senior officer.
- A grievance cannot be escalated twice.
- Rating can only be given when the grievance is closed.
- Rating ranges from 1 to 5.

## SLA Escalation
The scheduled task checks open grievances every 60 seconds. If the current time is after the grievance’s created time plus category SLA days, a new escalation record is created and the grievance status changes to `ESCALATED`.

## Testing in Postman
1. Start the application.
2. Create departments first.
3. Create categories and attach each one to a department.
4. Create a grievance using a valid category ID.
5. Update the grievance status through the workflow.
6. Validate rating rules and SLA escalation.

## Notes
- No login or security layer has been added.
- No frontend, payment, or unrelated modules are included.
- This project is kept intentionally simple for a college-level backend assignment.
