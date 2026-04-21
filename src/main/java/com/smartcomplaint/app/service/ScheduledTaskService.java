package com.smartcomplaint.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class ScheduledTaskService {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTaskService.class);

    @Autowired
    private DataSource dataSource;

    // [REQUIREMENT MET] @Scheduled - ScheduledTaskService.java
    // Runs every Monday at 8 AM. Note: For quick testing one might change it to fixedRate
    @Scheduled(cron = "0 0 8 * * MON")
    public void generateWeeklyReport() {
        log.info("Running scheduled task: Weekly Complaint Summary");
        
        String sql = "SELECT status, COUNT(*) AS count FROM complaints GROUP BY status";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
             log.info("--- Weekly Complaint Stats ---");
             while (rs.next()) {
                 log.info("Status: {} | Count: {}", rs.getString("status"), rs.getInt("count"));
             }
             log.info("------------------------------");
             
        } catch (SQLException e) {
            log.error("Failed to generate weekly report", e);
        }
    }
}
