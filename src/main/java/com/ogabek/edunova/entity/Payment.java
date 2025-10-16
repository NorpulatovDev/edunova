package com.ogabek.edunova.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "payments", indexes = {
    @Index(name = "idx_payment_student", columnList = "student_id"),
    @Index(name = "idx_payment_course", columnList = "course_id"),
    @Index(name = "idx_payment_month", columnList = "month"),
    @Index(name = "idx_payment_paid", columnList = "paid")
})
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    
    @Column(nullable = false, length = 7)
    private String month; // Format: YYYY-MM
    
    @Column(nullable = false)
    private Boolean paid = false;
}