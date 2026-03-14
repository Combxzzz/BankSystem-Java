package service;

import model.BankAccount;
import model.Transaction;
import repository.BankAccountRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BankAccountService {
    private final BankAccountRepository bankAccountRepository;

    public BankAccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
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

    public Optional<BankAccount> findById(int id) {
        return bankAccountRepository.findById(id);
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
}
