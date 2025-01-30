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

// 데이터베이스 백업 설정을 담당하는 설정 클래스
@Configuration
// 스케줄링 기능 활성화
@EnableScheduling
// Lombok을 사용한 로깅 설정
@Slf4j
public class DatabaseBackupConfig {

    // 데이터베이스 접속 정보 주입
    @Value("${spring.datasource.username}")
    private String dbUsername = "root";

    @Value("${spring.datasource.password}")
    private String dbPassword = "gkstndi1!";

    @Value("${spring.datasource.url}")
    private String dbUrl = "jdbc:mysql://localhost:3306/MotorcycleDB?serverTimezone=Asia/Seoul&characterEncoding=UTF-8";

    // 백업 디렉토리 설정 (기본값: /app/backups/mysql)
    @Value("${backup.directory:/app/backups/mysql}")
    private String backupDir;

    // 매일 새벽 2시에 백업 실행 (cron 표현식으로 설정 가능)
    @Scheduled(cron = "${backup.schedule:0 0 2 * * *}")
    public void performDailyBackup() {
        int retryCount = 0;
        int maxRetries = 3;  // 최대 재시도 횟수

        while (retryCount < maxRetries) {
            try {
                // 데이터베이스 이름 추출
                String dbName = extractDatabaseName(dbUrl);
                // 타임스탬프 생성 (yyyyMMdd 형식)
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                // 백업 파일명 생성
                String backupFileName = String.format("%s/%s_%s.sql", backupDir, dbName, timestamp);

                // 백업 디렉토리 생성
                createBackupDirectory();
                // 30일 이상된 백업 파일 정리
                cleanOldBackups();

                // mysqldump 명령어 설정
                ProcessBuilder processBuilder = new ProcessBuilder(
                        "mysqldump",
                        "-u" + dbUsername,
                        "--databases",
                        dbName,
                        "--result-file=" + backupFileName,
                        "--single-transaction",  // 트랜잭션 정합성 보장
                        "--quick",              // 대용량 데이터 처리 최적화
                        "--lock-tables=false"    // 테이블 잠금 방지
                );

                // 환경변수에 비밀번호 설정
                Map<String, String> environment = processBuilder.environment();
                environment.put("MYSQL_PWD", dbPassword);

                // 에러 스트림을 표준 출력으로 리다이렉트
                processBuilder.redirectErrorStream(true);
                Process process = processBuilder.start();

                // 프로세스 완료 대기 및 결과 확인
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
                // 재시도 전 대기
                try {
                    Thread.sleep(1000 * retryCount);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    // JDBC URL에서 데이터베이스 이름을 추출하는 유틸리티 메서드
    private String extractDatabaseName(String jdbcUrl) {
        try {
            String url = jdbcUrl.replace("mysql:", "localhost:");
            return url.substring(url.lastIndexOf("/") + 1).split("\\?")[0];
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid database URL", e);
        }
    }

    // 백업 디렉토리 생성 메서드
    private void createBackupDirectory() {
        File directory = new File(backupDir);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new RuntimeException("Failed to create backup directory");
            }
        }
    }

    // 30일 이상 된 백업 파일 정리 메서드
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

    // 백업 파일 압축 메서드
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