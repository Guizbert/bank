package net.guizbert.bank.dto;

import lombok.*;

import java.math.BigDecimal;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String firstName;
    private String lastName;
    private String gender;
    private String adress;
    private String origin;

    private String email;
    private String phoneNumber;
    private BigDecimal accountBalance;

}
