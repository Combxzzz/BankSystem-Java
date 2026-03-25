package app;

import model.BankAccount;
import model.Transaction;
import model.enums.AccountStatus;
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
                    7 - Switch Account
                    8 - Switch Account Status
                    0 - Exit
                    
                    Option:""");

            switch (option) {
                case 1 -> createAccount(bankService, session);
                case 2 -> deposit(bankService, session);
                case 3 -> withdraw(bankService, session);
                case 4 -> transfer(bankService, session);
                case 5 -> accountsMenu(bankService);
                case 6 -> transactionsMenu(bankService, transactionService);
                case 7 -> switchAccountMenu(bankService, scanner, session);
                case 8 -> switchAccountStatusMenu(bankService, session);
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
        // Working via terminal only
    }

    // ================= FEATURES =================

    private static void createAccount(BankAccountService service, Session session) {
        while (true) {
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
                break;
            } catch (IllegalArgumentException e) {
                println("Error: " + e.getMessage());
            } catch (Exception e) {
                println("An unexpected error occurred: " + e.getMessage());
            }
            println("");
        }
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

    private static void switchAccountStatusMenu(BankAccountService bankAccountService,
                                            Session session) {
        println("\n=== Switch Account Status ===");
        println("Warning: CLOSED accounts cannot be recovered or used for transfers.");

        String option;

        while (true) {
            System.out.print("Do you really want to change your account status? (Y/N): ");
            option = scanner.nextLine().trim().toUpperCase();

            if (option.equals("Y") || option.equals("N")) {
                break;
            }

            println("Invalid option. Please type Y or N.");
        }

        if (option.equals("N")) {
            println("Account status change cancelled!");
            return;
        }

        AccountStatus newAccountStatus = null;

        while (newAccountStatus == null) {
            println("\nSelect a new account status:");

            int status = readInt("""
                
                1 - ACTIVE
                2 - BLOCKED
                3 - CLOSED
                
                Option:""");

            switch (status) {
                case 1 -> newAccountStatus = AccountStatus.ACTIVE;
                case 2 -> newAccountStatus = AccountStatus.BLOCKED;
                case 3 -> newAccountStatus = AccountStatus.CLOSED;
                default -> println("Invalid option. Try again.");
            }
        }

        BankAccount account = bankAccountService.findById(session.mainAccount.getAccountId());

        try {
            account.setAccountStatus(newAccountStatus);
            println("Account status changed successfully!");
        } catch (IllegalArgumentException e) {
            println("Error: " + e.getMessage());
        } catch (Exception e) {
            println("Unexpected error: " + e.getMessage());
        }
    }

    private static void switchAccountMenu(BankAccountService bankAccountService, Scanner scanner, Session session) {
        println("\n=== Switch Account ===");

        int id = readInt("Enter the account ID for switch: ");

        try {
            BankAccount targetAccount = bankAccountService.findById(id);

            println("Account found!");
            printAccount(targetAccount);

            String option;
            while (true) {
                System.out.print("Do you really want to switch to this account? (Y/N): ");
                option = scanner.nextLine().trim().toUpperCase();

                if (option.equals("Y") || option.equals("N")) {
                    break;
                }

                println("Invalid option, please type Y or N");
            }

            if (option.equals("N")) {
                println("Account switch cancelled!");
                return;
            }
            println("");

            session.mainAccount = targetAccount;
            println("Switched to account ID " + targetAccount.getAccountId() + " successfully!");

        } catch (IllegalArgumentException e) {
            println("Error" + e.getMessage());
        } catch (Exception e) {
            println("Unexpected error: " + e.getMessage());
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