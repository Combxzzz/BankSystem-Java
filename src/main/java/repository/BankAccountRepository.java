package repository;

import model.BankAccount;

import java.util.*;

                                                                      // Autoboxing: int -> Integer
public class BankAccountRepository implements Repository<BankAccount, Integer> {
    private final Map<Integer, BankAccount> bankAccountMap = new HashMap<>();

    @Override
    public BankAccount save(BankAccount entity) {
        bankAccountMap.put(entity.getAccountId(), entity);
        return entity;
    }

    @Override
    public void deleteById(Integer id) {
        bankAccountMap.remove(id);
    }

    @Override
    public List<BankAccount> findAll() {
        return new ArrayList<>(bankAccountMap.values());
    }

    @Override
    public Optional<BankAccount> findById(Integer id) {
        return Optional.ofNullable(bankAccountMap.get(id));
    }
}
