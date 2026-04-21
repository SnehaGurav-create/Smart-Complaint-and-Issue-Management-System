package com.smartcomplaint.app.controller;

import com.smartcomplaint.app.dto.ReportDTO;
import com.smartcomplaint.app.enums.ComplaintStatus;
import com.smartcomplaint.app.model.Complaint;
import com.smartcomplaint.app.model.User;
import com.smartcomplaint.app.service.ComplaintService;
import com.smartcomplaint.app.service.ReportService;
import com.smartcomplaint.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ComplaintService complaintService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ReportService reportService;

    // GET /admin/dashboard -> stats overview
    @GetMapping({"/dashboard", "/"})
    public String dashboard(Model model) {
        model.addAttribute("totalCount", complaintService.getTotalComplaintsCount());
        model.addAttribute("pendingCount", complaintService.getPendingComplaintsCount());
        model.addAttribute("resolvedCount", complaintService.getResolvedComplaintsCount());
        
        Page<Complaint> recent = complaintService.getAllComplaints(PageRequest.of(0, 10));
        model.addAttribute("recentComplaints", recent.getContent());
        
        return "admin/dashboard";
    }

    // GET /admin/complaints -> list ALL complaints
    @GetMapping("/complaints")
    public String allComplaints(@RequestParam(defaultValue = "0") int page, 
                                @RequestParam(required = false) ComplaintStatus status,
                                Model model) {
                                
        Page<Complaint> complaintPage;
        if (status != null) {
            complaintPage = complaintService.getComplaintsByStatus(status, PageRequest.of(page, 15));
        } else {
            complaintPage = complaintService.getAllComplaints(PageRequest.of(page, 15));
        }
        
        model.addAttribute("complaintPage", complaintPage);
        model.addAttribute("currentStatus", status);
        return "admin/all-complaints";
    }

    // GET /admin/complaints/{id}/assign -> show assign form
    @GetMapping("/complaints/{id}/assign")
    public String showAssignForm(@PathVariable Long id, Model model) {
        Complaint complaint = complaintService.getComplaintById(id);
        model.addAttribute("complaint", complaint);
        model.addAttribute("staffList", userService.findAllStaff());
        return "admin/assign-complaint";
    }

    // POST /admin/complaints/{id}/assign -> assign staff
    @PostMapping("/complaints/{id}/assign")
    public String assignStaff(@PathVariable Long id, @RequestParam Long staffId, Principal principal) {
        User staff = userService.findAllStaff().stream()
                    .filter(u -> u.getId().equals(staffId)).findFirst().orElseThrow();
        User admin = userService.findByEmail(principal.getName());
        
        complaintService.assignStaff(id, staff, admin);
        return "redirect:/admin/complaints?assigned";
    }

    // POST /admin/complaints/{id}/status -> update status
    @PostMapping("/complaints/{id}/status")
    public String updateStatus(@PathVariable Long id, @RequestParam ComplaintStatus status, Principal principal) {
        User admin = userService.findByEmail(principal.getName());
        complaintService.updateStatus(id, status, admin);
        return "redirect:/admin/complaints?status_updated";
    }

    // GET /admin/reports -> show generated report via JDBC
    @GetMapping("/reports")
    public String reports(@RequestParam(required = false) String fromDate,
                          @RequestParam(required = false) String toDate,
                          Model model) {
                          
        LocalDate from = fromDate != null && !fromDate.isEmpty() ? LocalDate.parse(fromDate) : LocalDate.now().minusDays(30);
        LocalDate to = toDate != null && !toDate.isEmpty() ? LocalDate.parse(toDate) : LocalDate.now();
        
        List<ReportDTO> reportData = reportService.getComplaintSummaryReport(from, to);
        
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        model.addAttribute("reportData", reportData);
        
        return "admin/reports";
    }
}
