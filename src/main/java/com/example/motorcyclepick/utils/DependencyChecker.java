// DependencyChecker.java
package com.example.motorcyclepick.utils;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class DependencyChecker {
    private static final String BUILD_GRADLE_PATH = "build.gradle";
    private static final Map<String, String> KNOWN_VULNERABILITIES = new HashMap<>();

    static {
        KNOWN_VULNERABILITIES.put("spring-boot:2.7.5", "CVE-2023-XXXX");
        // 알려진 취약점 목록 추가
    }

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    public void checkDependencies() {
        try (BufferedReader reader = new BufferedReader(new FileReader(BUILD_GRADLE_PATH))) {
            String line;
            Pattern versionPattern = Pattern.compile("'([^:]+):([^:]+):([^']+)'");

            while ((line = reader.readLine()) != null) {
                Matcher matcher = versionPattern.matcher(line);
                if (matcher.find()) {
                    String groupId = matcher.group(1);
                    String artifactId = matcher.group(2);
                    String version = matcher.group(3);

                    String dependency = artifactId + ":" + version;
                    if (KNOWN_VULNERABILITIES.containsKey(dependency)) {
                        log.warn("Vulnerable dependency found: {} (CVE: {})",
                                dependency, KNOWN_VULNERABILITIES.get(dependency));
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to check dependencies", e);
        }
    }
}