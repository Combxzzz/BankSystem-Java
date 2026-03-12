package repository;

import model.BankAccount;

                                                                           // Autoboxing: int -> Integer
public class BankAccountRepository extends AbstractRepository<BankAccount, Integer>{
    @Override
    protected Integer getId(BankAccount entity) {
        return entity.getAccountId();
    }
}
