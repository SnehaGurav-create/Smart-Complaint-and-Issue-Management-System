package com.smartcomplaint.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusUpdateDTO {
    private Long complaintId;
    private String newStatus;
    private String message;
}
