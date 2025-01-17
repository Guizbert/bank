package net.guizbert.bank.service.impl;

import net.guizbert.bank.dto.TransactionDto;
import net.guizbert.bank.entity.Transaction;
import net.guizbert.bank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(TransactionDto transaction) {
        Transaction t = transactionBuilder(transaction, "SUCCESS");

        transactionRepository.save(t);
        System.out.println("Transaction saved");
    }


    Transaction transactionBuilder(TransactionDto t, String status)
    {
        return Transaction.builder()
                .transactionType(t.getTransactionType())
                .amount(t.getAmount())
                .accountNumber(t.getAccountNumber())
                .status(t.getStatus())
                .build();

    }
}
