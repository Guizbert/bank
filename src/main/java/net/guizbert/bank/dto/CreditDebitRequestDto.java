package net.guizbert.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditDebitRequestDto {

    private String accountNumber;
    private BigDecimal amount;

}
