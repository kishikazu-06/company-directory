package com.example.company_directory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MaintenanceController {
    @GetMapping("/maintenance")
    public String maintenance() {
        return "maintenance";
    }
}