package com.smartcomplaint.app.controller;

import com.smartcomplaint.app.dto.ComplaintDTO;
import com.smartcomplaint.app.model.Complaint;
import com.smartcomplaint.app.model.User;
import com.smartcomplaint.app.service.ComplaintService;
import com.smartcomplaint.app.service.NotificationService;
import com.smartcomplaint.app.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private NotificationService notificationService;

    private User getCurrentUser(Principal principal) {
        return userService.findByEmail(principal.getName());
    }

    // GET /user/dashboard -> show user home with stats
    @GetMapping({"/dashboard", "/"})
    public String dashboard(Model model, Principal principal) {
        User user = getCurrentUser(principal);
        
        // Stats
        Page<Complaint> userComplaints = complaintService.getUserComplaints(user.getId(), PageRequest.of(0, 5));
        
        long totalComplaints = userComplaints.getTotalElements();
        long pendingComplaints = userComplaints.getContent().stream()
                .filter(c -> c.getStatus().name().equals("PENDING")).count();
        long resolvedComplaints = userComplaints.getContent().stream()
                .filter(c -> c.getStatus().name().equals("RESOLVED")).count();

        model.addAttribute("totalComplaints", totalComplaints);
        model.addAttribute("pendingComplaints", pendingComplaints);
        model.addAttribute("resolvedComplaints", resolvedComplaints);
        model.addAttribute("recentComplaints", userComplaints.getContent());
        
        return "user/dashboard";
    }

    // GET /user/complaints -> list logged-in user's complaints
    @GetMapping("/complaints")
    public String listComplaints(@RequestParam(defaultValue = "0") int page, Model model, Principal principal) {
        User user = getCurrentUser(principal);
        Page<Complaint> complaintPage = complaintService.getUserComplaints(user.getId(), PageRequest.of(page, 10));
        
        model.addAttribute("complaintPage", complaintPage);
        return "user/my-complaints";
    }

    // GET /user/complaints/new -> show submit form
    @GetMapping("/complaints/new")
    public String showComplaintForm(Model model) {
        model.addAttribute("complaintDTO", new ComplaintDTO());
        model.addAttribute("categories", complaintService.getAllCategories());
        return "user/submit-complaint";
    }

    // POST /user/complaints -> save new complaint
    @PostMapping("/complaints")
    public String submitComplaint(@Valid @ModelAttribute("complaintDTO") ComplaintDTO complaintDTO,
                                  BindingResult result, Principal principal, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", complaintService.getAllCategories());
            return "user/submit-complaint";
        }
        
        User user = getCurrentUser(principal);
        complaintService.submitComplaint(complaintDTO, user);
        
        return "redirect:/user/complaints?success";
    }

    // GET /user/complaints/{id} -> view single complaint detail
    @GetMapping("/complaints/{id}")
    public String viewComplaintDetails(@PathVariable Long id, Model model, Principal principal) {
        Complaint complaint = complaintService.getComplaintById(id);
        
        // Simple authorization check
        if (!complaint.getUser().getEmail().equals(principal.getName())) {
            return "redirect:/user/dashboard?error=unauthorized";
        }
        
        model.addAttribute("complaint", complaint);
        return "user/complaint-detail";
    }
}
