package net.guizbert.bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDto {

    @Schema(
            name = " Receiver of the email"
    )
    private String recipient;
    private String subject;
    private String messageBody;
    private String attachment;

}
