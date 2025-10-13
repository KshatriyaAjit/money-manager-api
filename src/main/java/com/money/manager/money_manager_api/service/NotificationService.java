package com.money.manager.money_manager_api.service;

import com.money.manager.money_manager_api.entity.ExpenseEntity;
import com.money.manager.money_manager_api.entity.ProfileEntity;
import com.money.manager.money_manager_api.repository.ExpenseRepository;
import com.money.manager.money_manager_api.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final ProfileRepository profileRepository;
    private final ExpenseRepository expenseRepository;
    private final EmailService emailService;

    /**
     * Runs every day at 10 PM (22:00) IST.
     * Sends a reminder to all users to log their transactions.
     */
  @Scheduled(cron = "0 0 22 * * ?", zone = "Asia/Kolkata")

    public void sendDailyReminder() {
        logger.info("Running daily reminder job...");
        List<ProfileEntity> allProfiles = profileRepository.findAll();

        for (ProfileEntity profile : allProfiles) {
            String subject = "Daily Reminder: Log Your Transactions!";
            String body = "Hi " + profile.getFullName() + ",<br><br>This is a friendly reminder to add your income and expenses for today in Money Manager. Keeping your records up-to-date helps you stay on top of your finances!<br><br>Best regards,<br>The Money Manager Team";
            emailService.sendEmail(profile.getEmail(), subject, body);
        }
        logger.info("Daily reminder job finished.");
    }

    /**
     * Runs every day at 11 PM (23:00) IST.
     * Sends a summary of the day's expenses to each user.
     */
    @Scheduled(cron = "0 0 23 * * ?", zone = "Asia/Kolkata")
    public void sendDailyExpenseSummary() {
        logger.info("Running daily expense summary job...");
        List<ProfileEntity> allProfiles = profileRepository.findAll();
        LocalDate today = LocalDate.now();

        for (ProfileEntity profile : allProfiles) {
            List<ExpenseEntity> todaysExpenses = expenseRepository.findByProfileIdAndDate(profile.getId(), today);

            // Only send an email if the user had expenses today
            if (!todaysExpenses.isEmpty()) {
                String subject = "Your Daily Expense Summary";
                String body = buildExpenseSummaryHtml(profile.getFullName(), todaysExpenses);
                emailService.sendEmail(profile.getEmail(), subject, body);
            }
        }
        logger.info("Daily expense summary job finished.");
    }

    private String buildExpenseSummaryHtml(String fullName, List<ExpenseEntity> expenses) {
        StringBuilder sb = new StringBuilder();
        sb.append("Hi ").append(fullName).append(",<br><br>Here is a summary of your expenses for today:<br><br>");
        sb.append("<table border='1' cellpadding='5' style='border-collapse: collapse;'>");
        sb.append("<tr><th>Name</th><th>Category</th><th>Amount</th></tr>");

        for (ExpenseEntity expense : expenses) {
            sb.append("<tr>");
            sb.append("<td>").append(expense.getName()).append("</td>");
            sb.append("<td>").append(expense.getCategory().getName()).append("</td>");
            sb.append("<td>₹").append(expense.getAmount().toString()).append("</td>");
            sb.append("</tr>");
        }

        sb.append("</table><br>Best regards,<br>The Money Manager Team");
        return sb.toString();
    }
}
