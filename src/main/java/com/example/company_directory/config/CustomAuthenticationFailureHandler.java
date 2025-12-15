package com.example.company_directory.config;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.example.company_directory.repository.UserRepository;
import com.example.company_directory.service.LoginAttemptService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;

    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int LOCK_DURATION_MINUTES = 15;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

        String username = request.getParameter("username");
        String ipAddress = getClientIP(request);

        // IP失敗記録
        loginAttemptService.recordFailure(ipAddress);

        // ユーザーが存在する場合、失敗カウントを加算
        if (username != null && !username.isEmpty()) {
            userRepository.findByUserId(username).ifPresent(user -> {
                int newCount = user.getFailedLoginCount() + 1;
                user.setFailedLoginCount(newCount);

                // 5回連続失敗でロック
                if (newCount >= MAX_FAILED_ATTEMPTS) {
                    user.setLockedUntil(LocalDateTime.now().plusMinutes(LOCK_DURATION_MINUTES));
                }

                userRepository.save(user);
            });
        }

        // IP遅延適用
        int delaySeconds = loginAttemptService.getDelaySeconds(ipAddress);
        if (delaySeconds > 0) {
            try {
                Thread.sleep(delaySeconds * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // ロック例外の場合
        if (exception instanceof LockedException) {
            response.sendRedirect("/login?locked=true");
        } else {
            response.sendRedirect("/login?error=true");
        }
    }

    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
