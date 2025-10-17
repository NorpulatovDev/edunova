package com.ogabek.edunova.service;

import com.ogabek.edunova.dto.PaymentDTO;
import com.ogabek.edunova.dto.UnpaidStudentDTO;
import com.ogabek.edunova.entity.Payment;
import com.ogabek.edunova.entity.Student;
import com.ogabek.edunova.entity.Course;
import com.ogabek.edunova.mapper.EntityMapper;
import com.ogabek.edunova.repository.PaymentRepository;
import com.ogabek.edunova.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    /**
     * Get students who should have payments based on their enrolled courses but don't have any payments for the given month
     */
    @Transactional(readOnly = true)
    public List<UnpaidStudentDTO> getStudentsWithMissingPayments(String month) {
        // Get all students with their courses
        List<Student> studentsWithCourses = studentRepository.findAllWithCourses();

        // Get existing payments for the month
        List<Payment> existingPayments = paymentRepository.findByMonthAndDeletedFalse(month);

        // Create a set of student-course combinations that have payments
        Set<String> paidCombinations = existingPayments.stream()
                .map(p -> p.getStudent().getId() + "-" + p.getCourse().getId())
                .collect(Collectors.toSet());

        return studentsWithCourses.stream()
                .filter(student -> !student.getCourses().isEmpty())
                .map(student -> {
                    List<UnpaidStudentDTO.UnpaidPaymentDetail> missingPayments = student.getCourses().stream()
                            .filter(course -> !course.getDeleted())
                            .filter(course -> !paidCombinations.contains(student.getId() + "-" + course.getId()))
                            .map(course -> new UnpaidStudentDTO.UnpaidPaymentDetail(
                                    null, // No payment ID since payment doesn't exist
                                    course.getName(),
                                    course.getTeacher().getName(),
                                    course.getMonthlyFee(), // Use course fee as expected amount
                                    month
                            ))
                            .collect(Collectors.toList());

                    if (missingPayments.isEmpty()) {
                        return null; // Student has all payments
                    }

                    BigDecimal totalMissingAmount = missingPayments.stream()
                            .map(UnpaidStudentDTO.UnpaidPaymentDetail::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return new UnpaidStudentDTO(
                            student.getId(),
                            student.getName(),
                            totalMissingAmount,
                            missingPayments.size(),
                            missingPayments
                    );
                })
                .filter(dto -> dto != null)
                .sorted((a, b) -> b.getTotalUnpaidAmount().compareTo(a.getTotalUnpaidAmount()))
                .collect(Collectors.toList());
    }

    /**
     * Get complete unpaid summary including both unpaid payments and missing payments
     */
    @Transactional(readOnly = true)
    public List<UnpaidStudentDTO> getCompleteUnpaidSummary(String month) {
        List<UnpaidStudentDTO> unpaidPayments = getUnpaidStudentsSummaryByMonth(month);
        List<UnpaidStudentDTO> missingPayments = getStudentsWithMissingPayments(month);

        // Merge the two lists by student ID
        Map<Long, UnpaidStudentDTO> combinedMap = unpaidPayments.stream()
                .collect(Collectors.toMap(UnpaidStudentDTO::getStudentId, dto -> dto));

        missingPayments.forEach(missingDto -> {
            UnpaidStudentDTO existing = combinedMap.get(missingDto.getStudentId());
            if (existing != null) {
                // Merge unpaid and missing payments
                existing.getUnpaidPayments().addAll(missingDto.getUnpaidPayments());
                existing.setTotalUnpaidAmount(
                        existing.getTotalUnpaidAmount().add(missingDto.getTotalUnpaidAmount())
                );
                existing.setUnpaidPaymentsCount(
                        existing.getUnpaidPaymentsCount() + missingDto.getUnpaidPaymentsCount()
                );
            } else {
                combinedMap.put(missingDto.getStudentId(), missingDto);
            }
        });

        return combinedMap.values().stream()
                .sorted((a, b) -> b.getTotalUnpaidAmount().compareTo(a.getTotalUnpaidAmount()))
                .collect(Collectors.toList());
    }
}