package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

// TODO: Create the TransactionType EnumClass

public class Transaction {
    private static int nextId = 1;

    private final int transactionId;
    private final BankAccount bankAccount;
    private final BankAccount destinationBankAccount;

    private final TransactionType transactionType;
    private final BigDecimal amount;
    private final LocalDateTime date;
    private final String description;

    public Transaction(
            BankAccount bankAccount,
            BankAccount destinationBankAccount,
            TransactionType transactionType,
            BigDecimal amount,
            String description) {

        Objects.requireNonNull(bankAccount, "Bank account cannot be null");
        Objects.requireNonNull(transactionType, "Transaction Type cannot be null");
        Objects.requireNonNull(amount, "Amount cannot be null");

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        // Transfer case
        if (transactionType == TransactionType.TRANSFER && destinationBankAccount == null) {
            throw new IllegalArgumentException("Transfer must have destination account");
        }

        if (transactionType != TransactionType.TRANSFER && destinationBankAccount != null) {
            throw new IllegalArgumentException("Only transfers can have destination account");
        }

        this.transactionId = nextId++; // ID
        this.bankAccount =  bankAccount; // Not null
        this.destinationBankAccount = destinationBankAccount; // Can be null except for transfer
        this.transactionType = transactionType; // Not null
        this.amount = amount; // Not null || Zero or negative
        this.date = LocalDateTime.now(); // Date
        this.description = description; // Can be blank or null
    }

    public int getTransactionId() {
        return transactionId;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public BankAccount getDestinationBankAccount() {
        return destinationBankAccount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return transactionId == that.transactionId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(transactionId);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", bankAccountId=" + bankAccount.getAccountId() +
                ", destinationBankAccountId=" +
                (destinationBankAccount != null ? destinationBankAccount.getAccountId() : "null") +
                ", transactionType=" + transactionType +
                ", amount=" + amount +
                ", date=" + date +
                ", description='" + description + '\'' +
                '}';
    }
}
