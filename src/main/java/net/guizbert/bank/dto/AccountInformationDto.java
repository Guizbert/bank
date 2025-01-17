package net.guizbert.bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(
            name = "Name of the user",
            description = ""
    )
    private String accountName;
    @Schema(
            name = "Amount available",
            description = ""
    )
    private BigDecimal accountBalance;
    @Schema(
            name = "Account number",
            description = ""
    )
    private String accountNumber;
}
