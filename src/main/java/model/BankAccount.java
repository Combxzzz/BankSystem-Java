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
    private AccountStatus accountStatus;
    private final String holder;
    private BigDecimal balance;
    private final LocalDateTime createdAt;

    private final List<Transaction> transactions = new ArrayList<>();

    public BankAccount(String holder) {

        Objects.requireNonNull(holder, "Holder cannot be null");

        if (holder.isBlank()) {
            throw new IllegalArgumentException("holder cannot be blank");
        }

        this.accountId = nextId++;
        this.accountStatus = AccountStatus.ACTIVE;
        this.holder = holder;
        this.balance = BigDecimal.ZERO;
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

    public void deposit(BigDecimal amount, String description) {
        if (!this.accountStatus.canTransact()) {
            throw new IllegalArgumentException("The account must be ACTIVE to make transactions");
        }

        Objects.requireNonNull(amount, "Amount cannot be null");

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        addBalance(amount);

        Transaction record = Transaction.deposit(this, amount, description);
        recordTransaction(record);
    }

    public void withdraw(BigDecimal amount, String description) {
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

        subtractBalance(amount);

        Transaction record = Transaction.withdraw(this, amount, description);
        recordTransaction(record);
    }

    public void transfer(BankAccount destination, BigDecimal amount, String description) {
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

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        if (amount.compareTo(balance) > 0) {
            throw new IllegalArgumentException("Amount cannot be greater than balance");
        }

        subtractBalance(amount);
        destination.addBalance(amount);

        Transaction record = Transaction.transfer(this, destination, amount, description);
        recordTransaction(record);
        destination.recordTransaction(record);
    }

    public void setAccountStatus(AccountStatus newStatus) {
        Objects.requireNonNull(newStatus, "Account status cannot be null");

        if (!this.accountStatus.canChangeTo(newStatus)) {
            throw new IllegalArgumentException(String.format("Account Status cannot be changed from %s to %s",
                    this.accountStatus, newStatus));
        }

        this.accountStatus = newStatus;
    }

    // Internal methods
    private void addBalance(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    private void subtractBalance(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }

    private void recordTransaction(Transaction transaction) {
        this.transactions.add(transaction);
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
