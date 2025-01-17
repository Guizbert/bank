package net.guizbert.bank.service.impl;
import net.guizbert.bank.dto.EmailDto;
public interface EmailService {

    void sendEmailAlert(EmailDto emailDetails);

    void sendEmailWithAttachments(EmailDto emailDetails);
}
