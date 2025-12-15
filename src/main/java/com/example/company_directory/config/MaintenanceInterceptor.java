package com.example.company_directory.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class MaintenanceInterceptor implements HandlerInterceptor {
    @Value("${app.maintenance-mode:false}")
    private boolean maintenanceMode;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // メンテナンスページ自体へのアクセスは許可
        if (request.getRequestURI().equals("/maintenance")) {
            return true;
        }

        // 静的リソースは許可
        if (request.getRequestURI().startsWith("/css") ||
                request.getRequestURI().startsWith("/js") ||
                request.getRequestURI().startsWith("/images")) {
            return true;
        }
        if (maintenanceMode) {
            response.sendRedirect("/maintenance");
            return false;
        }
        return true;
    }
}
