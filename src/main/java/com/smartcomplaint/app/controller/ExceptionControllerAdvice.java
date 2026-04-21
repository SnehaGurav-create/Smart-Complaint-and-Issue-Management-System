package com.smartcomplaint.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.security.access.AccessDeniedException;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception e, Model model) {
        model.addAttribute("errorCode", "500");
        model.addAttribute("errorMessage", "Internal Server Error: " + e.getMessage());
        return "error";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadRequest(IllegalArgumentException e, Model model) {
        model.addAttribute("errorCode", "400");
        model.addAttribute("errorMessage", "Bad Request: " + e.getMessage());
        return "error";
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDenied(AccessDeniedException e, Model model) {
        model.addAttribute("errorCode", "403");
        model.addAttribute("errorMessage", "Access Denied: You do not have permission to view this page.");
        return "error";
    }
}
