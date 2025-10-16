package com.ogabek.edunova.service;

import com.ogabek.edunova.dto.PaymentDTO;
import com.ogabek.edunova.dto.UnpaidStudentDTO;
import com.ogabek.edunova.entity.Payment;
import com.ogabek.edunova.mapper.EntityMapper;
import com.ogabek.edunova.repository.PaymentRepository;
import com.ogabek.edunova.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UnpaidPaymentService {
    
    private final PaymentRepository paymentRepository;
    private final StudentRepository studentRepository;
    private final EntityMapper mapper;
    
    public UnpaidPaymentService(PaymentRepository paymentRepository,
                               StudentRepository studentRepository,
                               EntityMapper mapper) {
        this.paymentRepository = paymentRepository;
        this.studentRepository = studentRepository;
        this.mapper = mapper;
    }
    
    @Transactional(readOnly = true)
    public List<PaymentDTO> getAllUnpaidPayments() {
        return paymentRepository.findUnpaidPayments()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<PaymentDTO> getUnpaidPaymentsByMonth(String month) {
        return paymentRepository.findUnpaidPaymentsByMonth(month)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<PaymentDTO> getUnpaidPaymentsByStudent(Long studentId) {
        return paymentRepository.findUnpaidPaymentsByStudent(studentId)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<UnpaidStudentDTO> getUnpaidStudentsSummary() {
        List<Payment> unpaidPayments = paymentRepository.findUnpaidPayments();
        
        // Group by student
        Map<Long, List<Payment>> paymentsByStudent = unpaidPayments.stream()
                .collect(Collectors.groupingBy(p -> p.getStudent().getId()));
        
        return paymentsByStudent.entrySet().stream()
                .map(entry -> {
                    Long studentId = entry.getKey();
                    List<Payment> payments = entry.getValue();
                    
                    String studentName = payments.get(0).getStudent().getName();
                    
                    BigDecimal totalUnpaid = payments.stream()
                            .map(Payment::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    
                    List<UnpaidStudentDTO.UnpaidPaymentDetail> details = payments.stream()
                            .map(p -> new UnpaidStudentDTO.UnpaidPaymentDetail(
                                p.getId(),
                                p.getCourse().getName(),
                                p.getCourse().getTeacher().getName(),
                                p.getAmount(),
                                p.getMonth()
                            ))
                            .collect(Collectors.toList());
                    
                    return new UnpaidStudentDTO(
                        studentId,
                        studentName,
                        totalUnpaid,
                        payments.size(),
                        details
                    );
                })
                .sorted((a, b) -> b.getTotalUnpaidAmount().compareTo(a.getTotalUnpaidAmount()))
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<UnpaidStudentDTO> getUnpaidStudentsSummaryByMonth(String month) {
        List<Payment> unpaidPayments = paymentRepository.findUnpaidPaymentsByMonth(month);
        
        Map<Long, List<Payment>> paymentsByStudent = unpaidPayments.stream()
                .collect(Collectors.groupingBy(p -> p.getStudent().getId()));
        
        return paymentsByStudent.entrySet().stream()
                .map(entry -> {
                    Long studentId = entry.getKey();
                    List<Payment> payments = entry.getValue();
                    
                    String studentName = payments.get(0).getStudent().getName();
                    
                    BigDecimal totalUnpaid = payments.stream()
                            .map(Payment::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    
                    List<UnpaidStudentDTO.UnpaidPaymentDetail> details = payments.stream()
                            .map(p -> new UnpaidStudentDTO.UnpaidPaymentDetail(
                                p.getId(),
                                p.getCourse().getName(),
                                p.getCourse().getTeacher().getName(),
                                p.getAmount(),
                                p.getMonth()
                            ))
                            .collect(Collectors.toList());
                    
                    return new UnpaidStudentDTO(
                        studentId,
                        studentName,
                        totalUnpaid,
                        payments.size(),
                        details
                    );
                })
                .sorted((a, b) -> b.getTotalUnpaidAmount().compareTo(a.getTotalUnpaidAmount()))
                .collect(Collectors.toList());
    }
}