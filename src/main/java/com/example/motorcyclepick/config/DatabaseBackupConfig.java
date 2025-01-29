package com.example.motorcyclepick.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Configuration
@EnableScheduling
@Slf4j
public class DatabaseBackupConfig {

    @Value("${spring.datasource.username}")
    private String dbUsername = "root";

    @Value("${spring.datasource.password}")
    private String dbPassword = "gkstndi1!";

    @Value("${spring.datasource.url}")
    private String dbUrl = "jdbc:mysql://localhost:3306/MotorcycleDB?serverTimezone=Asia/Seoul&characterEncoding=UTF-8";

    @Value("${backup.directory:/app/backups/mysql}")
    private String backupDir;

    @Scheduled(cron = "${backup.schedule:0 0 2 * * *}")
    public void performDailyBackup() {
        int retryCount = 0;
        int maxRetries = 3;

        while (retryCount < maxRetries) {
            try {
                String dbName = extractDatabaseName(dbUrl);
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                String backupFileName = String.format("%s/%s_%s.sql", backupDir, dbName, timestamp);

                createBackupDirectory();
                cleanOldBackups();

                ProcessBuilder processBuilder = new ProcessBuilder(
                        "mysqldump",
                        "-u" + dbUsername,
                        "--databases",
                        dbName,
                        "--result-file=" + backupFileName,
                        "--single-transaction",
                        "--quick",
                        "--lock-tables=false"
                );

                Map<String, String> environment = processBuilder.environment();
                environment.put("MYSQL_PWD", dbPassword);

                processBuilder.redirectErrorStream(true);
                Process process = processBuilder.start();

                int exitCode = process.waitFor();
                if (exitCode == 0) {
                    log.info("Database backup completed successfully: {}", backupFileName);
                    compressBackupFile(backupFileName);
                    return;
                } else {
                    throw new RuntimeException("Database backup failed with exit code: " + exitCode);
                }
            } catch (Exception e) {
                retryCount++;
                log.error("Backup attempt {} failed: {}", retryCount, e.getMessage());
                if (retryCount >= maxRetries) {
                    throw new RuntimeException("Backup failed after " + maxRetries + " attempts", e);
                }
                try {
                    Thread.sleep(1000 * retryCount);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    private String extractDatabaseName(String jdbcUrl) {
        try {
            String url = jdbcUrl.replace("mysql:", "localhost:");
            return url.substring(url.lastIndexOf("/") + 1).split("\\?")[0];
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid database URL", e);
        }
    }

    private void createBackupDirectory() {
        File directory = new File(backupDir);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new RuntimeException("Failed to create backup directory");
            }
        }
    }

    private void cleanOldBackups() {
        File directory = new File(backupDir);
        if (directory.exists()) {
            LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".sql"));

            if (files != null) {
                for (File file : files) {
                    try {
                        String dateStr = file.getName().split("_")[1].replace(".sql", "");
                        LocalDateTime fileDate = LocalDateTime.parse(dateStr,
                                DateTimeFormatter.ofPattern("yyyyMMdd"));

                        if (fileDate.isBefore(thirtyDaysAgo)) {
                            if (!file.delete()) {
                                log.warn("Failed to delete old backup file: {}", file.getName());
                            }
                        }
                    } catch (Exception e) {
                        log.warn("Error processing backup file: {}", file.getName(), e);
                    }
                }
            }
        }
    }

    private void compressBackupFile(String backupFileName) {
        try {
            Process process = Runtime.getRuntime().exec(
                    "gzip " + backupFileName
            );
            process.waitFor();
            log.info("Backup file compressed: {}.gz", backupFileName);
        } catch (Exception e) {
            log.error("Failed to compress backup file", e);
        }
    }
}