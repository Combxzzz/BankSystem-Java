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

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        BankAccountService bankService = new BankAccountService(
                new BankAccountRepository(),
                new TransactionRepository()
        );

        TransactionService transactionService = new TransactionService(new TransactionRepository());

        Session session = new Session();

        println("\nWelcome to Bank System");
        createAccount(bankService, session);

        while (true) {
            clearScreen();
            showHeader(session);

            int option = readInt("""
                    
                    === MENU ===
                    1 - Create Account
                    2 - Deposit
                    3 - Withdraw
                    4 - Transfer
                    5 - Accounts
                    6 - Transactions
                    0 - Exit
                    
                    Option:""");

            switch (option) {
                case 1 -> createAccount(bankService, session);
                case 2 -> deposit(bankService, session);
                case 3 -> withdraw(bankService, session);
                case 4 -> transfer(bankService, session);
                case 5 -> accountsMenu(bankService);
                case 6 -> transactionsMenu(bankService, transactionService);
                case 0 -> {
                    println("Exiting...");
                    return;
                }
                default -> println("Invalid option");
            }

            pause();
        }
    }

    // ================= UI HELPERS =================

    private static void showHeader(Session session) {
        BankAccount acc = session.mainAccount;

        println("====================================");
        println("MAIN ACCOUNT");
        println("");
        println("ID: " + acc.getAccountId());
        println("Holder: " + acc.getHolder());
        println("Balance: " + acc.getBalance());
        println("Status: " + acc.getAccountStatus());
        println("");
        println("====================================");
    }

    private static void println(String text) {
        System.out.println(text);
    }

    private static void pause() {
        println("\nPress ENTER to continue...");
        scanner.nextLine();
    }

    private static int readInt(String message) {
        while (true) {
            try {
                System.out.print(message);
                int value = scanner.nextInt();
                scanner.nextLine();
                return value;
            } catch (Exception e) {
                println("Invalid number");
                scanner.nextLine();
            }
        }
    }

    private static BigDecimal readBigDecimal(String message) {
        while (true) {
            try {
                System.out.print(message);
                BigDecimal value = scanner.nextBigDecimal();
                scanner.nextLine();
                return value;
            } catch (Exception e) {
                println("Invalid value");
                scanner.nextLine();
            }
        }
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // ================= FEATURES =================

    private static void createAccount(BankAccountService service, Session session) {
        println("\nCreate Account");
        System.out.print("Holder name: ");
        String name = scanner.nextLine();

        try {
            BankAccount acc = new BankAccount(name);
            service.saveBankAccount(acc);

            if (session.mainAccount == null) {
                session.mainAccount = acc;
                println("Main account defined!");
            }

            println("Account created! ID: " + acc.getAccountId());
        } catch (IllegalArgumentException e) {
            println("Error: " + e.getMessage());
        } catch (Exception e) {
            println("An unexpected error occurred: " + e.getMessage());
        }
        println("");
    }

    private static void deposit(BankAccountService service, Session session) {
        println("\nDeposit");

        BigDecimal amount = readBigDecimal("Amount: ");
        System.out.print("Description: ");
        String desc = scanner.nextLine();

        try {
            service.deposit(session.mainAccount.getAccountId(), amount, desc);
            println("Deposit successful!");
        } catch (IllegalArgumentException e) {
            println("Error: " + e.getMessage());
        } catch (Exception e) {
            println("An unexpected error occurred: " + e.getMessage());
        }
        println("");
    }

    private static void withdraw(BankAccountService service, Session session) {
        println("\nWithdraw");

        BigDecimal amount = readBigDecimal("Amount: ");
        System.out.print("Description: ");
        String desc = scanner.nextLine();

        try {
            service.withdraw(session.mainAccount.getAccountId(), amount, desc);
            println("Withdraw successful!");
        } catch (IllegalArgumentException e) {
            println("Error: " + e.getMessage());
        } catch (Exception e) {
            println("An unexpected error occurred: " + e.getMessage());
        }
        println("");
    }

    private static void transfer(BankAccountService service, Session session) {
        println("\nTransfer");

        BigDecimal amount = readBigDecimal("Amount: ");
        int destId = readInt("Destination account ID: ");
        System.out.print("Description: ");
        String desc = scanner.nextLine();

        try {
            service.transfer(session.mainAccount.getAccountId(), destId, amount, desc);
            println("Transfer successful!");
        } catch (IllegalArgumentException e) {
            println("Error: " + e.getMessage());
        } catch (Exception e) {
            println("An unexpected error occurred: " + e.getMessage());
        }
        println("");
    }

    private static void accountsMenu(BankAccountService service) {
        String option;

        System.out.print("(L)ist or (F)ind: ");
        option = scanner.nextLine().toUpperCase();

        if (option.startsWith("L")) {
            List<BankAccount> accounts = service.findAll();
            accounts.forEach(Application::printAccount);
        } else {
            int id = readInt("Account ID: ");
            try {
                printAccount(service.findById(id));
            } catch (IllegalArgumentException e) {
                println("Error: " + e.getMessage());
            } catch (Exception e) {
                println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    private static void transactionsMenu(BankAccountService bankService,
                                         TransactionService transactionService) {
        String option;

        System.out.print("(L)ist or (F)ind: ");
        option = scanner.nextLine().toUpperCase();

        if (option.startsWith("L")) {
            int id = readInt("Account ID: ");
            try {
                List<Transaction> list = bankService.findAllAccountTransactions(id);
                list.forEach(Application::printTransaction);
            } catch (IllegalArgumentException e) {
                println("Error: " + e.getMessage());
            } catch (Exception e) {
                println("An unexpected error occurred: " + e.getMessage());
            }
        } else {
            int id = readInt("Transaction ID: ");
            try {
                printTransaction(transactionService.findById(id));
            } catch (IllegalArgumentException e) {
                println("Error: " + e.getMessage());
            } catch (Exception e) {
                println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    // ================= PRINT =================

    private static void printAccount(BankAccount acc) {
        println("----------------------------");
        println("ID: " + acc.getAccountId());
        println("Holder: " + acc.getHolder());
        println("Balance: " + acc.getBalance());
        println("Status: " + acc.getAccountStatus());
        println("Created: " + acc.getCreatedAt());
        println("----------------------------");
        println("");
    }

    private static void printTransaction(Transaction t) {
        println("----------------------------");
        println("ID: " + t.getTransactionId());
        println("From: " + t.getSenderBankAccount().getAccountId());
        println("To: " + (t.getDestinationBankAccount() != null ? t.getDestinationBankAccount().getAccountId() : "N/A"));
        println("Type: " + t.getTransactionType());
        println("Amount: " + t.getAmount());
        println("Date: " + t.getDate());
        println("Description: " + (t.getDescription() == null || t.getDescription().isEmpty() ? "null" : t.getDescription()));
        println("----------------------------");
        println("");
    }
}