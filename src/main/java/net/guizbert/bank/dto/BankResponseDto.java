package net.guizbert.bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankResponseDto {

    @Schema(
            name = "Response code"
    )
    private String responseCode;
    @Schema(
            name = "Message"
    )
    private String responseMessage;
    @Schema(
            name = "Account informations"
    )
    private AccountInformationDto accInfo;


}
