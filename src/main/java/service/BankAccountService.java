package service;

import model.AccountStatus;
import model.BankAccount;
import model.Transaction;
import repository.BankAccountRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BankAccountService {
    private final BankAccountRepository repository;

    public BankAccountService(BankAccountRepository repository) {
        this.repository = repository;
    }

    private BankAccount getAccountOrThrow(int id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException(
                "Account ID " + id + " does not exist"
        ));
    }

    public BankAccount saveBankAccount(BankAccount bankAccount) {
        return repository.save(bankAccount);
    }

    public void deleteById(int accountId) {
        getAccountOrThrow(accountId);
        repository.deleteById(accountId);
    }

    public BankAccount findById(int id) {
        return getAccountOrThrow(id);
    }

    public List<BankAccount> findAll() {
        return repository.findAll();
    }

    public List<Transaction> findAllAccountTransactions(int accountId) {
        BankAccount account = getAccountOrThrow(accountId);

        return new ArrayList<>(account.getTransactions());
    }

    public void deposit(int accountId, BigDecimal amount, String description) {
        BankAccount account = getAccountOrThrow(accountId);

        account.deposit(amount, description);
    }

    public void withdraw(int accountId, BigDecimal amount, String description) {
        BankAccount account = getAccountOrThrow(accountId);

        account.withdraw(amount, description);
    }

    public void transfer(int senderAccountId, int destinationAccountId, BigDecimal amount, String description) {
        BankAccount sender = getAccountOrThrow(senderAccountId);
        BankAccount destination = getAccountOrThrow(destinationAccountId);

        sender.transfer(destination, amount, description);
    }

    public void switchAccountStatus(int accountId, AccountStatus newAccountStatus) {
        BankAccount account = getAccountOrThrow(accountId);

        account.setAccountStatus(newAccountStatus);
    }
}
