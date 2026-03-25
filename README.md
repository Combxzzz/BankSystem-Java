# Bank System

A command-line interface (CLI) application for managing bank accounts and transactions, built with Java and Maven. This system allows users to perform basic banking operations such as creating accounts, depositing funds, withdrawing money, transferring between accounts, and viewing account and transaction histories.

## Features

- **Account Management**: Create new bank accounts, switch between accounts, and change account statuses (Active, Blocked, Closed).
- **Transactions**: Perform deposits, withdrawals, and transfers between accounts.
- **Account Listing**: View all accounts or search for a specific account by ID.
- **Transaction History**: List all transactions for a specific account or find a transaction by ID.
- **Session Management**: Maintain a main account session for operations.

## Requirements

- Java 11 or higher
- Maven 3.6 or higher

## Installation

1. Clone the repository:
   ```
   git clone <repository-url>
   cd BankSystem
   ```

2. Compile the project using Maven:
   ```
   mvn clean compile
   ```

3. Run the application:
   ```
   mvn exec:java -Dexec.mainClass="app.Application"
   ```

## Usage

Upon starting the application, you will be prompted to create an initial account if none exists. The main menu provides the following options:

1. **Create Account**: Add a new bank account by providing a holder name.
2. **Deposit**: Deposit funds into the current main account.
3. **Withdraw**: Withdraw funds from the current main account.
4. **Transfer**: Transfer funds from the current main account to another account.
5. **Accounts**: List all accounts or find a specific account by ID.
6. **Transactions**: List transactions for an account or find a specific transaction by ID.
7. **Switch Account**: Change the main account to another existing account.
8. **Switch Account Status**: Change the status of the current main account (Active, Blocked, Closed).
0. **Exit**: Exit the application.

### Example Interaction

```
Welcome to Bank System

Create Account
Holder name: John Doe
Account created! ID: 1
Main account defined!

====================================
MAIN ACCOUNT

ID: 1
Holder: John Doe
Balance: 0
Status: ACTIVE

====================================

=== MENU ===
1 - Create Account
2 - Deposit
3 - Withdraw
4 - Transfer
5 - Accounts
6 - Transactions
7 - Switch Account
8 - Switch Account Status
0 - Exit

Option: 2

Deposit
Amount: 100.00
Description: Initial deposit
Deposit successful!
```

## Project Structure

```
BankSystem/
├── pom.xml                                     # Maven configuration
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── app/
│   │   │   │   ├── Application.java            # Main CLI application
│   │   │   │   └── Session.java                # Session management
│   │   │   │
│   │   │   ├── model/
│   │   │   │   ├── BankAccount.java            # Bank account model
│   │   │   │   ├── Transaction.java            # Transaction model
│   │   │   │   │
│   │   │   │   └── enums/
│   │   │   │       ├── AccountStatus.java      # Account status enum
│   │   │   │       └── TransactionType.java    # Transaction type enum
│   │   │   │
│   │   │   ├── repository/
│   │   │   │   ├── AbstractRepository.java     # Base repository class
│   │   │   │   ├── BankAccountRepository.java  # Bank account data access
│   │   │   │   ├── Repository.java             # Repository interface
│   │   │   │   └── TransactionRepository.java  # Transaction data access
│   │   │   │
│   │   │   └── service/
│   │   │       ├── BankAccountService.java     # Bank account business logic
│   │   │       └── TransactionService.java     # Transaction business logic
│   │   │
│   │   └── resources/                          # Application resources
│   │
│   └── test/
│       └── java/                               # Unit tests
│
└── target/                                     # Compiled classes and build artifacts
```

## Architecture

The application follows a layered architecture:

- **Presentation Layer**: `Application.java` handles user input and output via the CLI.
- **Service Layer**: `BankAccountService` and `TransactionService` contain business logic and orchestrate operations.
- **Repository Layer**: `BankAccountRepository` and `TransactionRepository` manage data persistence (in-memory for this implementation).
- **Model Layer**: `BankAccount`, `Transaction`, and enums define the data structures.

## Contributing

1. Fork the repository.
2. Create a feature branch: `git checkout -b feature-name`.
3. Commit your changes: `git commit -am 'Add new feature'`.
4. Push to the branch: `git push origin feature-name`.
5. Submit a pull request.

## License

This project is licensed under the MIT License. See the LICENSE file for details.

## Contact

For questions or support, please contact the development team.
