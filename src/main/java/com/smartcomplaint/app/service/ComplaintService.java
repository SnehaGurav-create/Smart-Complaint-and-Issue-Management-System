package com.smartcomplaint.app.service;

import com.smartcomplaint.app.dto.ComplaintDTO;
import com.smartcomplaint.app.enums.ComplaintStatus;
import com.smartcomplaint.app.model.Assignment;
import com.smartcomplaint.app.model.Category;
import com.smartcomplaint.app.model.Complaint;
import com.smartcomplaint.app.model.User;
import com.smartcomplaint.app.repository.AssignmentRepository;
import com.smartcomplaint.app.repository.CategoryRepository;
import com.smartcomplaint.app.repository.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

// [REQUIREMENT MET] @Service layer
@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private ReportService reportService;

    public Page<Complaint> getUserComplaints(Long userId, Pageable pageable) {
        return complaintRepository.findByUserId(userId, pageable);
    }

    public Page<Complaint> getAllComplaints(Pageable pageable) {
        return complaintRepository.findAll(pageable);
    }
    
    public Page<Complaint> getComplaintsByStatus(ComplaintStatus status, Pageable pageable) {
        return complaintRepository.findByStatus(status, pageable);
    }

    @Transactional
    public Complaint submitComplaint(ComplaintDTO dto, User user) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));

        Complaint complaint = new Complaint();
        complaint.setTitle(dto.getTitle());
        complaint.setDescription(dto.getDescription());
        complaint.setCategory(category);
        complaint.setUser(user);
        complaint.setStatus(ComplaintStatus.PENDING);

        Complaint savedComplaint = complaintRepository.save(complaint);
        
        notificationService.sendNotificationAsync(user.getId(), "Your complaint '" + savedComplaint.getTitle() + "' has been submitted successfully.", null, null);

        return savedComplaint;
    }

    public Complaint getComplaintById(Long id) {
        return complaintRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Complaint not found"));
    }

    @Transactional
    public void updateStatus(Long complaintId, ComplaintStatus newStatus, User adminUser) {
        Complaint complaint = getComplaintById(complaintId);
        ComplaintStatus oldStatus = complaint.getStatus();
        
        if (oldStatus != newStatus) {
            complaint.setStatus(newStatus);
            complaintRepository.save(complaint);
            
            // Log to audit trail via JDBC
            reportService.logStatusChange(complaintId, oldStatus.name(), newStatus.name(), adminUser.getId());

            String message = "Status of your complaint '" + complaint.getTitle() + "' has changed to " + newStatus;
            
            // Trigger WebSocket Push + @Async notification DB save
            notificationService.sendNotificationAsync(complaint.getUser().getId(), message, complaint.getUser().getEmail(), newStatus.name());
        }
    }
    
    @Transactional
    public void assignStaff(Long complaintId, User staffUser, User adminUser) {
        Complaint complaint = getComplaintById(complaintId);
        
        Assignment assignment = new Assignment();
        assignment.setComplaint(complaint);
        assignment.setStaff(staffUser);
        assignment.setAssignedAt(LocalDateTime.now());
        
        assignmentRepository.save(assignment);
        
        // Update status to IN_PROGRESS upon assignment
        updateStatus(complaintId, ComplaintStatus.IN_PROGRESS, adminUser);
    }
    
    // Stats
    public long getTotalComplaintsCount() {
        return complaintRepository.count();
    }
    
    public long getPendingComplaintsCount() {
        return complaintRepository.countByStatus(ComplaintStatus.PENDING);
    }
    
    public long getResolvedComplaintsCount() {
        return complaintRepository.countByStatus(ComplaintStatus.RESOLVED);
    }
    
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
