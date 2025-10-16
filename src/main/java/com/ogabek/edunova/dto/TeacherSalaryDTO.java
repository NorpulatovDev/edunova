package com.ogabek.edunova.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherSalaryDTO {
    private Long teacherId;
    private String teacherName;
    private BigDecimal salaryPercentage;
    private String month;
    private BigDecimal totalPaymentsReceived;
    private BigDecimal calculatedSalary;
    private List<PaymentDetail> payments;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentDetail {
        private Long paymentId;
        private String studentName;
        private String courseName;
        private BigDecimal amount;
        private Boolean paid;
    }
}
