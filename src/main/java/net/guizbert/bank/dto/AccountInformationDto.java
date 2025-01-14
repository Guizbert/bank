package net.guizbert.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountInformationDto {
    private String accountName;
    private BigDecimal accountBalance;
    private String accountNumber;
}
