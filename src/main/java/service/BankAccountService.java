package service;

import model.enums.AccountStatus;
import model.BankAccount;
import model.Transaction;
import repository.BankAccountRepository;
import repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final TransactionRepository transactionRepository;

    public BankAccountService(BankAccountRepository bankAccountRepository, TransactionRepository transactionRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.transactionRepository = transactionRepository;
    }

    private BankAccount getAccountOrThrow(int id) {
        return bankAccountRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(
                "Account ID " + id + " does not exist"
        ));
    }

    public BankAccount saveBankAccount(BankAccount bankAccount) {
        return bankAccountRepository.save(bankAccount);
    }

    public void deleteById(int accountId) {
        getAccountOrThrow(accountId);
        bankAccountRepository.deleteById(accountId);
    }

    public BankAccount findById(int id) {
        return getAccountOrThrow(id);
    }

    public List<BankAccount> findAll() {
        return bankAccountRepository.findAll();
    }

    public List<Transaction> findAllAccountTransactions(int accountId) {
        BankAccount account = getAccountOrThrow(accountId);

        return new ArrayList<>(account.getTransactions());
    }

    public void deposit(int accountId, BigDecimal amount, String description) {
        BankAccount account = getAccountOrThrow(accountId);

        Transaction record = account.deposit(amount, description);

        transactionRepository.save(record);
    }

    public void withdraw(int accountId, BigDecimal amount, String description) {
        BankAccount account = getAccountOrThrow(accountId);

        Transaction record = account.withdraw(amount, description);

        transactionRepository.save(record);
    }

    public void transfer(int senderAccountId, int destinationAccountId, BigDecimal amount, String description) {
        BankAccount sender = getAccountOrThrow(senderAccountId);
        BankAccount destination = getAccountOrThrow(destinationAccountId);

        Transaction record = sender.transfer(destination, amount, description);

        transactionRepository.save(record);
    }

    public void switchAccountStatus(int accountId, AccountStatus newAccountStatus) {
        BankAccount account = getAccountOrThrow(accountId);

        account.setAccountStatus(newAccountStatus);
    }
}
