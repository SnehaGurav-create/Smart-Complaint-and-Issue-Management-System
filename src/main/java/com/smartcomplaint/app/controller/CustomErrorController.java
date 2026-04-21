package com.smartcomplaint.app.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Throwable exception = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        if (exception != null) {
            exception.printStackTrace();
        }
        
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            model.addAttribute("errorCode", statusCode);
            
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("errorMessage", "Page not found");
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("errorMessage", "Access Denied");
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("errorMessage", "Internal Server Error");
            } else {
                model.addAttribute("errorMessage", "Unexpected Error");
            }
        }
        return "error";
    }
}
