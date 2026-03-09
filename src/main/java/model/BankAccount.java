package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

// TODO: Create class Transaction.java

public class BankAccount {
    private static int nextId = 1;

    private final int accountId;
    private AccountStatus accountStatus;
    private final String holder;
    private BigDecimal balance;

    private final LocalDateTime createdAt;

    private final List<Transaction> transactions = new ArrayList<>();

    public BankAccount(String holder, BigDecimal balance) {
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }

        if (holder == null || holder.isBlank()) {
            throw new IllegalArgumentException("holder cannot be null or blank");
        }

        this.accountId = nextId++;
        this.accountStatus = AccountStatus.ACTIVE;
        this.holder = holder;
        this.balance = balance;
        this.createdAt = LocalDateTime.now();
    }

    public int getAccountId() {
        return accountId;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
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
        if (!this.accountStatus.canTransact()) {
            throw new IllegalArgumentException("The account must be ACTIVE to make transactions");
        }

        Objects.requireNonNull(amount, "Amount cannot be null");

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw  new IllegalArgumentException("Amount must be greater than zero");
        }

        this.balance = this.balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        if (!this.accountStatus.canTransact()) {
            throw new IllegalArgumentException("The account must be ACTIVE to make transactions");
        }

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
        if (!this.accountStatus.canTransact()) {
            throw new IllegalArgumentException("The account must be ACTIVE to make transactions");
        }

        Objects.requireNonNull(amount, "Amount cannot be null");
        Objects.requireNonNull(destination, "Destination cannot be null");

        if (destination == this) {
            throw new IllegalArgumentException("Destination cannot be the same object");
        }

        if (!destination.accountStatus.canTransact()) {
            throw new IllegalArgumentException("Destination cannot receive transactions");
        }

        withdraw(amount);
        destination.deposit(amount);
    }

    public void setAccountStatus(AccountStatus newStatus) {
        Objects.requireNonNull(newStatus, "Account status cannot be null");

        if (!this.accountStatus.canChangeTo(newStatus)) {
            throw new IllegalArgumentException(String.format("Account Status cannot be changed from %s to %s",
                    this.accountStatus, newStatus));
        }

        this.accountStatus = newStatus;
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
                ", accountStatus=" + accountStatus +
                ", holder='" + holder + '\'' +
                ", balance=" + balance +
                ", createdAt=" + createdAt +
                '}';
    }
}
