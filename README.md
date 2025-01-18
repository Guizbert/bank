# Banking API Documentation

## Overview
This project provides a RESTful API for managing banking operations such as user account creation, login, balance inquiries, crediting and debiting accounts, and transferring funds. It includes a well-structured backend built using Java and Spring Boot with Swagger/OpenAPI documentation for easy API exploration.

**I made this project to learn more about Spring Boot**
---

## Technologies Used
- **Java**
- **Spring Boot**: Framework for building the API.
- **Swagger/OpenAPI**: For API documentation.
- **Lombok**: Reduces boilerplate code.
- **Maven**: Build and dependency management tool.

---

## Project Structure

### Controllers
**TransactionController**: Manages user transactions and bank statements.

**UserController**: Manages user accounts and related operations.

---

### Utility Class: `AccountUtils`
This class provides helper methods and constants to ensure consistency in responses and unique account generation.

#### Features
- **Response Codes and Messages**
  - Predefined codes and messages for standard API responses.
- **Account Number Generator**
  - Dynamically creates unique account numbers based on the current year and a random number.
---

### Data Transfer Objects (DTOs)
DTOs are used for transferring data between client and server while maintaining a clear structure.

- **AccountInformationDto**: Contains user account details such as name, balance, and account number.
- **BankResponseDto**: Holds API response codes, messages, and account details.
- **CreditDebitRequestDto**: Manages credit or debit transactions.
- **EmailDto**: Facilitates email-related operations.
- **EnquiryRequestDto**: Handles balance and name inquiries.
- **LoginDto**: Contains user login credentials.
- **TransactionDto**: Represents transaction details.
- **TransfertRequestDto**: Manages fund transfers.
- **UserDto**: Represents user details and registration data.

---

## API Documentation
The API is documented using Swagger. Navigate to `http://<server>/swagger-ui.html` to view and test the available endpoints.

**Key Annotations**
- `@RestController`: Defines controllers.
- `@RequestMapping`: Maps requests to specific methods.
- `@Tag`: Groups API operations.
- `@Operation`: Documents individual operations.
- `@ApiResponse`: Specifies possible responses.

