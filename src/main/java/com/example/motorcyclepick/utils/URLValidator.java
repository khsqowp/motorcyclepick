// URLValidator.java
package com.example.motorcyclepick.utils;

import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class URLValidator {
    private static final List<String> ALLOWED_HOSTS = Arrays.asList(
            "api.example.com",
            "cdn.example.com"
    );

    private static final List<String> BLOCKED_IP_RANGES = Arrays.asList(
            "127.", "192.168.", "10.", "172.16."
    );

    private static final Pattern IP_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"
    );

    public boolean isValidUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            String host = url.getHost();

            // IP 주소 체크
            if (IP_PATTERN.matcher(host).matches()) {
                return !isBlockedIpRange(host);
            }

            // 허용된 호스트 체크
            return ALLOWED_HOSTS.contains(host.toLowerCase());

        } catch (MalformedURLException e) {
            return false;
        }
    }

    private boolean isBlockedIpRange(String ip) {
        return BLOCKED_IP_RANGES.stream()
                .anyMatch(ip::startsWith);
    }
}