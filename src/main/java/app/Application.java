package app;

import model.BankAccount;
import model.Transaction;
import repository.BankAccountRepository;
import repository.TransactionRepository;
import service.BankAccountService;
import service.TransactionService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        BankAccountRepository bankAccountRepository = new BankAccountRepository();
        TransactionRepository transactionRepository = new TransactionRepository();

        BankAccountService bankAccountService = new BankAccountService(
                bankAccountRepository,
                transactionRepository
        );
        TransactionService transactionService = new TransactionService(transactionRepository);

        Session session = new Session();
        session.mainAccount = null;

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("You don't have a main account yet, create one now.");
        createAccount(scanner, bankAccountService, session);

        while (running) {
            running = menu(scanner, bankAccountService, transactionService, session);
        }
    }

    public static boolean menu(Scanner scanner,
                               BankAccountService bankAccountService,
                               TransactionService transactionService,
                               Session session) {

        System.out.println("ID: " + session.mainAccount.getAccountId());
        System.out.println("Holder: " + session.mainAccount.getHolder());
        System.out.println("Account status: " + session.mainAccount.getAccountStatus());
        System.out.println("Balance: " + session.mainAccount.getBalance());

        System.out.println("\n=== BANK SYSTEM ===");
        System.out.println("1 - Create account");
        System.out.println("2 - Deposit");
        System.out.println("3 - Withdraw");
        System.out.println("4 - Transfer");
        System.out.println("5 - List/Find accounts");
        System.out.println("6 - List/Find transactions");
        System.out.println("0 - Exit");

        int option;
        try {
            System.out.print("Option: ");
            option = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Invalid entry");
            scanner.nextLine();
            return true;
        }

        switch (option) {
            case 1 -> createAccount(scanner, bankAccountService, session);
            case 2 -> deposit(scanner, bankAccountService, session);
            case 3 -> withdraw(scanner, bankAccountService, session);
            case 4 -> transfer(scanner, bankAccountService, session);
            case 5 -> listOrFindAccount(scanner, bankAccountService);
            case 6 -> listOrFindTransaction(scanner, bankAccountService, transactionService);
            case 0 -> { return false; }
        }

        return true;
    }

    public static void createAccount(Scanner scanner, BankAccountService bankAccountService, Session session) {
        System.out.print("\nHolder name: ");
        String name = scanner.nextLine();

        BankAccount account = new BankAccount(name);
        bankAccountService.saveBankAccount(account);

        if (session.mainAccount == null) {
            session.mainAccount = account;
            System.out.println("Main account defined!");
        }

        System.out.println("Account created with ID " + account.getAccountId());
        System.out.println("press ENTER to continue...");
        scanner.nextLine();
    }

    public static void deposit(Scanner scanner, BankAccountService bankAccountService, Session session) {
        System.out.print("\nEnter the deposit amount: ");
        BigDecimal amount = scanner.nextBigDecimal();
        scanner.nextLine();

        System.out.print("Enter a description(Optional): ");
        String description = scanner.nextLine();

        bankAccountService.deposit(session.mainAccount.getAccountId(), amount, description);
    }

    public static void withdraw(Scanner scanner, BankAccountService bankAccountService, Session session) {
        System.out.print("\nEnter the withdraw amount: ");
        BigDecimal amount = scanner.nextBigDecimal();
        scanner.nextLine();

        System.out.print("Enter a description(Optional): ");
        String description = scanner.nextLine();

        bankAccountService.withdraw(session.mainAccount.getAccountId(), amount, description);
    }

    public static void transfer(Scanner scanner, BankAccountService bankAccountService, Session session) {
        System.out.print("\nEnter the amount to be transferred: ");
        BigDecimal amount = scanner.nextBigDecimal();
        scanner.nextLine();

        System.out.print("Enter the receiver's account ID: ");
        int destinationId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter a description: ");
        String description = scanner.nextLine();

        bankAccountService.transfer(session.mainAccount.getAccountId(), destinationId, amount, description);
    }

    public static void listOrFindAccount(Scanner scanner, BankAccountService bankAccountService) {
        System.out.print("(L)ist all accounts or (F)ind specific account: ");
        String option = scanner.nextLine();

        switch (option.toUpperCase().charAt(0)) {
            case 'L':
                List<BankAccount> bankAccounts = bankAccountService.findAll();
                System.out.println("--------------------------------------------------------");
                for (BankAccount bankAccount : bankAccounts) {
                    printAccount(bankAccount);
                    System.out.println("--------------------------------------------------------");
                }
                System.out.println();
                break;
            case 'F':
                System.out.print("Enter the account ID to be searched: ");
                BankAccount account = bankAccountService.findById(scanner.nextInt());

                System.out.println("--------------------------------------------------------");
                printAccount(account);
                System.out.println("--------------------------------------------------------");
                break;
        }
    }

    public static void listOrFindTransaction(Scanner scanner,
                                             BankAccountService bankAccountService,
                                             TransactionService transactionService) {
        System.out.print("(L)ist all specific account transactions or (F)ind a specific transaction: ");
        String option = scanner.nextLine();

        switch (option.toUpperCase().charAt(0)) {
            case 'L':
                System.out.print("Type the account ID: ");
                List<Transaction> transactions = bankAccountService
                        .findAllAccountTransactions(scanner.nextInt());

                System.out.println("--------------------------------------------------------");
                for (Transaction transaction : transactions) {
                    printTransaction(transaction);
                    System.out.println("--------------------------------------------------------");
                }
                System.out.println();
                break;
            case 'F':
                System.out.println("Enter the transaction ID to be searched: ");
                Transaction transaction = transactionService.findById(scanner.nextInt());

                System.out.println("--------------------------------------------------------");
                printTransaction(transaction);
                System.out.println("--------------------------------------------------------");
                break;
        }
    }

    private static void printAccount(BankAccount bankAccount) {
        System.out.println("ID: "               + bankAccount.getAccountId());
        System.out.println("Holder: "           + bankAccount.getHolder());
        System.out.println("Balance: "          + bankAccount.getBalance());
        System.out.println("Status: "           + bankAccount.getAccountStatus());
        System.out.println("Create date: "      + bankAccount.getCreatedAt());
    }

    private static void printTransaction(Transaction transaction) {
        System.out.println("ID: "               + transaction.getTransactionId());
        System.out.println("Sender ID: "        + transaction.getSenderBankAccount().getAccountId());
        System.out.println("Receiver's ID: "    + transaction.getSenderBankAccount().getAccountId());
        System.out.println("Type: "             + transaction.getTransactionType());
        System.out.println("Amount: "           + transaction.getAmount());
        System.out.println("Create date: "      + transaction.getDate());
        System.out.println("Description: "      + (transaction.getDescription().isEmpty() || transaction.getDescription() == null ?
                "null" : transaction.getDescription()));
    }
}
