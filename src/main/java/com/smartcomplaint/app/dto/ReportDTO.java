package com.smartcomplaint.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
    private String categoryName;
    private String status;
    private int count;
}
