package com.smartcomplaint.app.repository;

import com.smartcomplaint.app.enums.ComplaintStatus;
import com.smartcomplaint.app.model.Complaint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    Page<Complaint> findByUserId(Long userId, Pageable pageable);
    List<Complaint> findByUserId(Long userId);
    Page<Complaint> findByStatus(ComplaintStatus status, Pageable pageable);
    Page<Complaint> findByCategoryId(Long categoryId, Pageable pageable);
    long countByStatus(ComplaintStatus status);
}
