package repository;

import model.Transaction;

public class TransactionRepository extends AbstractRepository<Transaction, Integer>{
    @Override
    protected Integer getId(Transaction entity) {
        return entity.getTransactionId();
    }
}
