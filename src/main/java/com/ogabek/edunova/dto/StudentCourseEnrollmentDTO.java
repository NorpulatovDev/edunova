package com.ogabek.edunova.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourseEnrollmentDTO {
    
    @NotNull(message = "Student ID is required")
    private Long studentId;
    
    @NotEmpty(message = "At least one course ID is required")
    private List<Long> courseIds;
}