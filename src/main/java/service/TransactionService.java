package service;

import model.Transaction;
import repository.TransactionRepository;

public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository repository) {
        this.transactionRepository = repository;
    }

    private Transaction getTransactionOrThrow(int id) {
        return transactionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(
                "Transaction ID" + id + " does not exist"
        ));
    }

    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
    
    public Transaction findById(int transactionId) {
        return getTransactionOrThrow(transactionId);
    }
}
