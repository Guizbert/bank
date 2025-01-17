package net.guizbert.bank.controller;


import com.itextpdf.text.DocumentException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.guizbert.bank.entity.Transaction;
import net.guizbert.bank.service.impl.BankStatement;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transaction APIs")
@AllArgsConstructor
public class TransactionController
{

    private BankStatement bankStatement;

    @GetMapping()
    public List<Transaction> generateStatement(@RequestParam String accountNumber,
                                               @RequestParam String startDate,
                                               @RequestParam String endDate) throws DocumentException, FileNotFoundException {
        return  bankStatement.generateStatement(accountNumber,startDate,endDate);

    }

}
