package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BankAccount {
    private static int nextId = 1;

    private final int accountId;
    private final AccountType accountType;
    private final String holder;
    private BigDecimal balance;

    private final LocalDateTime createdAt;

    private final List<Transaction> transactions = new ArrayList<>();

    public BankAccount(AccountType accountType, String holder, BigDecimal balance) {
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }

        if (holder == null || holder.isBlank()) {
            throw new IllegalArgumentException("holder cannot be null or blank");
        }

        Objects.requireNonNull(accountType, "Account type cannot be null");

        this.accountId = nextId++;
        this.accountType = accountType;
        this.holder = holder;
        this.balance = balance;
        this.createdAt = LocalDateTime.now();
    }

    public int getAccountId() {
        return accountId;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public String getHolder() {
        return holder;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void deposit(BigDecimal amount) {
        Objects.requireNonNull(amount, "Amount cannot be null");

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw  new IllegalArgumentException("Amount must be greater than zero");
        }

        this.balance = this.balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        Objects.requireNonNull(amount, "Amount cannot be null");

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        if (amount.compareTo(balance) > 0) {
            throw new IllegalArgumentException("Amount cannot be greater than balance");
        }

        this.balance = this.balance.subtract(amount);
    }

    public void transfer(BankAccount destination, BigDecimal amount) {
        Objects.requireNonNull(amount, "Amount cannot be null");
        Objects.requireNonNull(destination, "Destination cannot be null");

        if (destination == this) {
            throw new IllegalArgumentException("Destination cannot be the same object");
        }

        withdraw(amount);
        destination.deposit(amount);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount that = (BankAccount) o;
        return accountId == that.accountId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(accountId);
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "accountId=" + accountId +
                ", accountType=" + accountType +
                ", holder='" + holder + '\'' +
                ", balance=" + balance +
                ", createdAt=" + createdAt +
                '}';
    }
}
