package com.ogabek.edunova.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDTO {
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotNull(message = "Salary percentage is required")
    @DecimalMin(value = "0.0", message = "Salary percentage must be at least 0")
    @DecimalMax(value = "100.0", message = "Salary percentage must not exceed 100")
    private BigDecimal salaryPercentage;
}
