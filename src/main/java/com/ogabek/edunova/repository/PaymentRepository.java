package com.ogabek.edunova.repository;

import com.ogabek.edunova.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByDeletedFalse();
    List<Payment> findByMonthAndDeletedFalse(String month);

    @Query("SELECT p FROM Payment p WHERE p.student.id = :studentId AND p.deleted = false")
    List<Payment> findByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT p FROM Payment p WHERE p.course.teacher.id = :teacherId AND p.month = :month AND p.paid = true AND p.deleted = false")
    List<Payment> findPaidPaymentsByTeacherAndMonth(@Param("teacherId") Long teacherId, @Param("month") String month);

    @Query("SELECT p FROM Payment p WHERE p.paid = false AND p.deleted = false")
    List<Payment> findUnpaidPayments();

    @Query("SELECT p FROM Payment p WHERE p.month = :month AND p.paid = false AND p.deleted = false")
    List<Payment> findUnpaidPaymentsByMonth(@Param("month") String month);

    @Query("SELECT p FROM Payment p WHERE p.student.id = :studentId AND p.paid = false AND p.deleted = false")
    List<Payment> findUnpaidPaymentsByStudent(@Param("studentId") Long studentId);
}
