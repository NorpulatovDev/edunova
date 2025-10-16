package com.ogabek.edunova.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnpaidStudentDTO {
    private Long studentId;
    private String studentName;
    private BigDecimal totalUnpaidAmount;
    private Integer unpaidPaymentsCount;
    private List<UnpaidPaymentDetail> unpaidPayments;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UnpaidPaymentDetail {
        private Long paymentId;
        private String courseName;
        private String teacherName;
        private BigDecimal amount;
        private String month;
    }
}