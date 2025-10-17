package com.ogabek.edunova.controller;

import com.ogabek.edunova.dto.PaymentDTO;
import com.ogabek.edunova.dto.UnpaidStudentDTO;
import com.ogabek.edunova.service.UnpaidPaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/unpaid")
public class UnpaidPaymentController {

    private final UnpaidPaymentService unpaidPaymentService;

    public UnpaidPaymentController(UnpaidPaymentService unpaidPaymentService) {
        this.unpaidPaymentService = unpaidPaymentService;
    }

    @GetMapping("/payments")
    public ResponseEntity<List<PaymentDTO>> getAllUnpaidPayments() {
        return ResponseEntity.ok(unpaidPaymentService.getAllUnpaidPayments());
    }

    @GetMapping("/payments/month/{month}")
    public ResponseEntity<List<PaymentDTO>> getUnpaidPaymentsByMonth(@PathVariable String month) {
        return ResponseEntity.ok(unpaidPaymentService.getUnpaidPaymentsByMonth(month));
    }

    @GetMapping("/payments/student/{studentId}")
    public ResponseEntity<List<PaymentDTO>> getUnpaidPaymentsByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(unpaidPaymentService.getUnpaidPaymentsByStudent(studentId));
    }

    @GetMapping("/students")
    public ResponseEntity<List<UnpaidStudentDTO>> getUnpaidStudentsSummary() {
        return ResponseEntity.ok(unpaidPaymentService.getUnpaidStudentsSummary());
    }

    @GetMapping("/students/month/{month}")
    public ResponseEntity<List<UnpaidStudentDTO>> getUnpaidStudentsSummaryByMonth(@PathVariable String month) {
        return ResponseEntity.ok(unpaidPaymentService.getUnpaidStudentsSummaryByMonth(month));
    }

    /**
     * Get students who should have payments based on their enrolled courses but don't have any payments for the given month
     */
    @GetMapping("/missing/month/{month}")
    public ResponseEntity<List<UnpaidStudentDTO>> getStudentsWithMissingPayments(@PathVariable String month) {
        return ResponseEntity.ok(unpaidPaymentService.getStudentsWithMissingPayments(month));
    }

    /**
     * Get complete unpaid summary including both unpaid payments and missing payments based on course enrollments
     */
    @GetMapping("/complete/month/{month}")
    public ResponseEntity<List<UnpaidStudentDTO>> getCompleteUnpaidSummary(@PathVariable String month) {
        return ResponseEntity.ok(unpaidPaymentService.getCompleteUnpaidSummary(month));
    }
}