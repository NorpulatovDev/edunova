package com.ogabek.edunova.service;

import com.ogabek.edunova.dto.TeacherSalaryDTO;
import com.ogabek.edunova.entity.Payment;
import com.ogabek.edunova.entity.Teacher;
import com.ogabek.edunova.exception.ResourceNotFoundException;
import com.ogabek.edunova.repository.PaymentRepository;
import com.ogabek.edunova.repository.TeacherRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TeacherSalaryService {
    
    private final TeacherRepository teacherRepository;
    private final PaymentRepository paymentRepository;
    
    public TeacherSalaryService(TeacherRepository teacherRepository, 
                               PaymentRepository paymentRepository) {
        this.teacherRepository = teacherRepository;
        this.paymentRepository = paymentRepository;
    }
    
    @Transactional(readOnly = true)
    public TeacherSalaryDTO calculateSalary(Long teacherId, String month) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + teacherId));
        
        if (teacher.getDeleted()) {
            throw new ResourceNotFoundException("Teacher not found with id: " + teacherId);
        }
        
        List<Payment> paidPayments = paymentRepository.findPaidPaymentsByTeacherAndMonth(teacherId, month);
        
        BigDecimal totalPayments = paidPayments.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal calculatedSalary = totalPayments
                .multiply(teacher.getSalaryPercentage())
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        
        List<TeacherSalaryDTO.PaymentDetail> paymentDetails = paidPayments.stream()
                .map(p -> new TeacherSalaryDTO.PaymentDetail(
                    p.getId(),
                    p.getStudent().getName(),
                    p.getCourse().getName(),
                    p.getAmount(),
                    p.getPaid()
                ))
                .collect(Collectors.toList());
        
        log.info("Calculated salary for teacher {} for month {}: {}", teacherId, month, calculatedSalary);
        
        return new TeacherSalaryDTO(
            teacher.getId(),
            teacher.getName(),
            teacher.getSalaryPercentage(),
            month,
            totalPayments,
            calculatedSalary,
            paymentDetails
        );
    }
    
    @Transactional(readOnly = true)
    public List<TeacherSalaryDTO> calculateAllTeachersSalaries(String month) {
        List<Teacher> teachers = teacherRepository.findByDeletedFalse();
        
        return teachers.stream()
                .map(teacher -> calculateSalary(teacher.getId(), month))
                .collect(Collectors.toList());
    }
}