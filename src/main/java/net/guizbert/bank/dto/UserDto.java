package net.guizbert.bank.dto;

import lombok.*;
import net.guizbert.bank.entity.Role;

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
    private String password;
    private String email;
    private String phoneNumber;
    private Role role;
    private BigDecimal accountBalance;

}
