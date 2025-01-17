package net.guizbert.bank.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.guizbert.bank.entity.Transaction;
import net.guizbert.bank.entity.User;
import net.guizbert.bank.repository.TransactionRepository;
import net.guizbert.bank.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class BankStatement {

    // Constant for file path
    private static final String PDF_FILE_PATH = "C:\\Users\\Public\\MyStatement.pdf";

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate) throws DocumentException, FileNotFoundException {
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

        User user = userRepository.findByAccountNumber(accountNumber);
        List<Transaction> transactions = transactionRepository.findAll().stream()
                .filter(t -> t.getAccountNumber().equals(accountNumber))
                .filter(t -> !t.getCreatedAt().isBefore(start.atStartOfDay()))  // Include start date
                .filter(t -> !t.getCreatedAt().isAfter(end.atTime(23, 59, 59))) // Include end date
                .toList();

        createPdf(transactions, user, startDate, endDate);
        return transactions;
    }

    public void createPdf(List<Transaction> transactions, User user, String startDate, String endDate)
            throws FileNotFoundException, DocumentException {

        // Create document with A4 page format
        Rectangle pageFormat = new Rectangle(PageSize.A4);
        Document document = new Document(pageFormat);

        OutputStream outputStream = new FileOutputStream(PDF_FILE_PATH);
        PdfWriter.getInstance(document, outputStream);
        document.open();

        // Header Table (Bank Info)
        PdfPTable infoTable = createHeaderTable(user);

        // Statement Info Table (User and Date Info)
        PdfPTable statementInfoTable = createStatementInfoTable(user, startDate, endDate);

        // Transaction Table (Transaction Details)
        PdfPTable transactionTable = createTransactionTable(transactions);

        // Add tables to the document
        document.add(infoTable);
        document.add(statementInfoTable);
        document.add(transactionTable);

        document.close();
        log.info("PDF document generated successfully at {}", PDF_FILE_PATH);
    }

    private PdfPTable createHeaderTable(User user) {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);

        // Bank Name
        PdfPCell bankNameCell = new PdfPCell(new Phrase("Banking Project", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
        bankNameCell.setBorder(0);
        bankNameCell.setBackgroundColor(BaseColor.ORANGE);
        bankNameCell.setPadding(10f);
        bankNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);

        // Bank Address
        PdfPCell bankAddressCell = new PdfPCell(new Phrase("77, Random Address, Banking Country", FontFactory.getFont(FontFactory.HELVETICA, 12)));
        bankAddressCell.setBorder(0);
        bankAddressCell.setPadding(5f);
        bankAddressCell.setHorizontalAlignment(Element.ALIGN_CENTER);

        table.addCell(bankNameCell);
        table.addCell(bankAddressCell);

        return table;
    }

    private PdfPTable createStatementInfoTable(User user, String startDate, String endDate) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        // Statement
        PdfPCell statement = new PdfPCell(new Phrase("STATEMENT", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
        statement.setBorder(0);

        // User Info (Name)
        String userName = user.getLastName().toUpperCase() + " " + user.getFirstName();
        PdfPCell userInfoCell = new PdfPCell(new Phrase(userName, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
        userInfoCell.setBorder(0);
        // Dates (Start and End)
        PdfPCell startDateCell = new PdfPCell(new Phrase("From: " + startDate, FontFactory.getFont(FontFactory.HELVETICA, 12)));
        startDateCell.setBorder(0);
        PdfPCell endDateCell = new PdfPCell(new Phrase("To: " + endDate, FontFactory.getFont(FontFactory.HELVETICA, 12)));
        endDateCell.setBorder(0);

        // Address
        PdfPCell addressCell = new PdfPCell(new Phrase("Address: " + user.getAdress(), FontFactory.getFont(FontFactory.HELVETICA, 12)));
        addressCell.setBorder(0);

        // Empty space :
        PdfPCell empty = new PdfPCell();
        empty.setBorder(0);
        // Add cells to the table
        table.addCell(new PdfPCell(new Phrase("STATEMENT", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12))));
        table.addCell(empty);
        table.addCell(startDateCell);
        table.addCell(userInfoCell);
        table.addCell(endDateCell);
        table.addCell(addressCell);

        return table;
    }

    private PdfPTable createTransactionTable(List<Transaction> transactions) {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);

        // Table Headers
        PdfPCell dateHeader = new PdfPCell(new Phrase("DATE", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
        dateHeader.setBackgroundColor(BaseColor.ORANGE);
        dateHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
        dateHeader.setPadding(8f);

        PdfPCell typeHeader = new PdfPCell(new Phrase("TYPE", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
        typeHeader.setBackgroundColor(BaseColor.PINK);
        typeHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
        typeHeader.setPadding(8f);

        PdfPCell amountHeader = new PdfPCell(new Phrase("AMOUNT", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
        amountHeader.setBackgroundColor(BaseColor.ORANGE);
        amountHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
        amountHeader.setPadding(8f);

        PdfPCell statusHeader = new PdfPCell(new Phrase("STATUS", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
        statusHeader.setBackgroundColor(BaseColor.PINK);
        statusHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
        statusHeader.setPadding(8f);

        // Add headers to the table
        table.addCell(dateHeader);
        table.addCell(typeHeader);
        table.addCell(amountHeader);
        table.addCell(statusHeader);

        // Add transactions to the table
        transactions.forEach(transaction -> {
            PdfPCell dateCell = new PdfPCell(new Phrase(transaction.getCreatedAt().format(DateTimeFormatter.ISO_DATE), FontFactory.getFont(FontFactory.HELVETICA, 12)));
            PdfPCell typeCell = new PdfPCell(new Phrase(transaction.getTransactionType(), FontFactory.getFont(FontFactory.HELVETICA, 12)));
            PdfPCell amountCell = new PdfPCell(new Phrase(String.format("$%.2f", transaction.getAmount()), FontFactory.getFont(FontFactory.HELVETICA, 12)));
            PdfPCell statusCell = new PdfPCell(new Phrase(transaction.getStatus(), FontFactory.getFont(FontFactory.HELVETICA, 12)));

            // Add cells to the table
            table.addCell(dateCell);
            table.addCell(typeCell);
            table.addCell(amountCell);
            table.addCell(statusCell);
        });

        return table;
    }
}
